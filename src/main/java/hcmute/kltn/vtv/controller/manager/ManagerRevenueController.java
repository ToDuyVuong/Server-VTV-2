package hcmute.kltn.vtv.controller.manager;


import hcmute.kltn.vtv.model.data.vtv.response.*;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.service.manager.IManagerRevenueService;
import hcmute.kltn.vtv.service.vtv.IDateService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/manager/revenue")
@RequiredArgsConstructor
public class ManagerRevenueController {

    private final IDateService dateService;
    private final IManagerRevenueService managerRevenueService;


    @GetMapping("/statistics/customers")
    public ResponseEntity<StatisticsCustomersResponse> statisticsCustomersByDateAndStatus(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        dateService.checkDatesRequest(startDate, endDate, 31);
        return ResponseEntity.ok(managerRevenueService.statisticsCustomersByDateAndStatus(startDate, endDate));
    }


    @GetMapping("/statistics/orders/status/{status}")
    public ResponseEntity<StatisticsOrdersResponse> statisticsOrderByDateAndStatus(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @PathVariable OrderStatus status) {
        dateService.checkDatesRequest(startDate, endDate, 31);

        return ResponseEntity.ok(managerRevenueService.statisticsOrderByDateAndStatus(startDate, endDate, status));
    }


    @GetMapping("/statistics/products/limit/{limit}")
    public ResponseEntity<StatisticsProductsResponse> getTopProductByLimitAndDate(
            @PathVariable int limit,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        if (limit < 1) throw new BadRequestException("Giới hạn phải lớn hơn 0");
        if (limit > 100) throw new BadRequestException("Giới hạn phải nhỏ hơn 100");
        dateService.checkDatesRequest(startDate, endDate, 31);

        return ResponseEntity.ok(managerRevenueService.getTopProductByLimitAndDate(limit, startDate, endDate));
    }

    @GetMapping("/statistics/transports/method/{shippingMethod}")
    public ResponseEntity<StatisticsTransportsResponse> statisticsTransportsByDate(
            @PathVariable String shippingMethod,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        dateService.checkDatesRequest(startDate, endDate, 31);

        return ResponseEntity.ok(managerRevenueService.statisticsTransportsByDateAndShippingMethod(startDate, endDate, shippingMethod));
    }


    @GetMapping("/statistics/fee-order")
    public ResponseEntity<StatisticsFeeOrderResponse> statisticsFeeOrderByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        dateService.checkDatesRequest(startDate, endDate, 31);

        return ResponseEntity.ok(managerRevenueService.statisticsFeeOrderByDate(startDate, endDate));
    }


}
