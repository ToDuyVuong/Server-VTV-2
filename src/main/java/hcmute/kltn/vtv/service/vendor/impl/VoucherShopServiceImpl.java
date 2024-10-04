package hcmute.kltn.vtv.service.vendor.impl;

import hcmute.kltn.vtv.service.vendor.IShopService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.vendor.request.VoucherShopRequest;
import hcmute.kltn.vtv.model.data.vendor.response.ListVoucherShopResponse;
import hcmute.kltn.vtv.model.data.vendor.response.VoucherShopResponse;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.VoucherType;
import hcmute.kltn.vtv.repository.vtv.ShopRepository;
import hcmute.kltn.vtv.repository.vtv.VoucherRepository;
import hcmute.kltn.vtv.service.vendor.IVoucherShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherShopServiceImpl implements IVoucherShopService {

    private final VoucherRepository voucherRepository;
    private final IShopService shopService;
    private final ShopRepository shopRepository;

    @Override
    @Transactional
    public VoucherShopResponse addNewVoucher(VoucherShopRequest request, String username) {
        Shop shop = checkVoucherOnShop(request.getCode(), username);
        Voucher voucher = VoucherShopRequest.convertCreateToVoucher(request);
        voucher.setShop(shop);

        try {
            voucherRepository.save(voucher);

            return VoucherShopResponse.voucherShopResponse(voucher, "Thêm mới mã giảm giá thành công.", "success");
        } catch (Exception e) {
            throw new BadRequestException("Thêm mới mã giảm giá thất bại!");
        }

    }

    @Override
    public VoucherShopResponse getVoucherByVoucherId(Long voucherId, String username) {
        Voucher voucher = getVoucherByVoucherIdAndUsername(voucherId, username);

        return VoucherShopResponse.voucherShopResponse(voucher, "Lấy mã giảm giá thành công.", "ok");
    }

    @Override
    public ListVoucherShopResponse getListVoucherByUsername(String username) {
        Shop shop = shopService.getShopByUsername(username);

        List<Voucher> vouchers = voucherRepository.findAllByShopAndStatusNot(shop, Status.DELETED)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy mã giảm giá!"));

        return ListVoucherShopResponse.listVoucherShopResponse(vouchers, "Lấy danh sách mã giảm giá thành công.", shop.getShopId(),
                shop.getName());
    }

    @Override
    public ListVoucherShopResponse getListVoucherByUsernameAndStatus(String username, Status status) {
        Shop shop = shopService.getShopByUsername(username);

        List<Voucher> vouchers = voucherRepository.findAllByShopAndStatus(shop, status)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy mã giảm giá!"));

        return ListVoucherShopResponse.listVoucherShopResponse(vouchers, "Lấy danh sách mã giảm giá theo trạng thái thành công.",
                shop.getShopId(), shop.getName());
    }

    @Override
    public ListVoucherShopResponse getListVoucherByUsernameAndVoucherType(String username, VoucherType type) {
        Shop shop = shopService.getShopByUsername(username);

        List<Voucher> vouchers = voucherRepository.findAllByShopAndStatusNotAndType(shop, Status.DELETED, type)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy mã giảm giá!"));

        return ListVoucherShopResponse.listVoucherShopResponse(vouchers, "Lấy danh sách mã giảm giá theo loại thành công.", shop.getShopId(),
                shop.getName());
    }

    @Override
    @Transactional
    public VoucherShopResponse updateVoucherShopByVoucherShopRequest(Long voucherId, VoucherShopRequest request, String username) {
        Voucher voucher = getVoucherByVoucherIdAndUsername(voucherId, username);
        checkVoucherBeforeUpdate(voucher, request);
        convertUpdateVoucherShopByRequest(voucher, request);

        try {
            voucherRepository.save(voucher);

            return VoucherShopResponse.voucherShopResponse(voucher, "Cập nhật mã giảm giá thành công.", "success");
        } catch (Exception e) {
            throw new BadRequestException("Cập nhật mã giảm giá thất bại!");
        }
    }

    @Override
    @Transactional
    public VoucherShopResponse updateStatusVoucher(Long voucherId, Status status, String username) {
        Voucher voucher = getVoucherByVoucherIdAndUsername(voucherId, username);

        if (status != Status.ACTIVE && status != Status.INACTIVE && status != Status.DELETED
                && status != Status.CANCEL) {
            throw new BadRequestException("Trạng thái không hợp lệ!");
        }

        if (voucher.getStatus() == Status.DELETED) {
            throw new BadRequestException("Mã giảm giá đã bị xóa!");
        }
        voucher.setStatus(status);

        try {
            voucherRepository.save(voucher);

            return VoucherShopResponse.voucherShopResponse(voucher, "Cập nhật trạng thái mã giảm giá thành công.", "success");
        } catch (Exception e) {
            throw new BadRequestException("Cập nhật trạng thái mã giảm giá thất bại!");
        }
    }

    @Override
    public Voucher checkVoucherShop(Long voucherId, Long shopId) {
        Voucher voucher = getVoucherByVoucherIdAndShopId(voucherId, shopId);
        Instant now = Instant.now(); // Sử dụng Instant thay vì Date
        Instant voucherStartDate = voucher.getStartDate().toInstant(); // Chuyển startDate của voucher thành Instant
        Instant voucherEndDate = voucher.getEndDate().toInstant();
        if (voucherStartDate.isAfter(now)) { // So sánh theo Instant
            throw new BadRequestException("Mã giảm giá chưa có hiệu lực!");
        }
        if (voucherEndDate.isBefore(now)) {
            throw new BadRequestException("Mã giảm giá đã hết hạn!");
        }
        if (voucher.getStatus() == Status.DELETED) {
            throw new BadRequestException("Mã giảm giá đã bị xóa!");
        }
        if (voucher.getQuantityUsed() >= voucher.getQuantity()) {
            throw new BadRequestException("Mã giảm giá đã hết lượt sử dụng!");
        }
        return voucher;
    }

    private boolean existVoucherCodeOnShop(String code, Long shopId) {
        return voucherRepository.existsByCodeAndShopShopId(code, shopId);
    }

    private Shop checkVoucherOnShop(String code, String username) {
        Shop shop = shopService.getShopByUsername(username);
        if (voucherRepository.existsByCodeAndShopShopId(code, shop.getShopId())) {
            throw new BadRequestException("Mã giảm giá đã tồn tại trên cửa hàng này!");
        }

        return shop;
    }

    private Voucher getVoucherByCodeAndUsername(String code, String username) {
        Shop shop = shopService.getShopByUsername(username);
        Voucher voucher = voucherRepository.findByCodeAndShopShopId(code, shop.getShopId())
                .orElseThrow(() -> new BadRequestException("Mã giảm giá không tồn tại!"));
        if (voucher == null) {
            throw new BadRequestException("Mã giảm giá không tồn tại!");
        }
        if (voucher.getStatus() == Status.DELETED) {
            throw new BadRequestException("Mã giảm giá đã bị xóa!");
        }

        return voucher;
    }

    private Voucher getVoucherByVoucherIdAndShopId(Long voucherId, Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new BadRequestException("Cửa hàng không tồn tại!"));
        Voucher voucher = voucherRepository.findByVoucherIdAndShopShopId(voucherId, shop.getShopId())
                .orElseThrow(() -> new BadRequestException("Mã giảm giá không tồn tại!"));
        if (voucher == null) {
            throw new BadRequestException("Mã giảm giá không tồn tại!");
        }
        if (voucher.getStatus() == Status.DELETED) {
            throw new BadRequestException("Mã giảm giá đã bị xóa!");
        }

        return voucher;
    }

    private Voucher getVoucherByVoucherIdAndUsername(Long voucherId, String username) {
        Shop shop = shopService.getShopByUsername(username);
        Voucher voucher = voucherRepository.findByVoucherIdAndShopShopId(voucherId, shop.getShopId())
                .orElseThrow(() -> new BadRequestException("Không tìm thấy mã giảm giá!"));
        if (voucher == null) {
            throw new BadRequestException("Mã giảm giá không tồn tại!");
        }

        return voucher;
    }


    private void checkVoucherBeforeUpdate(Voucher voucher, VoucherShopRequest request) {
        if (!voucher.getCode().equals(request.getCode())
                && existVoucherCodeOnShop(request.getCode(), voucher.getShop().getShopId())) {

            throw new BadRequestException("Mã code giảm giá đã tồn tại trên cửa hàng này! Vui lòng chọn mã khác!");
        }
        if (voucher.getQuantityUsed() > 0) {
            throw new BadRequestException("Mã giảm giá đã được sử dụng!");
        }
    }


    private void convertUpdateVoucherShopByRequest(Voucher voucher, VoucherShopRequest request){
        voucher.setName(request.getName());
        voucher.setCode(request.getCode());
        voucher.setDescription(request.getDescription());
        voucher.setDiscount(request.getDiscount());
        voucher.setQuantity(request.getQuantity());
        voucher.setStartDate(request.getStartDate());
        voucher.setEndDate(request.getEndDate());
        voucher.setUpdateAt(LocalDateTime.now());
        voucher.setType(VoucherShopRequest.convertType(request.getType()));
    }




}
