package hcmute.kltn.vtv.controller.user;

import hcmute.kltn.vtv.authentication.response.RegisterResponse;
import hcmute.kltn.vtv.model.data.user.request.ActiveAccountRequest;
import hcmute.kltn.vtv.model.data.user.request.ChangePasswordRequest;
import hcmute.kltn.vtv.model.data.user.request.ForgotPasswordRequest;
import hcmute.kltn.vtv.model.data.user.request.ProfileCustomerRequest;
import hcmute.kltn.vtv.model.data.user.response.ProfileCustomerResponse;
import hcmute.kltn.vtv.model.data.user.response.SendEmailResponse;
import hcmute.kltn.vtv.service.user.ICustomerService;
import hcmute.kltn.vtv.service.vtv.IMailService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final ICustomerService customerService;
    private final IMailService mailService;


    @GetMapping("/profile")
    public ResponseEntity<ProfileCustomerResponse> getProfileCustomer(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");

        return ResponseEntity.ok(customerService.getProfileCustomer(username));
    }


    @PutMapping("/profile")
    public ResponseEntity<ProfileCustomerResponse> updateProfileCustomer(
            @RequestBody ProfileCustomerRequest profileCustomerRequest,
            HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        profileCustomerRequest.setUsername(username);
        profileCustomerRequest.validate();

        return ResponseEntity.ok(customerService.updateProfileCustomer(profileCustomerRequest));
    }


    @PatchMapping("/change-password")
    public ResponseEntity<ProfileCustomerResponse> changePassword(
            @RequestBody ChangePasswordRequest changePasswordRequest,
            HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        changePasswordRequest.setUsername(username);
        changePasswordRequest.validate();

        return ResponseEntity.ok(customerService.changePassword(changePasswordRequest));
    }



    @GetMapping("/forgot-password")
    public ResponseEntity<SendEmailResponse> sendMailForgotPassword(@RequestParam("username") String username) {
        if (username == null || username.isEmpty()) {
            throw new BadRequestException("Tài khoản không được để trống.");
        }

        return ResponseEntity.ok(mailService.forgotPasswordSendOtpToEmail(username));
    }


    @GetMapping("/active-account/send-email")
    public ResponseEntity<SendEmailResponse> sendEmailActivateAccount(@RequestParam("username") String username) {
        if (username == null || username.isEmpty()) {
            throw new BadRequestException("Tài khoản không được để trống.");
        }
        customerService.checkAccountUnActive(username);

        return ResponseEntity.ok(mailService.activateAccountSendOtpToEmail(username));
    }

    @PostMapping("/active-account")
    public ResponseEntity<RegisterResponse> activateAccount(@RequestBody ActiveAccountRequest request) {
        request.validate();

        return ResponseEntity.ok(customerService.activateAccount(request));
    }



    @PatchMapping("/reset-password")
    public ResponseEntity<RegisterResponse> resetPassword(@RequestBody ForgotPasswordRequest request) {
        request.validate();

        return ResponseEntity.ok(customerService.resetPassword(request));
    }


}