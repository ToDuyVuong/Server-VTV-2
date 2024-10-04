package hcmute.kltn.vtv.controller.manager;

import hcmute.kltn.vtv.model.data.manager.request.ManagerRequest;
import hcmute.kltn.vtv.model.data.manager.response.ManagerPageResponse;
import hcmute.kltn.vtv.model.data.manager.response.ManagerResponse;
import hcmute.kltn.vtv.model.extra.Role;
import hcmute.kltn.vtv.service.admin.IRoleManagerService;
import hcmute.kltn.vtv.service.manager.IManagerRoleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/role")
@RequiredArgsConstructor
public class ManagerRoleController {


    private final IManagerRoleService managerRoleService;
    private final IRoleManagerService roleManagerService;


    @PostMapping("/add")
    public ResponseEntity<ManagerResponse> managerAddRole(@RequestBody ManagerRequest request,
                                                          HttpServletRequest servletRequest) {
        String usernameAdded = (String) servletRequest.getAttribute("username");
        request.validate();

        return ResponseEntity.ok(managerRoleService.managerAddRole(usernameAdded, request.getUsernameCustomer(), request.getRole()));
    }

    @PutMapping("/update")
    public ResponseEntity<ManagerResponse> managerUpdateRole(@RequestBody ManagerRequest request,
                                                             HttpServletRequest servletRequest) {
        String usernameAdded = (String) servletRequest.getAttribute("username");
        request.validate();

        return ResponseEntity.ok(managerRoleService.managerUpdateRole(usernameAdded, request.getUsernameCustomer(), request.getRole()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ManagerResponse> managerDeleteRole(@RequestBody ManagerRequest request,
                                                             HttpServletRequest servletRequest) {
        String usernameAdded = (String) servletRequest.getAttribute("username");
        request.validate();

        return ResponseEntity.ok(managerRoleService.managerDeleteRole(usernameAdded, request.getUsernameCustomer(), request.getRole()));
    }

    @GetMapping("/page/role/{role}")
    public ResponseEntity<ManagerPageResponse> getListManagerPageByRole(@RequestParam int page,
                                                                        @RequestParam int size,
                                                                        @PathVariable Role role) {
        managerRoleService.checkRequestPageParams(page, size);

        return ResponseEntity.ok(managerRoleService.getListManagerPageByRole(role, page, size));
    }

    @PostMapping("/add-role-manager")
    public ResponseEntity<ManagerResponse> addRoleManager(@RequestBody String usernameCustomer,
                                                          HttpServletRequest servletRequest) {
        String username = (String) servletRequest.getAttribute("username");
        ManagerResponse managerResponse = roleManagerService.addRoleManager(username, usernameCustomer.trim());
        return ResponseEntity.ok(managerResponse);
    }

    @PostMapping("/remove-role-manager")
    public ResponseEntity<ManagerResponse> removeRoleManager(@RequestBody String usernameCustomer,
                                                             HttpServletRequest servletRequest) {
        String username = (String) servletRequest.getAttribute("username");
        ManagerResponse managerResponse = roleManagerService.removeRoleManager(username, usernameCustomer.trim());
        return ResponseEntity.ok(managerResponse);
    }

}
