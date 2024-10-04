package hcmute.kltn.vtv.controller.admin;

import hcmute.kltn.vtv.model.data.manager.response.ManagerPageResponse;
import hcmute.kltn.vtv.model.data.manager.response.ManagerResponse;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.service.admin.IRoleManagerService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/manage")
@RequiredArgsConstructor
public class AdminManagerController {


    private final IRoleManagerService roleManagerService;

    @PostMapping("/add-role-manager")
    public ResponseEntity<ManagerResponse> addRoleManager(@RequestParam String usernameCustomer,
            HttpServletRequest servletRequest) {
        String username = (String) servletRequest.getAttribute("username");
        ManagerResponse managerResponse = roleManagerService.addRoleManager(username, usernameCustomer.trim());
        return ResponseEntity.ok(managerResponse);
    }

    @PostMapping("/remove-role-manager")
    public ResponseEntity<ManagerResponse> removeRoleManager(@RequestParam String usernameCustomer,
            HttpServletRequest servletRequest) {
        String username = (String) servletRequest.getAttribute("username");
        ManagerResponse managerResponse = roleManagerService.removeRoleManager(username, usernameCustomer.trim());
        return ResponseEntity.ok(managerResponse);
    }

    @GetMapping("/get-list-manager")
    public ResponseEntity<ManagerPageResponse> getListManager(@RequestParam int size,
                                                              @RequestParam int page) {
        roleManagerService.checkRequestPageParams(size, page);
        return ResponseEntity.ok(roleManagerService.getManagerPage(page, size));
    }

    @GetMapping("/find-manager")
    public ResponseEntity<ManagerPageResponse> findManagerPageByUsername(@RequestParam int size,
                                                                         @RequestParam int page,
                                                                         @RequestParam String username) {
        roleManagerService.checkRequestPageParams(size, page);
        if (username.trim().isEmpty()) {
            throw new BadRequestException("Tên tài khoản không được để trống!");
        }
        return ResponseEntity.ok(roleManagerService.getManagerPageByUsername(page, size, username.trim()));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ManagerPageResponse> getListManagerByStatus(@PathVariable Status status,
                                                                      @RequestParam int size,
                                                                      @RequestParam int page) {
        roleManagerService.checkRequestPageParams(size, page);
        return ResponseEntity.ok(roleManagerService.getManagerPageByStatus(page, size, status));
    }

}
