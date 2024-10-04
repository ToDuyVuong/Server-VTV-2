package hcmute.kltn.vtv.model.data.manager.request;

import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.VoucherType;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@ToString
@RequiredArgsConstructor
public class VoucherSystemRequest {

    private String code;

    private String name;

    private String description;

    private int discount;

    private int quantity;

    private Date startDate;

    private Date endDate;

    private String type;


    public void validate() {
        if (this.code == null || this.code.isEmpty()) {
            throw new BadRequestException("Mã giảm giá không được để trống");
        }
        if (this.name == null || this.name.isEmpty()) {
            throw new BadRequestException("Tên giảm giá không được để trống");
        }
        if (this.description == null || this.description.isEmpty()) {
            throw new BadRequestException("Mô tả giảm giá không được để trống");
        }

        if (this.discount < 0) {
            throw new BadRequestException("Giá trị giảm giá không được nhỏ hơn 0%");
        }
        if (this.discount == 0) {
            throw new BadRequestException("Giá trị giảm giá không được bằng 0");
        }
        if (this.quantity <= 0) {
            throw new BadRequestException("Số lượng giảm giá không được nhỏ hơn hoặc bằng 0");
        }
        if (this.quantity > 1000) {
            throw new BadRequestException("Số lượng giảm giá không được lớn hơn 1000");
        }
        if (this.startDate == null) {
            throw new BadRequestException("Ngày bắt đầu không được để trống");
        }
        if (this.endDate == null) {
            throw new BadRequestException("Ngày kết thúc không được để trống");
        }

        if (this.startDate.after(this.endDate)) {
            throw new BadRequestException("Ngày bắt đầu không được sau ngày kết thúc");
        }

        // Ngày kết thúc không được ở trong quá khứ
        if (this.endDate.before(new Date())) {
            throw new BadRequestException("Ngày kết thúc không được ở trong quá khứ");
        }


        trim();

    }

    public void validateCreate() {
        validate();

        if (this.type == null || this.type.isEmpty()) {
            throw new BadRequestException("Loại giảm giá không được để trống");
        }
        if (!this.type.equals("percent".trim()) && !this.type.equals("money".trim())) {
            throw new BadRequestException(
                    "Loại giảm giá không hợp lệ. Loại giảm giá cửa chỉ có thể là percent, money");
        }
        if (this.type.equals("percent") && this.discount > 100) {
            throw new BadRequestException("Giá trị giảm giá không được lớn 100%");
        }
    }


    public static VoucherType convertType(String type) {
        if (type.equals("percent")) {
            return VoucherType.PERCENTAGE_SYSTEM;
        } else {
            return VoucherType.MONEY_SYSTEM;
        }
    }

    public void trim() {
        this.code = this.code.trim();
        this.name = this.name.trim();
        this.description = this.description.trim();
        this.type = this.type.trim();
    }

}
