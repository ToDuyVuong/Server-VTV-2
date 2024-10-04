package hcmute.kltn.vtv.model.dto.shipping;

import hcmute.kltn.vtv.model.entity.shipping.TransportHandle;
import hcmute.kltn.vtv.model.extra.TransportStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransportHandleDTO {

    private UUID transportHandleId;

    private String username;

    // vi tri cua shipper
    private String wardCode;


    private boolean handled;

    private String messageStatus;

    private TransportStatus transportStatus;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;


    public static TransportHandleDTO convertEntityToDTO(TransportHandle transportHandle) {

        String message = "Đơn hàng " + messageStatusString(transportHandle.getTransportStatus(), transportHandle.isHandled()) + ".";
        TransportHandleDTO transportHandleDTO = new TransportHandleDTO();
        transportHandleDTO.setTransportHandleId(transportHandle.getTransportHandleId());
        transportHandleDTO.setMessageStatus(message);
        transportHandleDTO.setUsername(transportHandle.getUsername());
        transportHandleDTO.setWardCode(transportHandle.getWardCode());
        transportHandleDTO.setHandled(transportHandle.isHandled());
        transportHandleDTO.setTransportStatus(transportHandle.getTransportStatus());
        transportHandleDTO.setCreateAt(transportHandle.getCreateAt());
        transportHandleDTO.setUpdateAt(transportHandle.getUpdateAt());
        return transportHandleDTO;
    }


    public static List<TransportHandleDTO> convertEntitiesToDTOs(List<TransportHandle> transportHandles) {

        List<TransportHandleDTO> transportHandleDTOs = new ArrayList<>();
        for (TransportHandle transportHandle : transportHandles) {
            transportHandleDTOs.add(convertEntityToDTO(transportHandle));
        }
        transportHandleDTOs.sort(Comparator.comparing(TransportHandleDTO::getCreateAt).reversed());

        return transportHandleDTOs;
    }

//    private static final Map<TransportStatus, Map<Boolean, String>> STATUS_MESSAGES = new HashMap<>();
//
//    static {
//        STATUS_MESSAGES.put(TransportStatus.WAITING, Map.of(false, "đang chờ xử lý"));
//        STATUS_MESSAGES.put(TransportStatus.PICKED_UP, Map.of(false, "đang chở lấy hàng", true, "đã lấy hàng"));
//        STATUS_MESSAGES.put(TransportStatus.TRANSPORTING, Map.of(false, "đang vận chuyển", true, "đã vận chuyển"));
//        STATUS_MESSAGES.put(TransportStatus.DELIVERED, Map.of(false, "đang giao hàng", true, "đã giao hàng"));
//        STATUS_MESSAGES.put(TransportStatus.WAREHOUSE, Map.of(false, "đã đến kho", true, "đã rời kho"));
//        STATUS_MESSAGES.put(TransportStatus.IN_TRANSIT, Map.of(false, "đang trung chuyển", true, "đã trung chuyển"));
//        STATUS_MESSAGES.put(TransportStatus.CANCEL, Map.of(false, "đã hủy"));
//        STATUS_MESSAGES.put(TransportStatus.COMPLETED, Map.of(false, "đã nhận hàng"));
//        STATUS_MESSAGES.put(TransportStatus.RETURNED, Map.of(false, "đã trả hàng"));
//        STATUS_MESSAGES.put(TransportStatus.PROCESSING, Map.of(false, "đang xử lý"));
//    }
//
//
//    private static String messageStatusString(TransportStatus transportStatus, boolean handled) {
//        Map<Boolean, String> statusMap = STATUS_MESSAGES.getOrDefault(transportStatus, Map.of(false, "đang chờ xác nhận"));
//        return statusMap.getOrDefault(handled, "chờ xác nhận");
//    }


    public static String messageStatusString(TransportStatus transportStatus, boolean handled) {
        if (transportStatus == TransportStatus.WAITING) {
            return "đang chờ xác nhận";
        } else if (transportStatus == TransportStatus.PICKED_UP && !handled) {
            return "đã lấy hàng";
        } else if (transportStatus == TransportStatus.PICKUP_PENDING) {
            return "chờ lấy hàng";
        } else if (transportStatus == TransportStatus.PICKED_UP) {
            return "đã lấy hàng";
        } else if (transportStatus == TransportStatus.SHIPPING && !handled) {
            return "đang vận chuyển";
        } else if (transportStatus == TransportStatus.SHIPPING) {
            return "đang vận chuyển";
        } else if (transportStatus == TransportStatus.DELIVERED && !handled) {
            return "đã giao hàng";
        } else if (transportStatus == TransportStatus.DELIVERED) {
            return "đã giao hàng";
        } else if (transportStatus == TransportStatus.WAREHOUSE && !handled) {
            return "đã lưu kho";
        } else if (transportStatus == TransportStatus.WAREHOUSE) {
            return "đã lưu kho";
        } else if (transportStatus == TransportStatus.IN_TRANSIT && !handled) {
            return "đang trung chuyển";
        } else if (transportStatus == TransportStatus.IN_TRANSIT) {
            return "đang trung chuyển";
        } else if (transportStatus == TransportStatus.CANCEL) {
            return "đã hủy";
        } else if (transportStatus == TransportStatus.COMPLETED) {
            return "đã nhận hàng";
        } else if (transportStatus == TransportStatus.RETURNED) {
            return "trả hàng";
        } else if (transportStatus == TransportStatus.PROCESSING) {
            return "đang xử lý";
        } else {
            return "chờ xác nhận";
        }
    }
}
