package hcmute.kltn.vtv.service.shipping.impl;

import hcmute.kltn.vtv.model.data.shipping.request.ShopAndTransportResponse;
import hcmute.kltn.vtv.model.data.shipping.response.TransportResponse;
import hcmute.kltn.vtv.model.dto.shipping.ShippingDTO;
import hcmute.kltn.vtv.model.dto.shipping.ShopAndTransportsDTO;
import hcmute.kltn.vtv.model.entity.location.Ward;
import hcmute.kltn.vtv.model.entity.shipping.Deliver;
import hcmute.kltn.vtv.model.entity.shipping.Transport;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.entity.user.VoucherOrder;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.TransportStatus;
import hcmute.kltn.vtv.model.extra.TypeWork;
import hcmute.kltn.vtv.repository.shipping.TransportRepository;
import hcmute.kltn.vtv.repository.user.OrderRepository;
import hcmute.kltn.vtv.service.location.IWardService;
import hcmute.kltn.vtv.service.shipping.*;
import hcmute.kltn.vtv.service.user.IOrderItemService;
import hcmute.kltn.vtv.service.user.IVoucherOrderService;
import hcmute.kltn.vtv.service.vendor.IShopService;
import hcmute.kltn.vtv.service.vtv.IMailService;
import hcmute.kltn.vtv.service.vtv.INotificationService;
import hcmute.kltn.vtv.service.wallet.ILoyaltyPointService;
import hcmute.kltn.vtv.service.wallet.IWalletService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TransportServiceImpl implements ITransportService {

    private final TransportRepository transportRepository;
    private final ITransportHandleService transportHandleService;
    private final IDeliverService deliverService;
    private final IWardService wardService;
    private final OrderRepository orderRepository;
    private final ITransportShopService transportShopService;
    private final ICashOrderService cashOrderService;
    private final IWalletService walletService;
    private final IVoucherOrderService voucherOrderService;
    private final ILoyaltyPointService loyaltyPointService;
    private final IOrderItemService orderItemService;
    private final INotificationService notificationService;
    private final IShopService shopService;
    private final IMailService mailService;
    private final IShippingService shippingService;


    @Transactional
    @Override
    public Transport addNewTransport(UUID orderId) {
        Transport transport = createTransport(orderId);
        try {
            return transportRepository.save(transport);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi tạo mới một đơn vận chuyển mới!");
        }
    }


    @Override
    @Transactional
    public Transport updateStatusTransport(Transport transport, String wardCode, String username,
                                           boolean handled, TransportStatus transportStatus) {
        transport.setStatus(transportStatus);
        transport.setUpdateAt(LocalDateTime.now());
        try {
            transportHandleService.addNewTransportHandleByOrderId(transport.getOrderId(), wardCode, username, handled, transportStatus);
            return transportRepository.save(transport);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi cập nhật trạng thái đơn vận chuyển!");
        }
    }


    @Override
    public Transport updateStatusTransportByOrderId(UUID orderId, String wardCode, String username,
                                                    boolean handled, TransportStatus transportStatus) {
        Transport transport = getTransportByOrderId(orderId);
        return updateStatusTransport(transport, wardCode, username, handled, transportStatus);
    }


    @Override
    public Transport updateStatusTransportByTransportId(UUID transportId, String wardCode, String username,
                                                        boolean handled, TransportStatus transportStatus) {
        Transport transport = getTransportById(transportId);
        if (transport.getStatus().equals(TransportStatus.COMPLETED)) {
            throw new BadRequestException("Dịch vụ vận chuyển đã hoàn thành không thể cập nhật trạng thái!");
        }
        updateStatusTransport(transport, wardCode, username, handled, transportStatus);
        return getTransportById(transportId);
    }


    @Override
    public Transport getTransportById(UUID transportId) {
        Transport transport = transportRepository.findByTransportId(transportId)
                .orElseThrow(() -> new InternalServerErrorException("Không tìm thấy đơn vận chuyển với id: " + transportId));
        transport.setTransportHandles(transportHandleService.getAllTransportHandleByTransportId(transport.getTransportId()));
        return transport;
    }


    @Override
    public Transport getTransportByOrderId(UUID orderId) {
        Transport transport = transportRepository.findByOrderId(orderId)
                .orElseThrow(() -> new InternalServerErrorException("Không tìm thấy đơn vận chuyển theo mã đơn hàng: " + orderId));

        transport.setTransportHandles(transportHandleService.getAllTransportHandleByTransportId(transport.getTransportId()));
        return transport;
    }


    @Override
    @Transactional
    public TransportResponse updateTransportStatusByDeliver(UUID transportId, String username, boolean handled,
                                                            TransportStatus transportStatus, String wardCode) {
        checkStatusTransportBeforeUpdateStatusTransportByTransportId(transportId, transportStatus);
        checkStatusOrderBeforeUpdateTransportStatusByDeliver(transportId);
        wardService.checkExistWardCode(wardCode);

        Deliver deliver = deliverService.checkTypeWorkDeliverWithTransportStatus(username, transportStatus);
        checkDeliverCanUpdateStatus(transportId, deliver);
        checkTransportStatusAndAddCashOrderByTransportId(transportId, username, transportStatus, getPaymentMethodByTransportId(transportId));

        Transport transport = updateStatusTransportByTransportId(transportId, wardCode, username, handled, transportStatus);
        handleUpdateTransportStatusWareHouseByDeliver(transport, transportStatus);
        notificationByShipper(transport, transportStatus);


        try {
            updateStatusOrderByDeliver(transport.getOrderId(), transportStatus);


            return TransportResponse.transportResponse(transport, "Dịch vụ vận chuyển đã được cập nhật trạng thái thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi cập nhật trạng thái vận chuyển bởi nhân viên vận chuyển cho đơn hàng trả hàng! " + e.getMessage());
        }
    }


    public void handleUpdateTransportStatusWareHouseByDeliver(Transport transport, TransportStatus transportStatus) {
        if (transportStatus.equals(TransportStatus.WAREHOUSE) && cashOrderService.checkExistCashOrderByTransport_Id(transport.getTransportId())) {
            cashOrderService.deleteCashOrderByTransportIdAndTransportStatus(transport.getTransportId(), TransportStatus.SHIPPING);
        }
    }


    @Override
    @Transactional
    public TransportResponse updateTransportStatusWithReturnOrderByDeliver(UUID transportId, String username, boolean handled,
                                                                           TransportStatus transportStatus, String wardCode) {
        checkExistTransportByTransportIdAndStatus(transportId, TransportStatus.DELIVERED);
        checkStatusOrderBeforeUpdateStatusWithRefundedOrderByTransportId(transportId);
        wardService.checkExistWardCode(wardCode);
        Deliver deliver = deliverService.checkTypeWorkDeliverWithTransportStatus(username, transportStatus);
        checkDeliverCanUpdateStatus(transportId, deliver);
        try {
            Transport transport = updateStatusTransportByTransportId(transportId, wardCode, username, handled, transportStatus);
            if (transportStatus.equals(TransportStatus.DELIVERED)) {
                orderItemService.cancelOrderItem(getOrderByTransportId(transportId));
            }

            return TransportResponse.transportResponse(transport, "Dịch vụ vận chuyển đã được cập nhật trạng thái thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi cập nhật trạng thái vận chuyển bởi nhân viên vận chuyển! " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public TransportResponse updateTransportStatusWithCancelReturnOrderByDeliver(UUID transportId, String username, String reason) {
        Order order = getOrderByTransportId(transportId);
        checkExistOrderByOrderIdAndOrderStatus(order.getOrderId(), OrderStatus.RETURNED);
        hasPickupOrShipperRole(username);
        try {
            Transport transport = updateStatusTransportByTransportId(transportId, order.getAddress().getWard().getWardCode(), username, true, TransportStatus.COMPLETED);
            updateCancelReturnOrderByDeliver(order, reason);

            return TransportResponse.transportResponse(transport, "Dịch vụ vận chuyển không thể hoàn thành đơn hàng trả hàng!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi dịch vụ vận chuyển xác nhận hủy đơn hàng trả hàng! " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public TransportResponse updateTransportStatusWithSuccessReturnOrderByDeliver(UUID transportId, String username, String reason) {
        Order order = getOrderByTransportId(transportId);
        checkExistOrderByOrderIdAndOrderStatus(order.getOrderId(), OrderStatus.RETURNED);
        hasPickupOrShipperRole(username);

        try {
            if (cashOrderService.checkExistCashOrderByOrderIdAndShipperUsernameWithShipperHold(order.getOrderId(), username, Status.ACTIVE)) {
                cashOrderService.updateCashOrderByOrderIdAndShipperUsernameWithSuccessReturnOrder(order.getOrderId(), username);
            } else {
                walletService.updateWalletByUsername(order.getCustomer().getUsername(), order.getOrderId(), order.getPaymentTotal(), "REFUND");
            }

            if (order.getVoucherOrders() != null) {
                for (VoucherOrder voucherOrder : order.getVoucherOrders()) {
                    voucherOrderService.cancelVoucherOrder(voucherOrder.getVoucherOrderId());
                }
            }
            if (order.getLoyaltyPointHistory() != null) {
                Long point = -order.getLoyaltyPointHistory().getPoint();
                loyaltyPointService.updatePointInLoyaltyPointByUsername(order.getCustomer().getUsername(), point, "REFUND");
            }

            Transport transport = updateStatusTransportByTransportId(transportId, order.getAddress().getWard().getWardCode(), username, true, TransportStatus.PICKED_UP);
            updateSuccessReturnOrderByDeliver(order, reason);
            cashOrderService.updateStatusDeleteCashOrderByTransport(transport);

            return TransportResponse.transportResponse(transport, "Dịch vụ vận chuyển đã lấy hàng trả hàng thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi dịch vụ vận chuyển lấy hàng trả hàng thất bại! " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public TransportResponse updateReturnTransportByWarehouse(String username, UUID transportId, String reason) {

        deliverService.checkTypeWorkDeliverWithTransportStatus(username, TransportStatus.WAREHOUSE);

        Transport transport = getTransportById(transportId);
        if (!getTransportById(transportId).getStatus().equals(TransportStatus.WAREHOUSE)) {
            throw new BadRequestException("Nhân viên kho không thể cập nhật trạng thái hoàn vận chuyển khi không ở trạng thái kho!");
        }
        Order order = getOrderByTransportId(transportId);


        try {
            if (cashOrderService.checkExistCashOrderByTransport_Id(order.getOrderId())) {
                cashOrderService.updateStatusDeleteCashOrderByTransport(transport);
            }

            if(!order.getPaymentMethod().equals("COD")){
                walletService.updateWalletByUsername(order.getCustomer().getUsername(), order.getOrderId(), order.getPaymentTotal(), "REFUND");
            }

            if (order.getVoucherOrders() != null) {
                for (VoucherOrder voucherOrder : order.getVoucherOrders()) {
                    voucherOrderService.cancelVoucherOrder(voucherOrder.getVoucherOrderId());
                }
            }
            if (order.getLoyaltyPointHistory() != null) {
                Long point = -order.getLoyaltyPointHistory().getPoint();
                loyaltyPointService.updatePointInLoyaltyPointByUsername(order.getCustomer().getUsername(), point, "REFUND");
            }

            Transport transportUpdate = updateStatusTransportByTransportId(transportId, order.getAddress().getWard().getWardCode(), username, true, TransportStatus.PICKED_UP);
            updateSuccessReturnOrderByDeliver(order, reason);

            return TransportResponse.transportResponse(transportUpdate, "Nhân viên kho đã xác nhận trả hàng thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi nhân viên kho xác nhận trả hàng! " + e.getMessage());
        }
    }



    @Override
    public TransportResponse getTransportResponseByTransportId(UUID transportId) {
        try {
            Transport transport = getTransportById(transportId);
            return TransportResponse.transportResponse(transport, "Lấy thông tin dịch vụ vận chuyển thành công!", "OK");
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi lấy thông tin dịch vụ vận chuyển theo id: " + transportId + "! " + e.getMessage());
        }
    }


    @Override
    public ShopAndTransportResponse getTransportsByWardWorksDeliver(String username) {
        Deliver deliver = deliverService.checkTypeWorkDeliverWithTransportStatus(username, TransportStatus.PICKED_UP);
        List<Shop> shops = transportShopService.getShopsByWards(deliver.getWardsWork());
        try {
            List<Transport> transports = transportRepository.findAllByShopIdInAndStatus(shops.stream().map(Shop::getShopId).toList(), TransportStatus.PICKUP_PENDING)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy dịch vụ vận chuyển nào!"));

            String message = "Lấy danh sách đơn vận chuyển theo phường làm việc của nhân viên vận chuyển thành công theo trạng thái: "
                    + convertTransportStatusToString(TransportStatus.PICKUP_PENDING);

            return ShopAndTransportResponse.shopAndTransportResponse(shops, deliver.getWardsWork(), transports, message);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi lấy danh sách đơn vận chuyển theo phường làm việc của nhân viên vận chuyển! " + e.getMessage());
        }
    }


    @Override
    public ShopAndTransportResponse getTransportsByDistrictCode(String districtCode) {
        List<Ward> wards = wardService.getWardsByDistrictCode(districtCode);
        List<Shop> shops = transportShopService.getShopsByWards(wards);

        try {
            List<Transport> transports = transportRepository.findAllByShopIdInAndStatus(shops.stream().map(Shop::getShopId).toList(), TransportStatus.PICKUP_PENDING)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy dịch vụ vận chuyển nào!"));

            String message = "Lấy danh sách đơn vận chuyển theo mã quận/huyện: " + districtCode + " với trạng thái: "
                    + convertTransportStatusToString(TransportStatus.PICKUP_PENDING);

            return ShopAndTransportResponse.shopAndTransportResponse(shops, wards, transports, message);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi lấy danh sách đơn vận chuyển theo mã quận/huyện: " + districtCode + "! " + e.getMessage());
        }
    }


    @Override
    public ShopAndTransportResponse getTransportsByWard(String wardCode, String username) {
        try {
            wardService.checkExistWardCode(wardCode);
            Deliver deliver = deliverService.checkTypeWorkDeliverWithTransportStatus(username, TransportStatus.PICKED_UP);
            List<Shop> shops = transportShopService.getShopsByWardCode(wardCode);
            List<Transport> transports = transportRepository.findAllByShopIdInAndStatus(shops.stream().map(Shop::getShopId).toList(), TransportStatus.PICKUP_PENDING)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy dịch vụ vận chuyển nào!"));

            String message = "Lấy danh sách đơn vận chuyển theo mã phường: " + wardCode + " với trạng thái: "
                    + convertTransportStatusToString(TransportStatus.PICKUP_PENDING);

            return ShopAndTransportResponse.shopAndTransportResponse(shops, deliver.getWardsWork(), transports, message);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi lấy danh sách đơn vận chuyển theo mã phường: " + wardCode + "! " + e.getMessage());
        }
    }


    @Override
    public ShopAndTransportsDTO getShopAndTransportsDTOByShopId(Long shopId, TransportStatus status) {
        Shop shop = shopService.getShopByShopId(shopId);
        List<Transport> transports = transportRepository.findAllByShopIdAndStatus(shopId, status)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy dịch vụ vận chuyển nào!"));

        return ShopAndTransportsDTO.shopAndTransportsDTO(transports, shop);
    }


    private void checkExistTransportByTransportIdAndStatus(UUID transportId, TransportStatus transportStatus) {
        if (transportRepository.existsByTransportIdAndStatus(transportId, transportStatus)) {
            throw new BadRequestException("Đơn vận chuyển đã tồn tại với trạng thái: " + convertTransportStatusToString(transportStatus));
        }
    }


    private void notificationByShipper(Transport transport, TransportStatus transportStatus) {
        if (transportStatus.equals(TransportStatus.DELIVERED)) {
            Order order = getOrderByOrderId(transport.getOrderId());
            ShippingDTO shippingDTO = shippingService.getCalculateShippingByWardAndTransportProvider(order.getAddress().getWard().getWardCode(),
                    order.getShop().getWard().getWardCode(), order.getShippingMethod()).getShippingDTO();
            String titleNotification = "Đơn hàng đã được giao thành công! Mã đơn hàng #" + transport.getOrderId();
            String bodyNotification = "Đơn hàng của bạn đã được giao thành công! Vui lòng xác nhận đã nhận hàng! " +
                    "Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi! Mã đơn hàng: #" + transport.getOrderId();
            String messageEmail = "Đơn hàng của bạn đã được giao thành công! Vui lòng xác nhận đã nhận hàng! " +
                    "Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi! Mã vận chuyển: " +
                    transport.getTransportId() + " - Mã đơn hàng: #" + transport.getOrderId();

            notificationService.addNewNotification(
                    titleNotification,
                    bodyNotification,
                    order.getCustomer().getUsername(),
                    "shipper",
                    "ORDER"
            );

            mailService.sendOrderConfirmationEmail(order, shippingDTO, messageEmail);
        }
    }


    private void hasPickupOrShipperRole(String username) {
        if (!deliverService.checkBooleanDeliverUseRole(username, TypeWork.PICKUP) &&
                !deliverService.checkBooleanDeliverUseRole(username, TypeWork.SHIPPER)) {
            throw new BadRequestException("Bạn không có quyền thực hiện hành động này!");
        }
    }


    private void checkDeliverCanUpdateStatus(UUID transportId, Deliver deliver) {
        Transport transport = getTransportById(transportId);
        if (!deliver.getTransportProvider().getShortName().equals(transport.getShippingMethod())) {
            throw new BadRequestException("Nhân viên vận không cùng nhà vận chuyển với đơn hàng! Không thể cập nhật trạng thái!");
        }
        if (transport.getStatus().equals(TransportStatus.CANCEL) ||
                transport.getStatus().equals(TransportStatus.PENDING) ||
                transport.getStatus().equals(TransportStatus.WAITING)) {
            throw new BadRequestException("Nhân viên vận không thể cập nhật trạng thái do đơn hàng đã được xử lý hoặc đã hủy!");
        }
    }


    public void checkTransportStatusAndAddCashOrderByTransportId(UUID transportId, String username,
                                                                 TransportStatus transportStatus, String paymentMethod) {
        if (!transportStatus.equals(TransportStatus.SHIPPING) && !transportStatus.equals(TransportStatus.DELIVERED)) {
            return;
        }
        try {
            boolean isCOD = paymentMethod.equals("COD");
            Status status = isCOD ? Status.ACTIVE : Status.INACTIVE;
            boolean isDelivered = transportStatus.equals(TransportStatus.DELIVERED);
            boolean isHold = isCOD ? isDelivered : false;
            cashOrderService.addNewCashOrder(transportId, username, isHold, status);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi tạo đơn thu tiền theo mã vận chuyển: " + transportId + "! " + e.getMessage());
        }
    }


    public Transport createTransport(UUID orderId) {

        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng với mã: " + orderId));
        Transport transport = new Transport();
        transport.setOrderId(orderId);
        transport.setShopId(order.getShop().getShopId());
        transport.setWardCodeShop(order.getShop().getWard().getWardCode());
        transport.setWardCodeCustomer(order.getAddress().getWard().getWardCode());
        transport.setShippingMethod(order.getShippingMethod());
        transport.setStatus(TransportStatus.WAITING);
        transport.setCreateAt(LocalDateTime.now());
        transport.setUpdateAt(LocalDateTime.now());

        return transport;
    }


    private void updateStatusOrderByDeliver(UUID orderId, TransportStatus transportStatus) {
        Order order = getOrderByOrderId(orderId);
        order.setStatus(convertTransportStatusToOrderStatus(transportStatus));
        order.setUpdateAt(LocalDateTime.now());

        try {
            orderRepository.save(order);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi cập nhật trạng thái đơn hàng theo trạng thái vận chuyển của nhân viên vận chuyển!");
        }
    }


    private void checkStatusOrderBeforeUpdateTransportStatusByDeliver(UUID transportId) {
        Order order = getOrderByOrderId(getTransportById(transportId).getOrderId());
        if (!order.getStatus().equals(OrderStatus.PICKUP_PENDING) && !order.getStatus().equals(OrderStatus.SHIPPING)) {
            throw new BadRequestException("Trạng thái đơn hàng không hợp lệ để cập nhật trạng thái vận chuyển!");
        }
    }


    private void updateCancelReturnOrderByDeliver(Order order, String reason) {
        order.setStatus(OrderStatus.COMPLETED);
        order.setUpdateAt(LocalDateTime.now());
        order.setNote(order.getNote() + " - #Lý do trả hàng thất bại: " + reason);

        try {
            processPaymentIfCancelReturnOrder(order);

            orderRepository.save(order);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi cập nhật trạng thái đơn hàng khi hủy đơn hàng trả hàng!");
        }
    }


    private void processPaymentIfCancelReturnOrder(Order order) {
        if (!order.getPaymentMethod().equals("COD")) {
            handlePayment(order, "COMPLETED_ORDER");
        } else if (cashOrderService.checkCashOrderByOrderIdWithHandlePaymentAndStatus(
                order.getOrderId(), false, true, false, Status.ACTIVE)) {
            handlePayment(order, "COMPLETED_ORDER_COD");
        }
    }


    private void updateSuccessReturnOrderByDeliver(Order order, String reason) {
        order.setStatus(OrderStatus.REFUNDED);
        order.setUpdateAt(LocalDateTime.now());
        order.setNote(order.getNote() + " - #Lý do trả hàng thành công: " + reason);

        try {

            orderRepository.save(order);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi cập nhật trạng thái đơn hàng khi hoàn thành đơn hàng trả hàng!");
        }
    }


    private Order getOrderByOrderId(UUID orderId) {
        return orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng với mã: " + orderId));
    }


    private OrderStatus convertTransportStatusToOrderStatus(TransportStatus transportStatus) {
        return switch (transportStatus) {
            case DELIVERED -> OrderStatus.DELIVERED;
            case RETURNED -> OrderStatus.RETURNED;
            case CANCEL -> OrderStatus.CANCEL;
            case COMPLETED -> OrderStatus.COMPLETED;
            default -> OrderStatus.SHIPPING;
        };
    }

    private String convertTransportStatusToString(TransportStatus transportStatus) {
        return switch (transportStatus) {
            case PENDING -> "Chờ xác nhận";
            case WAITING -> "Chờ xử lý";
            case PROCESSING -> "Đang xử lý";
            case PICKUP_PENDING -> "Chờ lấy hàng";
            case PICKED_UP -> "Đã lấy hàng";
            case UNPAID -> "Chưa thanh toán";
            case SHIPPING -> "Đang giao hàng";
            case IN_TRANSIT -> "Đang vận chuyển";
            case WAREHOUSE -> "Đã về kho";
            case DELIVERED -> "Đã giao hàng";
            case RETURNED -> "Đã trả hàng";
            case CANCEL -> "Đã hủy";
            case COMPLETED -> "Đã hoàn thành";
        };
    }


    private void checkStatusTransportBeforeUpdateStatusTransportByTransportId(UUID transportId, TransportStatus transportStatus) {
        Transport transport = getTransportById(transportId);
        if (transport.getStatus().equals(TransportStatus.COMPLETED) ||
                transport.getStatus().equals(TransportStatus.DELIVERED) ||
                transport.getStatus().equals(TransportStatus.RETURNED)) {
            throw new BadRequestException("Dịch vụ vận chuyển đã hoàn thành hoặc đã hủy không thể cập nhật trạng thái!");
        }

        if (transport.getStatus().equals(TransportStatus.PICKUP_PENDING) && !transportStatus.equals(TransportStatus.PICKED_UP)) {
            throw new BadRequestException("Dịch vụ vận chuyển chưa lấy hàng không thể cập nhật trạng thái khác!");
        }
    }


    private void checkStatusOrderBeforeUpdateStatusWithRefundedOrderByTransportId(UUID transportId) {
        Transport transport = getTransportById(transportId);
        if (!orderRepository.existsByOrderIdAndStatus(transport.getOrderId(), OrderStatus.REFUNDED)) {
            throw new BadRequestException("Đơn hàng này không phải là đơn hàng trả hàng và đ hoàn tiền");
        }
    }


    private String getPaymentMethodByTransportId(UUID transportId) {
        Order order = getOrderByTransportId(transportId);

        return order.getPaymentMethod();
    }


    private Order getOrderByTransportId(UUID transportId) {
        Transport transport = getTransportById(transportId);
        return orderRepository.findByOrderId(transport.getOrderId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng theo mã vận chuyển: " + transportId));
    }


    private void checkExistOrderByOrderIdAndOrderStatus(UUID orderId, OrderStatus orderStatus) {
        if (!orderRepository.existsByOrderIdAndStatus(orderId, orderStatus)) {
            throw new NotFoundException("Không tìm thấy đơn hàng theo mã: " + orderId + " và trạng thái: " + orderStatus);
        }
    }


    private void handlePayment(Order order, String paymentType) {
        cashOrderService.updateCashOrderByOrderIdWithHandlePayment(order.getOrderId());
        walletService.updateWalletByUsername(
                order.getShop().getCustomer().getUsername(),
                order.getOrderId(),
                order.getTotalPrice() * 96 / 100 - order.getDiscountShop(),
                paymentType);
    }


}
