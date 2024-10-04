package hcmute.kltn.vtv.service.guest.impl;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.guest.ListVoucherResponse;
import hcmute.kltn.vtv.model.data.guest.VoucherResponse;
import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.VoucherType;
import hcmute.kltn.vtv.repository.vtv.VoucherRepository;
import hcmute.kltn.vtv.service.guest.IVoucherService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements IVoucherService {

    private final VoucherRepository voucherRepository;



    @Override
    public VoucherResponse getVoucherByVoucherId(Long voucherId) {
        return VoucherResponse.voucherResponse(getVoucherById(voucherId), "Lấy mã giảm giá thành công.", "OK");
    }


    @Override
    public ListVoucherResponse listVoucherByShopId(Long shopId) {
        Date date = Date.from(new Date().toInstant());

        List<Voucher> vouchers = voucherRepository
                .findAllByShopShopIdAndStatusAndStartDateBeforeAndEndDateAfter(
                        shopId, Status.ACTIVE, date, date)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy mã giảm giá trên cửa hàng!"));
        return ListVoucherResponse.listVoucherResponse(vouchers, "Lấy danh sách mã giảm giá trong cửa hàng thành công.");
    }


    @Override
    public ListVoucherResponse listVoucherByType(VoucherType type) {
        List<Voucher> vouchers = voucherRepository.findAllByStatusAndType(Status.ACTIVE, type)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy mã giảm giá!"));

        return ListVoucherResponse.listVoucherResponse(vouchers, "Lấy danh sách mã giảm giá theo loại thành công.");
    }


    @Override
    public ListVoucherResponse listVoucherSystem() {
        Date date = Date.from(new Date().toInstant());
        List<Voucher> vouchers = voucherRepository.findAllByShopNullAndStatusAndStartDateBeforeAndEndDateAfter(
                Status.ACTIVE, date, date)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy mã giảm giá!"));

        return ListVoucherResponse.listVoucherResponse(vouchers, "Lấy danh sách mã giảm giá cửa hệ thống thành công.");
    }


    @Override
    public ListVoucherResponse allVoucher() {
        List<Voucher> vouchers = voucherRepository.findAllByStatus(Status.ACTIVE)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy mã giảm giá!"));

        return ListVoucherResponse.listVoucherResponse(vouchers, "Lấy danh sách tất cả khã dụng mã giảm giá thành công.");
    }



    @Override
    public void checkQuantityVoucher(String voucherCode, Integer quantity) {
        Voucher voucher = voucherRepository.findByCodeAndStatus(voucherCode, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy mã giảm giá có mã " + voucherCode + "!"));

        // check date
        Date date = Date.from(new Date().toInstant());
        if (voucher.getStartDate().after(date) || voucher.getEndDate().before(date)) {
            throw new BadRequestException("Mã giảm giá đã hết hạn!");
        }

        if (voucher.getQuantity() < quantity) {
            throw new BadRequestException("Số lượng mã giảm giá không đủ! Số lượng còn lại: " + voucher.getQuantity());
        }

    }



    @Override
    public Voucher getVoucherById(Long voucherId) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy mã giảm giá!"));
        if (voucher.getStatus().equals(Status.DELETED)) {
            throw new BadRequestException("Mã giảm giá đã bị xóa!");
        }
        return voucher;

    }

}
