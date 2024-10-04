package hcmute.kltn.vtv.model.data.manager.request;


import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@ToString
@RequiredArgsConstructor
public class FeeShippingRequest {

    private Long zeroArea;

    private int zeroEstimatedDeliveryTime;

    private Long oneArea;

    private int oneEstimatedDeliveryTime;

    private Long twoArea;

    private int twoEstimatedDeliveryTime;

    private Long threeArea;

    private int threeEstimatedDeliveryTime;

    private Long fourArea;

    private int fourEstimatedDeliveryTime;

    public void validate() {
        if (this.zeroArea == null || this.zeroArea <= 0) {
            throw new BadRequestException("Phí vận chuyển cho cùng xã/phường không hợp lệ");
        }
        if (this.oneArea == null || this.oneArea <= 0) {
            throw new BadRequestException("Phí vận chuyển cho cùng quận/huyện không hợp lệ");
        }
        if (this.twoArea == null || this.twoArea <= 0) {
            throw new BadRequestException("Phí vận chuyển cho cùng tỉnh/thành phố không hợp lệ");
        }
        if (this.threeArea == null || this.threeArea <= 0) {
            throw new BadRequestException("Phí vận chuyển cho cùng khu vực không hợp lệ");
        }
        if (this.fourArea == null || this.fourArea <= 0) {
            throw new BadRequestException("Phí vận chuyển cho khác khu vực không hợp lệ");
        }
        if (this.zeroArea >= this.oneArea) {
            throw new BadRequestException("Phí vận chuyển cùng xã/phường phải nhỏ hơn cùng quận/huyện");
        }
        if (this.oneArea >= this.twoArea) {
            throw new BadRequestException("Phí vận chuyển cùng quận/huyện phải nhỏ hơn cùng tỉnh/thành phố");
        }
        if (this.twoArea >= this.threeArea) {
            throw new BadRequestException("Phí vận chuyển cùng tỉnh/thành phố phải nhỏ hơn cùng khu vực");
        }
        if (this.threeArea >= this.fourArea) {
            throw new BadRequestException("Phí vận chuyển cùng khu vực phải nhỏ hơn khác khu vực");
        }

        if (this.zeroEstimatedDeliveryTime < 0 || this.oneEstimatedDeliveryTime < 0 || this.twoEstimatedDeliveryTime < 0 || this.threeEstimatedDeliveryTime < 0 || this.fourEstimatedDeliveryTime < 0) {
            throw new BadRequestException("Thời gian ước tính giao hàng không hợp lệ. Thời gian ước tính giao hàng phải lớn hơn hoặc bằng 0");
        }


        if (this.zeroEstimatedDeliveryTime >= this.oneEstimatedDeliveryTime) {
            throw new BadRequestException("Thời gian ước tính giao hàng cùng xã/phường phải nhỏ hơn cùng quận/huyện");
        }
        if (this.oneEstimatedDeliveryTime >= this.twoEstimatedDeliveryTime) {
            throw new BadRequestException("Thời gian ước tính giao hàng cùng quận/huyện phải nhỏ hơn cùng tỉnh/thành phố");
        }
        if (this.twoEstimatedDeliveryTime >= this.threeEstimatedDeliveryTime) {
            throw new BadRequestException("Thời gian ước tính giao hàng cùng tỉnh/thành phố phải nhỏ hơn cùng khu vực");
        }
        if (this.threeEstimatedDeliveryTime >= this.fourEstimatedDeliveryTime) {
            throw new BadRequestException("Thời gian ước tính giao hàng cùng khu vực phải nhỏ hơn khác khu vực");
        }



    }
}
