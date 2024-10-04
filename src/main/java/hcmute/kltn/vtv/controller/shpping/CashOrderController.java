


package hcmute.kltn.vtv.controller.shpping;


import hcmute.kltn.vtv.model.data.shipping.request.CashOrdersRequest;
import hcmute.kltn.vtv.model.data.shipping.response.CashOrderDetailResponse;
import hcmute.kltn.vtv.model.data.shipping.response.CashOrdersByDatesResponse;
import hcmute.kltn.vtv.model.data.shipping.response.CashOrdersResponse;
import hcmute.kltn.vtv.service.shipping.ICashOrderService;
import hcmute.kltn.vtv.service.shipping.IDeliverService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shipping/cash-order")
@RequiredArgsConstructor
public class CashOrderController {


    private final ICashOrderService cashOrderService;
    private final IDeliverService deliverService;


    @PostMapping("/updates/transfers-money-warehouse")
    public ResponseEntity<CashOrdersResponse> shipperUpdateCashOrdersByShipper(@RequestBody CashOrdersRequest request,
                                                                               HttpServletRequest servletRequest) {
        String username = (String) servletRequest.getAttribute("username");
        request.validate();
        deliverService.checkExistByTypeWorkShipperByUsername(username);

        return ResponseEntity.ok(cashOrderService.updateCashOrdersWithWaveHouseHold(request, username, false, false));
    }


    @PostMapping("/updates/confirm-money-warehouse")
    public ResponseEntity<CashOrdersResponse> shipperUpdateCashOrdersByWareHouse(@RequestBody CashOrdersRequest request,
                                                                                 HttpServletRequest servletRequest) {
        String username = (String) servletRequest.getAttribute("username");
        request.setWaveHouseUsername(username);
        request.validate();
        deliverService.checkExistByTypeWorkWarehouseByUsername(username);

        return ResponseEntity.ok(cashOrderService.updateCashOrdersWithWaveHouseHold(request, username, false, true));
    }

    @PostMapping("/updates/warehouse-confirm")
    public ResponseEntity<CashOrdersResponse> wareHouseUpdateCashOrdersByWaveHouse(@RequestBody List<UUID> cashOrderIds,
                                                                                   HttpServletRequest servletRequest) {
        String username = (String) servletRequest.getAttribute("username");
        if (cashOrderIds == null || cashOrderIds.isEmpty()) {
            throw new BadRequestException("Danh sách mã đơn tiền không được để trống.");
        }
        deliverService.checkExistByTypeWorkWarehouseByUsername(username);
        CashOrdersRequest request = CashOrdersRequest.cashOrdersRequest(cashOrderIds, username);

        return ResponseEntity.ok(cashOrderService.updateCashOrdersWithWaveHouseHold(request, username, false, true));
    }


    @GetMapping("/list-by-ware-house")
    public ResponseEntity<CashOrdersResponse> getCashOrdersByWaveHouseUsername(HttpServletRequest servletRequest) {
        String waveHouseUsername = (String) servletRequest.getAttribute("username");

        return ResponseEntity.ok(cashOrderService.getListCashOrdersByWaveHouseUsernameAnhStatus(waveHouseUsername));
    }


    @GetMapping("/list/ware-house/can-update")
    public ResponseEntity<CashOrdersResponse> getCashOrdersCanUpdateByWareHouseUsername(HttpServletRequest servletRequest) {
        String shipperUsername = (String) servletRequest.getAttribute("username");

        return ResponseEntity.ok(cashOrderService.getCashOrdersCanUpdateByWareHouseUsername(shipperUsername));
    }


    @GetMapping("/all-by-shipper")
    public ResponseEntity<CashOrdersResponse> getAllCashOrdersByShipperUsername(HttpServletRequest servletRequest) {
        String shipperUsername = (String) servletRequest.getAttribute("username");

        return ResponseEntity.ok(cashOrderService.getListCashOrdersByShipperUsername(shipperUsername));
    }


    @GetMapping("/list/shipper/can-update")
    public ResponseEntity<CashOrdersResponse> getCashOrdersCanUpdateByShipperUsername(HttpServletRequest servletRequest) {
        String shipperUsername = (String) servletRequest.getAttribute("username");

        return ResponseEntity.ok(cashOrderService.getCashOrdersCanUpdateByShipperUsername(shipperUsername));
    }


    @GetMapping("/history-by-shipper")
    public ResponseEntity<CashOrdersByDatesResponse> historyCashOrdersByShipperUsername(HttpServletRequest servletRequest,
                                                                                        @RequestParam("shipperHold") boolean shipperHold,
                                                                                        @RequestParam("shipping") boolean shipping) {
        if (shipperHold && shipping) {
            throw new BadRequestException("Không thể lấy lịch sử đơn hàng với cả hai trạng thái: shipperHold và shipping cùng true!");
        }
        String shipperUsername = (String) servletRequest.getAttribute("username");

        return ResponseEntity.ok(cashOrderService.historyCashOrdersByShipperUsernameAndShipperHold(shipperUsername, shipperHold, shipping));
    }


    @GetMapping("/history-by-warehouse")
    public ResponseEntity<CashOrdersByDatesResponse> historyCashOrdersByWarehouseUsername(HttpServletRequest servletRequest,
                                                                                          @RequestParam("warehouseHold") boolean warehouseHold,
                                                                                          @RequestParam("handlePayment") boolean handlePayment) {
        if (warehouseHold && handlePayment) {
            throw new BadRequestException("Không thể lấy lịch sử đơn hàng với cả hai trạng thái: warehouseHold và handlePayment cùng true!");
        }
        String warehouseUsername = (String) servletRequest.getAttribute("username");

        return ResponseEntity.ok(cashOrderService.historyCashOrdersByWaveHouseUsernameAndWaveHouseHold(warehouseUsername, warehouseHold, handlePayment));
    }


//   @GetMapping("/history-by-username")
//    public ResponseEntity<CashOrdersByDatesResponse> historyCashOrdersByUsername(HttpServletRequest servletRequest,
//                                                                                          @RequestParam("warehouseHold") boolean warehouseHold,
//                                                                                          @RequestParam("handlePayment") boolean handlePayment) {
//        if (warehouseHold && handlePayment) {
//            throw new BadRequestException("Không thể lấy lịch sử đơn hàng với cả hai trạng thái: warehouseHold và handlePayment cùng true!");
//        }
//        String warehouseUsername = (String) servletRequest.getAttribute("username");
//
//        return ResponseEntity.ok(cashOrderService.historyCashOrdersByWaveHouseUsernameAndWaveHouseHold(warehouseUsername, warehouseHold, handlePayment));
//    }


    @GetMapping("/detail/{cashOrderId}")
    public ResponseEntity<CashOrderDetailResponse> getDetailCashOrder(@PathVariable("cashOrderId") UUID cashOrderId,
                                                                      HttpServletRequest servletRequest) {
        String username = (String) servletRequest.getAttribute("username");
        return ResponseEntity.ok(cashOrderService.getDetailCashOrder(cashOrderId, username));
    }


}