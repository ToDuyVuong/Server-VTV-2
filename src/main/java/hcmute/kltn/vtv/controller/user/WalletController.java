package hcmute.kltn.vtv.controller.user;


import hcmute.kltn.vtv.model.data.wallet.response.WalletResponse;
import hcmute.kltn.vtv.service.wallet.IWalletService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final IWalletService walletService;


    @GetMapping("/get")
    public ResponseEntity<WalletResponse> getWalletByUsername(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(walletService.getWalletResponseByUsername(username));
    }


}
