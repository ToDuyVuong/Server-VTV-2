package hcmute.kltn.vtv.model.dto.vtv;

import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.VoucherType;
import lombok.*;

import java.util.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VoucherDTO {

    private Long voucherId;

    private Status status;

    private String code;

    private String name;

    private String description;

    private int discount;

    private int quantity;

    private Date startDate;

    private Date endDate;

    private int quantityUsed;

    private VoucherType type;

    public static VoucherDTO convertEntityToDTO(Voucher voucher) {
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setVoucherId(voucher.getVoucherId());
        voucherDTO.setStatus(voucher.getStatus());
        voucherDTO.setCode(voucher.getCode());
        voucherDTO.setName(voucher.getName());
        voucherDTO.setDescription(voucher.getDescription());
        voucherDTO.setDiscount(voucher.getDiscount());
        voucherDTO.setQuantity(voucher.getQuantity());
        voucherDTO.setStartDate(voucher.getStartDate());
        voucherDTO.setEndDate(voucher.getEndDate());
        voucherDTO.setQuantityUsed(voucher.getQuantityUsed());
        voucherDTO.setType(voucher.getType());




        return voucherDTO;
    }

    public static List<VoucherDTO> convertEntitiesToDTOs(List<Voucher> vouchers) {
        List<VoucherDTO> voucherDTOs = new ArrayList<>();
        for (Voucher voucher : vouchers) {
            voucherDTOs.add(convertEntityToDTO(voucher));
        }

        if (voucherDTOs.size() > 1){
            voucherDTOs.sort(Comparator
                    .comparing(VoucherDTO::getType)
                    .thenComparing(VoucherDTO::getStartDate)
                    .thenComparing(VoucherDTO::getEndDate)
                    .reversed());
        }

        return voucherDTOs;
    }

    public String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }

}
