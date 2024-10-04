package hcmute.kltn.vtv.controller.manager;

import hcmute.kltn.vtv.model.extra.Role;
import hcmute.kltn.vtv.service.vtv.IPageService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.manager.response.PageCustomerResponse;
import hcmute.kltn.vtv.model.data.user.response.ProfileCustomerResponse;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.service.manager.IManagerCustomerService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/customer")
@RequiredArgsConstructor
public class ManagerCustomerController {

    private final IManagerCustomerService managerCustomerService;
    private final IPageService pageService;

    @GetMapping("/page/status/{status}")
    public ResponseEntity<PageCustomerResponse> getPageCustomerByStatus(@RequestParam int page,
                                                                        @RequestParam int size,
                                                                        @PathVariable Status status) {
        pageService.validatePageNumberAndSize(page, size);

        return ResponseEntity.ok(managerCustomerService.getPageCustomerByStatus(size, page, status));
    }


    @GetMapping("/page/status/{status}/sort/{sort}")
    public ResponseEntity<PageCustomerResponse> getPageCustomerByStatusAndSort(@RequestParam int page,
                                                                               @RequestParam int size,
                                                                               @PathVariable Status status,
                                                                               @PathVariable String sort) {
        pageService.validatePageNumberAndSize(page, size);
        pageService.checkRequestSortCustomerParams(sort);
        if (!sort.equals("name-asc") && !sort.equals("name-desc") && !sort.equals("at-asc") && !sort.equals("at-desc")) {
            throw new BadRequestException("Sắp xếp không hợp lệ! Sắp xếp theo tên tăng dần: name-asc, tên giảm dần: name-desc, ngày tạo tăng dần: at-asc, ngày tạo giảm dần: at-desc");
        }

        return ResponseEntity.ok(managerCustomerService.getPageCustomerByStatusAndSort(size, page, status, sort));
    }


    @GetMapping("/page/search/{search}/status/{status}")
    public ResponseEntity<PageCustomerResponse> searchPageCustomerByFullNameAndStatus(@RequestParam int page,
                                                                                      @RequestParam int size,
                                                                                      @PathVariable Status status,
                                                                                      @PathVariable String search) {
        pageService.validatePageNumberAndSize(page, size);
        if (search == null) {
            throw new BadRequestException("Tên người dùng cần tìm kiếm không được để trống!");
        }

        return ResponseEntity.ok(managerCustomerService.searchPageCustomerByFullNameAndStatus(size, page, status, search));
    }


    @GetMapping("/detail/{customerId}")
    public ResponseEntity<ProfileCustomerResponse> getCustomerDetailByCustomerId(@PathVariable Long customerId) {

        return ResponseEntity.ok(managerCustomerService.getCustomerDetailByCustomerId(customerId));
    }


    @PatchMapping("/update/{customerId}/role/{role}")
    public ResponseEntity<ProfileCustomerResponse> updateCustomerRoleByCustomerId(@PathVariable Long customerId, @PathVariable Role role) {
        if (!role.equals(Role.MANAGERCUSTOMER) && !role.equals(Role.MANAGERSHIPPING) && !role.equals(Role.MANAGERVENDOR)) {
            throw new BadRequestException("Quyền không hợp lệ! Quyền quản lý khách hàng: MANAGERCUSTOMER, quản lý vận chuyển: MANAGERSHIPPING, quản lý nhà cung cấp: MANAGERVENDOR");
        }

        return ResponseEntity.ok(managerCustomerService.updateCustomerRoleByCustomerId(customerId, role));
    }

}
