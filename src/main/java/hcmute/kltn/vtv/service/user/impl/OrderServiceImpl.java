package hcmute.kltn.vtv.service.user.impl;

import hcmute.kltn.vtv.model.data.user.request.OrderRequestWithCart;
import hcmute.kltn.vtv.model.data.user.request.OrderRequestWithProductVariant;
import hcmute.kltn.vtv.model.dto.shipping.ShippingDTO;
import hcmute.kltn.vtv.model.entity.shipping.Transport;
import hcmute.kltn.vtv.model.entity.user.*;
import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPoint;
import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPointHistory;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.TransportStatus;
import hcmute.kltn.vtv.service.guest.IProductVariantService;
import hcmute.kltn.vtv.service.location.IDistanceLocationService;
import hcmute.kltn.vtv.service.shipping.*;

import hcmute.kltn.vtv.service.vtv.IMailService;
import hcmute.kltn.vtv.service.vtv.INotificationService;
import hcmute.kltn.vtv.service.wallet.ILoyaltyPointService;
import hcmute.kltn.vtv.service.wallet.IWalletService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.user.response.ListOrderResponse;
import hcmute.kltn.vtv.model.data.user.response.OrderResponse;
import hcmute.kltn.vtv.repository.user.OrderRepository;
import hcmute.kltn.vtv.service.user.*;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final IOrderItemService orderItemService;
    private final IVoucherOrderService voucherOrderService;
    private final ICartService cartService;
    private final ICustomerService customerService;
    private final IAddressService addressService;
    private final IDistanceLocationService distanceLocationService;
    private final IProductVariantService productVariantService;
    private final IVoucherCustomerService voucherCustomerService;
    private final IShippingService shippingService;
    private final ILoyaltyPointService loyaltyPointService;
    private final IMailService mailService;
    private final INotificationService notificationService;
    private final ITransportService transportService;
    private final ITransportHandleService transportHandleService;
    private final IWalletService walletService;
    private final ICashOrderService cashOrderService;
    private final IFeeShippingService feeShippingService;

    @Override
    @Transactional
    public OrderResponse createOrderByCartIds(String username, List<UUID> cartIds) {
        cartService.checkDuplicateCartIds(cartIds);
        cartService.checkListCartSameShop(username, cartIds);

        List<OrderItem> orderItems = orderItemService.createOrderItemsByCartIds(username, cartIds);
        Address address = addressService.getAddressActiveByUsername(username);

        Order order = createBaseOrder(username, address);
        updateCreateOrderByOrderItemsPaymentMethod(order, orderItems, "COD");
        updateShippingInCreateOrder(order, "VTV Express");

        return handleAfterCreateOrder(order, "VTV Express", address.getWard().getWardCode(),
                order.getShop().getWard().getWardCode(), "Tạo đơn hàng mới thành công từ danh sách mã giỏ hàng.");

    }


    @Override
    @Transactional
    public OrderResponse createOrderByMapProductVariantsAndQuantities(String username, Map<Long, Integer> productVariantIdsAndQuantities) {
        productVariantService.checkDuplicateProductVariantIds(new ArrayList<>(productVariantIdsAndQuantities.keySet()));
        Long shopId = getShopIdOfProductVariantId(new ArrayList<>(productVariantIdsAndQuantities.keySet()).get(0));
        productVariantService.checkProductVariantsSameShop(new ArrayList<>(productVariantIdsAndQuantities.keySet()), shopId);
        Address address = addressService.getAddressActiveByUsername(username);

        Order order = createBaseOrder(username, address);
        List<OrderItem> orderItems = orderItemService.createOrderItemsByMapProductVariantIdsAndQuantities(order.getCustomer(),
                productVariantIdsAndQuantities);
        updateCreateOrderByOrderItemsPaymentMethod(order, orderItems, "COD");
        updateShippingInCreateOrder(order, "VTV Express");


        return handleAfterCreateOrder(order, "VTV Express", address.getWard().getWardCode(),
                order.getShop().getWard().getWardCode(), "Tạo đơn hàng mới thành công từ danh sách sản phẩm và số lượng.");
    }


    @Override
    @Transactional
    public OrderResponse createOrderWithProductVariant(OrderRequestWithProductVariant request, String username) {
        Long shopId = getShopIdOfProductVariantId(new ArrayList<>(request.getProductVariantIdsAndQuantities().keySet()).get(0));

        productVariantService.checkProductVariantsSameShop(new ArrayList<>(request.getProductVariantIdsAndQuantities().keySet()), shopId);
        Address address = addressService.checkAddress(request.getAddressId(), username);

        Order order = createOrderByOrderRequestWithProductVariant(request, username, address);

        return handleAfterCreateOrder(order, request.getShippingMethod(), address.getWard().getWardCode(),
                order.getShop().getWard().getWardCode(), "Tạo đơn hàng mới thành công từ danh sách sản phẩm và số lượng.");
    }


    @Override
    @Transactional
    public OrderResponse createOrderWithCart(OrderRequestWithCart request, String username) {
        cartService.checkDuplicateCartIds(request.getCartIds());
        cartService.checkListCartSameShop(username, request.getCartIds());
        Address address = addressService.checkAddress(request.getAddressId(), username);

        Order order = createOrderByOrderRequestWithCartIds(request, username, address);

        return handleAfterCreateOrder(order, request.getShippingMethod(), address.getWard().getWardCode(),
                order.getShop().getWard().getWardCode(), "Tạo đơn hàng mới thành công từ danh sách sản phẩm trong giỏ hàng.");
    }


    @Override
    @Transactional
    public OrderResponse addNewOrderWithCart(OrderRequestWithCart request, String username) {
        cartService.checkDuplicateCartIds(request.getCartIds());
        cartService.checkListCartSameShop(username, request.getCartIds());
        Order order = createAddNewOrderWithCart(request, username);

        try {
            addOrderItemsToOrder(order, request.getCartIds(), username);
            addVoucherOrderToOrder(order, request.getShopVoucherCode(), request.getSystemVoucherCode());
            if (request.isUseLoyaltyPoint()) {
                addLoyaltyPointHistoryToOrder(order);
            }
            updateShippingInCreateOrder(order, request.getShippingMethod());
            if (request.getPaymentMethod().equals("Wallet")) {
                walletService.updateWalletByUsername(username, order.getOrderId(), -order.getPaymentTotal(), "PAYMENT_WALLET");
            }
            orderRepository.save(order);
            transportService.addNewTransport(order.getOrderId());

            String messageEmail = "Đặt hàng thành công.";
            String messageResponse = "Đặt hàng thành công từ danh sách sản phẩm trong giỏ hàng.";
            String titleNotification = "Có đơn hàng mới";
            String bodyNotification = "Bạn có đơn hàng mới từ tài khoản " + order.getCustomer().getUsername() + " với mã đơn hàng #" + order.getOrderId();
            TransportStatus transportStatus = order.getPaymentMethod().equals("VNPay") ? TransportStatus.UNPAID : TransportStatus.PENDING;

            return handleAfterSaveOrder(order, messageEmail, messageResponse, titleNotification, bodyNotification, transportStatus);
        } catch (Exception e) {
            throw new InternalServerErrorException("Đặt hàng thất bại từ danh sách sản phẩm trong giỏ hàng! " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public OrderResponse addNewOrderWithProductVariant(OrderRequestWithProductVariant request, String username) {
        Long shopId = getShopIdOfProductVariantId(new ArrayList<>(request.getProductVariantIdsAndQuantities().keySet()).get(0));
        productVariantService.checkProductVariantsSameShop(new ArrayList<>(request.getProductVariantIdsAndQuantities().keySet()), shopId);
        Order order = createAddNewOrderWithCart(OrderRequestWithCart.convertWithProductVariantToWithCart(request), username);

        try {
            addOrderItemsToOrderByMapProductVariant(order, request.getProductVariantIdsAndQuantities(), username);
            addVoucherOrderToOrder(order, request.getShopVoucherCode(), request.getSystemVoucherCode());
            if (request.isUseLoyaltyPoint()) {
                addLoyaltyPointHistoryToOrder(order);
            }
            updateShippingInCreateOrder(order, request.getShippingMethod());
            if (request.getPaymentMethod().equals("Wallet")) {
                walletService.updateWalletByUsername(username, order.getOrderId(), -order.getPaymentTotal(), "PAYMENT_WALLET");
            }
            orderRepository.save(order);
            transportService.addNewTransport(order.getOrderId());

            String messageEmail = "Đặt hàng thành công.";
            String messageResponse = "Đặt hàng thành công từ danh sách sản phẩm.";
            String titleNotification = "Có đơn hàng mới";
            String bodyNotification = "Bạn có đơn hàng mới từ tài khoản " + order.getCustomer().getUsername() + " với mã đơn hàng #" + order.getOrderId();
            TransportStatus transportStatus = order.getPaymentMethod().equals("VNPay") ? TransportStatus.UNPAID : TransportStatus.PENDING;

            return handleAfterSaveOrder(order, messageEmail, messageResponse, titleNotification, bodyNotification, transportStatus);
        } catch (Exception e) {
            throw new InternalServerErrorException("Đặt hàng thất bại từ danh sách sản phẩm! " + e.getMessage());
        }
    }


    @Override
    public ListOrderResponse getOrders(String username) {
        List<Order> orders = orderRepository.findAllByCustomerUsername(username)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy đơn hàng!"));
        return ListOrderResponse.listOrderResponse(orders, "Lấy danh sách đơn hàng thành công.", "OK");

    }


    @Override
    public ListOrderResponse getOrdersByStatus(String username, OrderStatus status) {
        List<Order> orders = orderRepository.findAllByCustomerUsernameAndStatus(username, status)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy đơn hàng!"));

        String message = messageByOrderStatus(status);

        return ListOrderResponse.listOrderResponse(orders, message, "OK");
    }


    @Override
    public OrderResponse getOrderDetail(String username, UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy đơn hàng!"));
        if (!order.getCustomer().getUsername().equals(username)) {
            throw new BadRequestException("Không tìm thấy đơn hàng!");
        }

        ShippingDTO shippingDTO = shippingService.getAndCalculateShippingDTO(order.getAddress().getWard().getWardCode(),
                order.getShop().getWard().getWardCode(), order.getShippingMethod(), order.getCreateAt());
        Transport transport = transportService.getTransportByOrderId(orderId);

        return OrderResponse.orderResponse(order, transport, shippingDTO, "Lấy chi tiết đơn hàng thành công.", "OK");

    }


    @Override
    @Transactional
    public OrderResponse completeOrderById(String username, UUID orderId) {
        Order order = orderRepository.findByOrderIdAndCustomerUsername(orderId, username)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng theo mã đơn hàng của tài khoản!"));

        if (order.getStatus().equals(OrderStatus.DELIVERED)) {
            order.setStatus(OrderStatus.COMPLETED);
            order.setUpdateAt(LocalDateTime.now());
            try {

                if (!order.getPaymentMethod().equals("COD")) {
                    handlePayment(order, orderId, "COMPLETED_ORDER");
                } else if (cashOrderService.checkCashOrderByOrderIdWithHandlePaymentAndStatus(orderId, false, true, false, Status.ACTIVE)) {
                    handlePayment(order, orderId, "COMPLETED_ORDER_COD");
                }


                orderRepository.save(order);

                String messageEmail = "Xác nhận đã nhận hàng thành công.";
                String messageResponse = "Xác nhận đã nhận hàng thành công.";
                String titleNotification = "Bạn có đơn hàng thành công.";
                String bodyNotification = "Bạn có đơn hàng thành công " + order.getCustomer().getUsername() + " với mã đơn hàng #" + order.getOrderId() + " đã giao thành công.";

                return handleAfterSaveOrder(order, messageEmail, messageResponse, titleNotification, bodyNotification, TransportStatus.COMPLETED);

            } catch (Exception e) {
                throw new InternalServerErrorException("Hoàn thành đơn hàng thất bại!");
            }
        }

        throw new BadRequestException("Không thể hoàn thành đơn hàng! Chỉ có thể hoàn thành đơn hàng đang giao hàng hoặc đã giao hàng.");
    }

    @Override
    @Transactional
    public OrderResponse cancelOrderById(String username, UUID orderId) {
        Order order = orderRepository.findByOrderIdAndCustomerUsername(orderId, username)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng theo mã đơn hàng của tài khoản!"));

        if (order.getStatus().equals(OrderStatus.PENDING) || order.getStatus().equals(OrderStatus.UNPAID)) {
            return cancelOrder(order);
        }
        if (order.getStatus().equals(OrderStatus.PROCESSING)) {
            return waitingOrder(order);
        }

        throw new BadRequestException("Không thể hủy đơn hàng! Chỉ có thể hủy đơn hàng đang chờ xử lý hoặc yêu cầu hủy đơn hàng đang xử lý.");
    }




    @Override
    @Transactional
    public OrderResponse returnOrderById(String username, UUID orderId, String reason) {
        Order order = getOrderByOrderIdAndUsername(orderId, username);
        checkOrderStatusBeforeReturn(order);

        order.setNote(order.getNote() + " - #Lý do trả hàng: " + reason);
        order.setStatus(OrderStatus.RETURNED);
        order.setUpdateAt(LocalDateTime.now());
        try {
            orderRepository.save(order);

            String messageEmail = "Yêu cầu trả hàng thành công.";
            String messageResponse = "Yêu cầu trả hàng thành công.";
            String titleNotification = "Có yêu cầu trả hàng.";
            String bodyNotification = "Bạn có yêu cầu trả hàng với mã đơn hàng #" + order.getOrderId();

            return handleAfterSaveOrder(order, messageEmail, messageResponse, titleNotification, bodyNotification, TransportStatus.RETURNED);
        } catch (Exception e) {
            throw new InternalServerErrorException("Yêu cầu trả hàng thất bại!");
        }

    }


    @Override
    public String messageByOrderStatus(OrderStatus status) {

        return switch (status) {
            case PENDING -> "Lấy danh sách đơn hàng đang chờ xử lý thành công.";
            case PROCESSING -> "Lấy danh sách đơn hàng đang xử lý thành công.";
            case SHIPPING -> "Lấy danh sách đơn hàng đang giao thành công.";
            case COMPLETED -> "Lấy danh sách đơn hàng đã giao thành công.";
            case CANCEL -> "Lấy danh sách đơn hàng đã hủy thành công.";
            case WAITING -> "Lấy danh sách đơn hàng yêu cầu xử lý thành công.";
            default -> "Lấy danh sách đơn hàng thành công.";
        };
    }


    @Override
    public Long getTotalPaymentByOrderId(UUID orderId, String username) {
        Order order = orderRepository.findByOrderIdAndCustomerUsername(orderId, username)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng theo mã đơn hàng của tài khoản!"));
        if (!order.getStatus().equals(OrderStatus.UNPAID)) {
            throw new BadRequestException("Không thể thanh toán đơn hàng #" + orderId + "! Chỉ có thể thanh toán đơn hàng chưa thanh toán.");
        }

        return order.getPaymentTotal();
    }


    private OrderResponse handleAfterCreateOrder(Order order, String shippingMethod, String addressWardCode, String shopWardCode, String message) {
        ShippingDTO shippingDTO = shippingService.getCalculateShippingByWardAndTransportProvider(addressWardCode, shopWardCode, shippingMethod).getShippingDTO();
        Long totalPoint = loyaltyPointService.getLoyaltyPointByUsername(order.getCustomer().getUsername()).getTotalPoint();
        Long balance = walletService.getBalanceByUsername(order.getCustomer().getUsername());
        return OrderResponse.orderResponse(balance, totalPoint, order, shippingDTO, message, "OK");
    }


    public OrderResponse handleAfterSaveOrder(Order order, String messageEmail, String messageResponse,
                                              String titleNotification, String bodyNotification, TransportStatus status) {
        try {
            transportHandleService.addNewTransportHandleByOrderId(
                    order.getOrderId(), order.getAddress().getWard().getWardCode(), order.getCustomer().getUsername(), true, status);

            ShippingDTO shippingDTO = shippingService.getCalculateShippingByWardAndTransportProvider(order.getAddress().getWard().getWardCode(),
                    order.getShop().getWard().getWardCode(), order.getShippingMethod()).getShippingDTO();

            Transport transport = transportService.getTransportByOrderId(order.getOrderId());

            mailService.sendOrderConfirmationEmail(order, shippingDTO, messageEmail);

            notificationService.addNewNotification(
                    titleNotification,
                    bodyNotification,
                    order.getShop().getCustomer().getUsername(),
                    "system",
                    "ORDER"
            );

            return OrderResponse.orderResponse(order, transport, shippingDTO, messageResponse, "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Xử lý đơn hàng thất bại! " + e.getMessage());
        }
    }

    @Transactional
    public OrderResponse cancelOrder(Order order) {

        if ((order.getPaymentMethod().equals("VNPay") || order.getPaymentMethod().equals("Wallet")) && !order.getStatus().equals(OrderStatus.UNPAID)) {
            walletService.updateWalletByUsername(order.getCustomer().getUsername(), order.getOrderId(), order.getPaymentTotal(), "REFUND");
        }

        order.setStatus(OrderStatus.CANCEL);
        order.setUpdateAt(LocalDateTime.now());
        try {
            orderRepository.save(order);

            if (order.getVoucherOrders() != null) {
                for (VoucherOrder voucherOrder : order.getVoucherOrders()) {
                    voucherOrderService.cancelVoucherOrder(voucherOrder.getVoucherOrderId());
                }
            }
            if (order.getLoyaltyPointHistory() != null) {
                Long point = -order.getLoyaltyPointHistory().getPoint();
                loyaltyPointService.updatePointInLoyaltyPointByUsername(order.getCustomer().getUsername(), point, "REFUND");
            }
            List<OrderItem> orderItems = orderItemService.cancelOrderItem(order);
            order.setOrderItems(orderItems);

            String messageEmail = "Hủy đơn hàng thành công.";
            String messageResponse = "Hủy đơn hàng thành công.";
            String titleNotification = "Có đơn hàng bị hủy";
            String bodyNotification = "Đơn hàng đã bị hủy với mã đơn hàng #" + order.getOrderId();

            return handleAfterSaveOrder(order, messageEmail, messageResponse, titleNotification, bodyNotification, TransportStatus.CANCEL);
        } catch (Exception e) {
            throw new InternalServerErrorException("Hủy đơn hàng thất bại!");
        }
    }


    @Transactional
    public OrderResponse waitingOrder(Order order) {
        order.setStatus(OrderStatus.WAITING);
        order.setUpdateAt(LocalDateTime.now());
        try {
            orderRepository.save(order);

            String messageEmail = "Yêu cầu hủy đơn hàng.";
            String messageResponse = "Gửi yêu cầu hủy đơn hàng thành công.";
            String titleNotification = "Có yêu cầu hủy đơn hàng.";
            String bodyNotification = "Bạn có đơn hàng yêu cầu hủy đơn hàng với mã đơn hàng #" + order.getOrderId();

            return handleAfterSaveOrder(order, messageEmail, messageResponse, titleNotification, bodyNotification, TransportStatus.WAITING);
        } catch (Exception e) {
            throw new InternalServerErrorException("Yêu cầu hủy đơn hàng thất bại!");
        }
    }



    @Override
    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng theo mã đơn hàng!"));
    }


    private Order createOrderByOrderRequestWithCartIds(OrderRequestWithCart request, String username, Address address) {
        List<OrderItem> orderItems = orderItemService.createOrderItemsByCartIds(username, request.getCartIds());
        Order order = createBaseOrder(username, address);
        updateCreateOrderByOrderItemsPaymentMethod(order, orderItems, request.getPaymentMethod());
        if (request.isUseLoyaltyPoint()) {
            updateLoyaltyPointHistoryInCreateOrder(order);
        }
        if (!request.getNote().isEmpty()) {
            order.setNote(request.getNote());
        }
        if (request.getPaymentMethod().equals("Wallet")) {
            walletService.checkBalanceByUsernameAndMoney(username, order.getPaymentTotal());
        }
        updateCreateOrderByVoucherOrder(order, request.getShopVoucherCode(), request.getSystemVoucherCode());
        updateShippingInCreateOrder(order, request.getShippingMethod());

        return order;
    }


    private Order createOrderByOrderRequestWithProductVariant(OrderRequestWithProductVariant request, String username, Address address) {
        Order order = createBaseOrder(username, address);
        List<OrderItem> orderItems = orderItemService.createOrderItemsByMapProductVariantIdsAndQuantities(order.getCustomer(),
                request.getProductVariantIdsAndQuantities());
        updateCreateOrderByOrderItemsPaymentMethod(order, orderItems, request.getPaymentMethod());
        if (request.isUseLoyaltyPoint()) {
            updateLoyaltyPointHistoryInCreateOrder(order);
        }
        if (!request.getNote().isEmpty()) {
            order.setNote(request.getNote());
        }
        if (request.getPaymentMethod().equals("Wallet")) {
            walletService.checkBalanceByUsernameAndMoney(username, order.getPaymentTotal());
        }
        updateCreateOrderByVoucherOrder(order, request.getShopVoucherCode(), request.getSystemVoucherCode());
        updateShippingInCreateOrder(order, request.getShippingMethod());

        return order;
    }


    private Order createAddNewOrderWithCart(OrderRequestWithCart request, String username) {
        Address address = addressService.checkAddress(request.getAddressId(), username);
        Order order = createBaseOrder(username, address);
        order.setPaymentMethod(request.getPaymentMethod());
        if (request.getPaymentMethod().equals("VNPay")) {
            order.setStatus(OrderStatus.UNPAID);
        } else {
            order.setStatus(OrderStatus.PENDING);
            if (request.getPaymentMethod().equals("Wallet")) {
                walletService.checkBalanceByUsernameAndMoney(username, order.getPaymentTotal());
            }
        }
        if (!request.getNote().isEmpty()) {
            order.setNote(request.getNote());
        }
        try {
            return orderRepository.save(order);
        } catch (Exception e) {
            throw new InternalServerErrorException("Tạo mẫu đơn hàng mới thất bại! " + e.getMessage());
        }
    }


    private Order createBaseOrder(String username, Address address) {
        Order order = new Order();
        order.setCustomer(customerService.getCustomerByUsername(username));
        order.setAddress(address);
        order.setDiscountShop(0L);
        order.setDiscountSystem(0L);
        order.setPaymentMethod("COD");
        order.setPaymentTotal(0L);
        order.setNote(null);
        order.setCreateAt(LocalDateTime.now());
        order.setUpdateAt(LocalDateTime.now());
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.WAITING);

        return order;
    }


    private void updateCreateOrderByOrderItemsPaymentMethod(Order order, List<OrderItem> orderItems, String paymentMethod) {
        order.setShop(orderItems.get(0).getCart().getProductVariant().getProduct().getShop());
        order.setOrderItems(orderItems);
        order.setTotalPrice(getTotalPrice(orderItems));
        order.setVoucherOrders(null);
        order.setPaymentMethod(paymentMethod);
        order.setShop(orderItems.get(0).getCart().getProductVariant().getProduct().getShop());
        order.setOrderDate(new Date());
        order.setCount(getTotalCount(orderItems));
    }


    private void updateCreateOrderByVoucherOrder(Order order, String shopVoucherCode, String systemVoucherCode) {
        List<VoucherOrder> voucherOrders = new ArrayList<>();
        VoucherOrder voucherOrder;
        if (shopVoucherCode != null) {
            voucherOrder = voucherOrderService.createVoucherOrder(shopVoucherCode, order.getShop().getShopId());
            voucherOrders.add(voucherOrder);
            Long discount = voucherCustomerService
                    .discountVoucherByShopVoucherCodeAndShopIdAndTotalPrice(
                            shopVoucherCode,
                            order.getShop().getShopId(),
                            order.getTotalPrice());
            if (discount > order.getTotalPrice()) {
                discount = order.getTotalPrice() - order.getTotalPrice() * 96 / 100;
            }
            order.setDiscountShop(-discount);
        }

        if (systemVoucherCode != null) {
            voucherOrder = voucherOrderService.createVoucherOrder(systemVoucherCode, null);
            voucherOrders.add(voucherOrder);
            Long discount = voucherCustomerService
                    .discountVoucherBySystemVoucherCodeAndTotalPrice(
                            systemVoucherCode,
                            order.getTotalPrice());
            order.setDiscountSystem(-discount);
        }
        order.setVoucherOrders(voucherOrders);
    }


    private void updateLoyaltyPointHistoryInCreateOrder(Order order) {
        LoyaltyPoint loyaltyPoint = loyaltyPointService.getLoyaltyPointByUsername(order.getCustomer().getUsername());

        Long point = order.getTotalPrice() <= loyaltyPoint.getTotalPoint() ? order.getTotalPrice() : loyaltyPoint.getTotalPoint();
        LoyaltyPointHistory loyaltyPointHistory = new LoyaltyPointHistory();
        loyaltyPointHistory.setPoint(-point);
        loyaltyPointHistory.setLoyaltyPoint(loyaltyPoint);

        order.setLoyaltyPointHistory(loyaltyPointHistory);
    }


    private void updateShippingInCreateOrder(Order order, String shippingMethod) {
        Long shippingFee = feeShippingService.calculateShippingCost(shippingMethod,
                order.getAddress().getWard().getWardCode(), order.getShop().getWard().getWardCode());
        Long loyaltyPoint = order.getLoyaltyPointHistory() != null ? order.getLoyaltyPointHistory().getPoint() : 0;
        order.setShippingMethod(shippingMethod);
        order.setShippingFee(shippingFee);
        order.setPaymentTotal(order.getTotalPrice() + shippingFee + order.getDiscountShop() + order.getDiscountSystem() + loyaltyPoint);
        if (order.getPaymentTotal() < 0) {
            order.setPaymentTotal(0L);
        }
    }


    private void addOrderItemsToOrder(Order order, List<UUID> cartIds, String username) {
        List<OrderItem> orderItems = orderItemService.addNewOrderItemsByCartIds(order, cartIds, username);
        order.setShop(orderItems.get(0).getCart().getProductVariant().getProduct().getShop());
        order.setOrderItems(orderItems);
        order.setTotalPrice(getTotalPrice(orderItems));
        order.setCount(getTotalCount(orderItems));
    }


    private void addOrderItemsToOrderByMapProductVariant(Order order, Map<Long, Integer> productVariantsAndQuantities, String username) {
        List<OrderItem> orderItems = orderItemService.addNewOrderItemsByyMapProductVariant(order, productVariantsAndQuantities, username);
        order.setShop(orderItems.get(0).getCart().getProductVariant().getProduct().getShop());
        order.setOrderItems(orderItems);
        order.setTotalPrice(getTotalPrice(orderItems));
        order.setCount(getTotalCount(orderItems));
    }


    private void addVoucherOrderToOrder(Order order, String shopVoucherCode, String systemVoucherCode) {
        List<VoucherOrder> voucherOrders = new ArrayList<>();
        VoucherOrder voucherOrder;
        if (shopVoucherCode != null) {
            voucherOrder = voucherOrderService.addNewVoucherOrderByCode(shopVoucherCode, order, order.getShop().getShopId());
            voucherOrders.add(voucherOrder);
            Long discount = voucherCustomerService
                    .discountVoucherByShopVoucherCodeAndShopIdAndTotalPrice(
                            shopVoucherCode,
                            order.getShop().getShopId(),
                            order.getTotalPrice());
            if (discount > order.getTotalPrice()) {
                discount = order.getTotalPrice() - order.getTotalPrice() * 96 / 100;
            }
            order.setDiscountShop(-discount);
        }

        if (systemVoucherCode != null) {
            voucherOrder = voucherOrderService.addNewVoucherOrderByCode(systemVoucherCode, order, null);
            voucherOrders.add(voucherOrder);
            Long discount = voucherCustomerService
                    .discountVoucherBySystemVoucherCodeAndTotalPrice(
                            systemVoucherCode,
                            order.getTotalPrice());
            order.setDiscountSystem(-discount);
        }
        order.setVoucherOrders(voucherOrders);
    }


    private void addLoyaltyPointHistoryToOrder(Order order) {
        LoyaltyPoint loyaltyPoint = loyaltyPointService.getLoyaltyPointByUsername(order.getCustomer().getUsername());
        if (loyaltyPoint.getTotalPoint() == 0) {
            throw new BadRequestException("Không đủ điểm thưởng để thanh toán. Điểm thưởng hiện tại của bạn là 0.");
        }
        Long point = -(order.getTotalPrice() <= loyaltyPoint.getTotalPoint() ? order.getTotalPrice() : loyaltyPoint.getTotalPoint());
        LoyaltyPointHistory loyaltyPointHistory = loyaltyPointService.updatePointInLoyaltyPointByUsername(order.getCustomer().getUsername(), point, "PAYMENT");

        order.setLoyaltyPointHistory(loyaltyPointHistory);
        order.setPaymentTotal(order.getTotalPrice() - loyaltyPointHistory.getPoint());
    }




    private Long getTotalPrice(List<OrderItem> orderItems) {
        long totalPrice = 0L;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getCart().getProductVariant().getPrice() * orderItem.getCart().getQuantity();
        }

        return totalPrice;
    }


    private int getTotalCount(List<OrderItem> orderItems) {
        int count = 0;
        for (OrderItem orderItem : orderItems) {
            count += orderItem.getCart().getQuantity();
        }
        return count;
    }


    private Long getShopIdOfProductVariantId(Long productVariantId) {
        return productVariantService.getProductVariantById(productVariantId).getProduct().getShop().getShopId();
    }


    private void handlePayment(Order order, UUID orderId, String paymentType) {
        cashOrderService.updateCashOrderByOrderIdWithHandlePayment(orderId);
        walletService.updateWalletByUsername(
                order.getShop().getCustomer().getUsername(),
                order.getOrderId(),
                order.getTotalPrice() * 96 / 100 + order.getDiscountShop(),
                paymentType);
    }


    private Order getOrderByOrderIdAndUsername(UUID orderId, String username) {
        checkExistOrderByOrderIdAndUsername(orderId, username);
        return orderRepository.findByOrderIdAndCustomerUsername(orderId, username)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng theo mã đơn hàng của tài khoản!"));
    }


    private void checkExistOrderByOrderIdAndUsername(UUID orderId, String username) {
        if (!orderRepository.existsByOrderIdAndCustomerUsername(orderId, username)) {
            throw new BadRequestException("Mã đơn hàng không tồn tại hoặc không thuộc tài khoản của bạn!");
        }
    }

    private void checkOrderStatusBeforeReturn(Order order) {
        if (!order.getStatus().equals(OrderStatus.DELIVERED)) {
            throw new BadRequestException("Không thể yêu cầu trả hàng! Chỉ có thể yêu cầu trả hàng đơn hàng đã giao hàng.");
        }
    }


}
