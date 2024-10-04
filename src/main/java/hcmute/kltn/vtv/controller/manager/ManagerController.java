package hcmute.kltn.vtv.controller.manager;


import hcmute.kltn.vtv.model.data.manager.response.ManagerPageResponse;
import hcmute.kltn.vtv.model.data.manager.response.ManagerResponse;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.service.admin.IRoleManagerService;
import hcmute.kltn.vtv.service.manager.IManagerService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final IManagerService managerService;
    private final IRoleManagerService roleManagerService;

    @GetMapping("/info")
    public ResponseEntity<ManagerResponse> getManagerInfo(HttpServletRequest servletRequest) {
        String username = (String) servletRequest.getAttribute("username");

        return ResponseEntity.ok(managerService.getManagerByUserName(username));
    }

    @GetMapping("/page/username-added/{usernameAdded}/status/{status}")
    public ResponseEntity<ManagerPageResponse> getManagerPageByUsernameAddedAndStatus(@PathVariable String usernameAdded,
                                                                                   @PathVariable Status status,
                                                                                   @RequestParam int page,
                                                                                   @RequestParam int size) {
        managerService.checkRequestPageParams(page, size);

        return ResponseEntity.ok(managerService.getManagerPageByUsernameAddedAndStatus(usernameAdded, status, page, size));
    }

    @GetMapping("/page/status/{status}")
    public ResponseEntity<ManagerPageResponse> getManagerPageByStatus(@PathVariable Status status,
                                                                      @RequestParam int page,
                                                                      @RequestParam int size) {
        managerService.checkRequestPageParams(page, size);

        return ResponseEntity.ok(managerService.getManagerPageByStatus(status, page, size));
    }

    @DeleteMapping("/delete/{managerId}")
    public ResponseEntity<ManagerResponse> deleteManagerByrId(@PathVariable Long managerId,
                                                              HttpServletRequest servletRequest) {
        String username = (String) servletRequest.getAttribute("username");

        return ResponseEntity.ok(managerService.deleteManager(username, managerId));
    }


    @GetMapping("/page")
    public ResponseEntity<ManagerPageResponse> getManagerPage(@RequestParam int size,
                                                              @RequestParam int page) {
        roleManagerService.checkRequestPageParams(size, page);
        return ResponseEntity.ok(roleManagerService.getManagerPage(page, size));
    }


    @GetMapping("/page/search/{username}")
    public ResponseEntity<ManagerPageResponse> findManagerPageByUsername(@RequestParam int size,
                                                                         @RequestParam int page,
                                                                         @PathVariable String username) {
        roleManagerService.checkRequestPageParams(size, page);
        if (username.trim().isEmpty()) {
            throw new BadRequestException("Tên tài khoản không được để trống!");
        }
        return ResponseEntity.ok(roleManagerService.getManagerPageByUsername(page, size, username.trim()));
    }


}
