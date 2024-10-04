package hcmute.kltn.vtv.controller.manager;


import hcmute.kltn.vtv.model.data.manager.response.ManagerResponse;
import hcmute.kltn.vtv.service.manager.IManagerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/manager-info/get")
@RequiredArgsConstructor
public class ManagerInfoController {

//    @Autowired
//    private IManagerService managerService;
//
//    @GetMapping("/manager")
//    public ResponseEntity<ManagerResponse> getManagerInfo(HttpServletRequest servletRequest) {
//        String username = (String) servletRequest.getAttribute("username");
//
//        return ResponseEntity.ok(managerService.getManagerByUserName(username));
//    }
//
//    @GetMapping("/manager-customer")
//    public ResponseEntity<ManagerResponse> getManagerInfoCustomer(HttpServletRequest servletRequest) {
//        String username = (String) servletRequest.getAttribute("username");
//
//        return ResponseEntity.ok(managerService.getManagerByUserName(username));
//    }
//
//
//    @GetMapping("/manager-shipping")
//    public ResponseEntity<ManagerResponse> getManagerInfoShipping(HttpServletRequest servletRequest) {
//        String username = (String) servletRequest.getAttribute("username");
//
//        return ResponseEntity.ok(managerService.getManagerByUserName(username));
//    }
//
//    @GetMapping("/manager-product")
//    public ResponseEntity<ManagerResponse> getManagerInfoProduct(HttpServletRequest servletRequest) {
//        String username = (String) servletRequest.getAttribute("username");
//
//        return ResponseEntity.ok(managerService.getManagerByUserName(username));
//    }
//
//    @GetMapping("/manager-vendor")
//    public ResponseEntity<ManagerResponse> getManagerInfoVendor(HttpServletRequest servletRequest) {
//        String username = (String) servletRequest.getAttribute("username");
//
//        return ResponseEntity.ok(managerService.getManagerByUserName(username));
//    }




}
