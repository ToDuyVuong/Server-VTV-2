package hcmute.kltn.vtv.service.shipping.impl;

import hcmute.kltn.vtv.model.data.shipping.response.DeliverResponse;
import hcmute.kltn.vtv.model.entity.shipping.Deliver;
import hcmute.kltn.vtv.model.entity.shipping.TransportProvider;
import hcmute.kltn.vtv.model.extra.TransportStatus;
import hcmute.kltn.vtv.model.extra.TypeWork;
import hcmute.kltn.vtv.repository.shipping.DeliverRepository;
import hcmute.kltn.vtv.service.shipping.IDeliverService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeliverServiceImpl implements IDeliverService {

    private final DeliverRepository deliverRepository;


    @Override
    public DeliverResponse getDeliverResponseByUsername(String username) {
        Deliver deliver = getDeliverByUsername(username);
        return DeliverResponse.deliverResponse(deliver, "Lấy thông tin nhân viên giao hàng thành công!", "OK");
    }


    @Override
    public Deliver checkTypeWorkDeliverWithTransportStatus(String username, TransportStatus transportStatus) {
        Deliver deliver = getDeliverByUsername(username);
        if (!checkTypeWorkDeliverWithTransportStatus(deliver.getTypeWork(), transportStatus)) {
            throw new BadRequestException("Người giao hàng không thể thực hiện hành động này!");
        }
        return deliver;
    }


    @Override
    public Deliver getDeliverByUsername(String username) {
        checkExistByUsername(username);
        return deliverRepository.findByCustomerUsername(username)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người giao hàng có tên đăng nhập: " + username));
    }


    @Override
    public Deliver getDeliverByDeliverId(Long deliverId) {
        return deliverRepository.findByDeliverId(deliverId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người giao hàng có mã: " + deliverId));
    }

    @Override
    public Deliver getDeliverByDeliverIdAndTransportProvider(Long deliverId, TransportProvider transportProvider) {
        return deliverRepository.findByDeliverIdAndTransportProvider(deliverId, transportProvider)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người giao hàng có mã: " + deliverId));
    }


    @Override
    public void checkTypeWorkWithManager(Deliver deliver) {
        if (!deliver.getTypeWork().equals(TypeWork.PROVIDER) && !deliver.getTypeWork().equals(TypeWork.MANAGER)) {
            throw new NotFoundException("Không thể thực hiện thao tác này với nhà vận chuyển " + deliver.getTransportProvider().getShortName() + ".");
        }
    }


    @Override
    public void checkDeliverUseRole(String username, TypeWork typeWork) {
        Deliver deliver = getDeliverByUsername(username);
        TypeWork deliverTypeWork = deliver.getTypeWork();

        if (!isValidRole(deliverTypeWork, typeWork)) {
            throw new BadRequestException(getErrorMessage(typeWork));
        }
    }


    @Override
    public boolean checkBooleanDeliverUseRole(String username, TypeWork typeWork) {
        Deliver deliver = getDeliverByUsername(username);
        TypeWork deliverTypeWork = deliver.getTypeWork();

        return isValidRole(deliverTypeWork, typeWork);
    }


    private boolean isValidRole(TypeWork deliverTypeWork, TypeWork requiredTypeWork) {
        Map<TypeWork, List<TypeWork>> validRoles = Map.of(
                TypeWork.SHIPPER, List.of(TypeWork.SHIPPER, TypeWork.MANAGER, TypeWork.WAREHOUSE, TypeWork.PROVIDER),
                TypeWork.PICKUP, List.of(TypeWork.PICKUP, TypeWork.MANAGER, TypeWork.WAREHOUSE, TypeWork.PROVIDER),
                TypeWork.TRANSIT, List.of(TypeWork.TRANSIT, TypeWork.MANAGER, TypeWork.WAREHOUSE, TypeWork.PROVIDER),
                TypeWork.WAREHOUSE, List.of(TypeWork.WAREHOUSE, TypeWork.MANAGER, TypeWork.PROVIDER),
                TypeWork.MANAGER, List.of(TypeWork.MANAGER, TypeWork.PROVIDER),
                TypeWork.PROVIDER, List.of(TypeWork.PROVIDER)
        );

        System.out.println("deliverTypeWork: " + validRoles.getOrDefault(requiredTypeWork, List.of()).contains(deliverTypeWork));

        return validRoles.getOrDefault(requiredTypeWork, List.of()).contains(deliverTypeWork);
    }

    private String getErrorMessage(TypeWork typeWork) {
        return switch (typeWork) {
            case SHIPPER -> "Bạn không phải có quyền thực hiện hành động của nhân viên giao hàng!";
            case PICKUP -> "Bạn không phải có quyền thực hiện hành động của nhân viên lấy hàng!";
            case TRANSIT -> "Bạn không phải có quyền thực hiện hành động của nhân viên trung chuyển!";
            case WAREHOUSE -> "Bạn không phải có quyền thực hiện hành động của nhân kho!";
            case MANAGER ->
                    "Bạn không phải có quyền thực hiện hành động của nhân viên quản lý của nhà cung cấp dịch vụ giao hàng!";
            case PROVIDER -> "Bạn không phải có quyền thực hiện hành động của nhà cung cấp dịch vụ giao hàng!";
            default -> "Không tìm thấy quyền hành động!";
        };
    }


    @Override
    public void checkExistByTypeWorkShipperByUsername(String username) {

        Deliver deliver = getDeliverByUsername(username);
        if (!deliver.getTypeWork().equals(TypeWork.SHIPPER) && !deliver.getTypeWork().equals(TypeWork.MANAGER) &&
                !deliver.getTypeWork().equals(TypeWork.WAREHOUSE) && !deliver.getTypeWork().equals(TypeWork.PROVIDER)) {
            throw new BadRequestException("Bạn không phải có quyền thực hiện hành động của nhân viên giao hàng!");
        }

    }


    @Override
    public void checkExistByTypeWorkWarehouseByUsername(String username) {

        Deliver deliver = getDeliverByUsername(username);
        if (!deliver.getTypeWork().equals(TypeWork.MANAGER) &&
                !deliver.getTypeWork().equals(TypeWork.WAREHOUSE) && !deliver.getTypeWork().equals(TypeWork.PROVIDER)) {
            throw new BadRequestException("Bạn không phải có quyền thực hiện hành động của nhân kho!");
        }

    }


    private void checkExistByUsername(String username) {
        if (!deliverRepository.existsByCustomerUsername(username)) {
            throw new NotFoundException("Không tồn tại người giao hàng có tên đăng nhập: " + username);
        }
    }

    private boolean checkTypeWorkDeliverWithTransportStatus(TypeWork typeWork, TransportStatus transportStatus) {
        return switch (typeWork) {
            case SHIPPER -> transportStatus == TransportStatus.PICKED_UP ||
                    transportStatus == TransportStatus.SHIPPING || transportStatus == TransportStatus.IN_TRANSIT ||
                    transportStatus == TransportStatus.DELIVERED || transportStatus == TransportStatus.RETURNED;
            case MANAGER -> transportStatus == TransportStatus.PICKED_UP ||
                    transportStatus == TransportStatus.SHIPPING || transportStatus == TransportStatus.IN_TRANSIT ||
                    transportStatus == TransportStatus.WAREHOUSE || transportStatus == TransportStatus.DELIVERED ||
                    transportStatus == TransportStatus.COMPLETED || transportStatus == TransportStatus.RETURNED;
            case PROVIDER -> transportStatus == TransportStatus.PICKED_UP ||
                    transportStatus == TransportStatus.PROCESSING || transportStatus == TransportStatus.WAITING ||
                    transportStatus == TransportStatus.WAREHOUSE || transportStatus == TransportStatus.SHIPPING ||
                    transportStatus == TransportStatus.DELIVERED || transportStatus == TransportStatus.RETURNED;
            case WAREHOUSE -> transportStatus == TransportStatus.PICKED_UP ||
                    transportStatus == TransportStatus.WAREHOUSE || transportStatus == TransportStatus.DELIVERED ||
                    transportStatus == TransportStatus.RETURNED;
            case TRANSIT ->
                    transportStatus == TransportStatus.IN_TRANSIT || transportStatus == TransportStatus.RETURNED;
            case PICKUP ->
                    transportStatus == TransportStatus.PICKED_UP || transportStatus == TransportStatus.IN_TRANSIT ||
                            transportStatus == TransportStatus.SHIPPING || transportStatus == TransportStatus.RETURNED;
        };
    }

}
