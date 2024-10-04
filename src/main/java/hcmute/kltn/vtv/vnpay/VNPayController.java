package hcmute.kltn.vtv.vnpay;


import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.vnpay.model.VNPayDTO;
import hcmute.kltn.vtv.vnpay.model.VNPayResponse;
import hcmute.kltn.vtv.vnpay.service.impl.IOrderVNPayService;
import hcmute.kltn.vtv.vnpay.service.impl.IVNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vnpay")
@RequiredArgsConstructor
public class VNPayController {

    private final IVNPayService vnPayService;
    private final IOrderVNPayService orderVNPayService;

    @PostMapping("/create-payment/{orderId}")
    public ResponseEntity<VNPayResponse> createPaymentByVNPay(@PathVariable UUID orderId, HttpServletRequest req) {
        String ipAddress = VNPayConfig.getIpAddress(req);
        String username = (String) req.getAttribute("username");

        return ResponseEntity.ok(vnPayService.createPaymentByVNPay(orderId, ipAddress, username));
    }

    @PostMapping("/create-payment-web/{orderId}")
    public ResponseEntity<VNPayResponse> createPaymentByVNPayForWeb(@PathVariable UUID orderId, HttpServletRequest req) {
        String ipAddress = VNPayConfig.getIpAddress(req);
        String username = (String) req.getAttribute("username");

        return ResponseEntity.ok(vnPayService.createPaymentByVNPayForWeb(orderId, ipAddress, username));
    }


    @PostMapping("/create-payment/multiple-order")
    public ResponseEntity<VNPayResponse> createPaymentByVNPayWithMultipleOrder(@RequestBody List<UUID> orderIds,
                                                                               HttpServletRequest req) {
        String ipAddress = VNPayConfig.getIpAddress(req);
        String username = (String) req.getAttribute("username");

        return ResponseEntity.ok(vnPayService.createPaymentByVNPayWithMultipleOrder(orderIds, ipAddress, username));
    }

    @PostMapping("/create-payment-web/multiple-order")
    public ResponseEntity<VNPayResponse> createPaymentByVNPayWithMultipleOrderForWeb(@RequestBody List<UUID> orderIds,
                                                                               HttpServletRequest req) {
        String ipAddress = VNPayConfig.getIpAddress(req);
        String username = (String) req.getAttribute("username");

        return ResponseEntity.ok(vnPayService.createPaymentByVNPayWithMultipleOrderForWeb(orderIds, ipAddress, username));
    }

    @GetMapping("/return")
    public ResponseEntity<VNPayDTO> returnPayment(@RequestParam String vnp_ResponseCode,
                                                  @RequestParam String vnp_TxnRef,
                                                  HttpServletRequest req) throws Exception {
        String ipAddress = VNPayConfig.getIpAddress(req);
        VNPayDTO vnPayDTO = vnPayService.checkPaymentByVNPay(vnp_TxnRef, ipAddress, req);
        if (!vnPayDTO.getTransactionStatus().equals("00") || !vnp_ResponseCode.equals("00")) {
            throw new BadRequestException("Đơn hàng chưa được thanh toán hoặc đã xử lý trước đó!");
        }

        if (vnp_TxnRef.contains(",")) {
            String[] orderIds = vnp_TxnRef.split(",");
            for (String orderId : orderIds) {
                orderVNPayService.updateOrderStatusAfterPayment(UUID.fromString(orderId.trim()));
            }
        } else {
            orderVNPayService.updateOrderStatusAfterPayment(UUID.fromString(vnp_TxnRef));
        }

        return ResponseEntity.ok(vnPayDTO);
    }


}
