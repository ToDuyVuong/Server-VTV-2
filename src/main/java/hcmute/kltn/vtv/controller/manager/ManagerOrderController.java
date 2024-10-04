package hcmute.kltn.vtv.controller.manager;


import hcmute.kltn.vtv.model.data.user.response.OrderResponse;
import hcmute.kltn.vtv.model.data.vendor.response.PageOrderResponse;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.service.manager.IMangerOrderService;
import hcmute.kltn.vtv.service.vtv.IDateService;
import hcmute.kltn.vtv.service.vtv.IPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/manager/order")
@RequiredArgsConstructor
public class ManagerOrderController {

    private final IMangerOrderService managerOrderService;
    private final IPageService pageService;
    private final IDateService dateService;

    @GetMapping("/page/status/{status}/date")
    public ResponseEntity<PageOrderResponse> getOrderPageByStatusAndDate(@RequestParam("page") int page,
                                                                         @RequestParam("size") int size,
                                                                         @PathVariable("status") OrderStatus status,
                                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        pageService.validatePageNumberAndSize(page, size);
        dateService.checkDatesRequest(startDate, endDate, 31);


        return ResponseEntity.ok(managerOrderService.getOrderPageByStatusAndDate(page, size, status, startDate, endDate));
    }


    @GetMapping("/detail/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetailByOrderId(@PathVariable("orderId") UUID orderId) {
        return ResponseEntity.ok(managerOrderService.getOrderDetailByOrderId(orderId));
    }

}
