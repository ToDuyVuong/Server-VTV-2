package hcmute.kltn.vtv.controller.vendor;

import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.service.vtv.IDateService;
import hcmute.kltn.vtv.service.vtv.IPageService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.vendor.response.PageOrderResponse;
import hcmute.kltn.vtv.model.data.user.response.ListOrderResponse;
import hcmute.kltn.vtv.model.data.user.response.OrderResponse;
import hcmute.kltn.vtv.service.vendor.IOrderShopService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/vendor/order")
@RequiredArgsConstructor
public class OrderShopController {

    private final IOrderShopService orderShopService;
    private final IPageService pageService;
    private final IDateService dateService;


    @GetMapping("/list")
    public ResponseEntity<ListOrderResponse> getOrders(HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        return ResponseEntity.ok(orderShopService.getOrders(username));
    }


    @GetMapping("/page")
    public ResponseEntity<PageOrderResponse> getPageOrder(HttpServletRequest httpServletRequest,
                                                          @RequestParam int page,
                                                          @RequestParam int size) {
        String username = (String) httpServletRequest.getAttribute("username");
        pageService.checkRequestOrderPageParams(page, size);

        return ResponseEntity.ok(orderShopService.getPageOrder(username, page, size));
    }


    @GetMapping("/page/status/{status}")
    public ResponseEntity<PageOrderResponse> getPageOrderByStatus(HttpServletRequest httpServletRequest,
                                                                  @PathVariable OrderStatus status,
                                                                  @RequestParam int page,
                                                                  @RequestParam int size) {
        String username = (String) httpServletRequest.getAttribute("username");
        pageService.checkRequestOrderPageParams(page, size);

        return ResponseEntity.ok(orderShopService.getPageOrderByStatus(username, status, page, size));
    }


    @GetMapping("/list/status/{status}")
    public ResponseEntity<ListOrderResponse> getOrdersByStatus(HttpServletRequest httpServletRequest,
                                                               @PathVariable OrderStatus status) {
        String username = (String) httpServletRequest.getAttribute("username");
        return ResponseEntity.ok(orderShopService.getOrdersByStatus(username, status));
    }


    @GetMapping("/list/on-same-day")
    public ResponseEntity<ListOrderResponse> getOrdersOnSameDate(HttpServletRequest httpServletRequest,
                                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        String username = (String) httpServletRequest.getAttribute("username");

        return ResponseEntity.ok(orderShopService.getOrdersOnSameDay(username, date));
    }


    @GetMapping("/list/between-date")
    public ResponseEntity<ListOrderResponse> getOrdersBetweenDate(HttpServletRequest httpServletRequest,
                                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        dateService.checkDatesRequest(startDate, endDate, 31);
        String username = (String) httpServletRequest.getAttribute("username");

        return ResponseEntity.ok(orderShopService.getOrdersBetweenDate(username, startDate, endDate));
    }


    @GetMapping("/list/on-same-day/status/{status}")
    public ResponseEntity<ListOrderResponse> getOrdersOnSameDateByStatus(HttpServletRequest httpServletRequest,
                                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
                                                                         @PathVariable OrderStatus status) {
        String username = (String) httpServletRequest.getAttribute("username");

        return ResponseEntity.ok(orderShopService.getOrdersOnSameDayByStatus(username, date, status));
    }


    @GetMapping("/list/between-date/status/{status}")
    public ResponseEntity<ListOrderResponse> getOrdersBetweenDateByStatus(HttpServletRequest httpServletRequest,
                                                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
                                                                          @PathVariable OrderStatus status) {
        dateService.checkDatesRequest(startDate, endDate, 31);
        String username = (String) httpServletRequest.getAttribute("username");

        return ResponseEntity.ok(orderShopService.getOrdersBetweenDateByStatus(username, startDate, endDate, status));
    }


    @GetMapping("/detail/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(HttpServletRequest httpServletRequest,
                                                      @PathVariable UUID orderId) {
        String username = (String) httpServletRequest.getAttribute("username");
        return ResponseEntity.ok(orderShopService.getOrderById(username, orderId));
    }


    @PatchMapping("/update/{orderId}/status/{status}")
    public ResponseEntity<OrderResponse> updateStatusOrder(HttpServletRequest httpServletRequest,
                                                           @PathVariable UUID orderId,
                                                           @PathVariable OrderStatus status) {
        if (!status.equals(OrderStatus.WAITING) && !status.equals(OrderStatus.PICKUP_PENDING) &&
                !status.equals(OrderStatus.PENDING) && !status.equals(OrderStatus.PROCESSING) &&
                !status.equals(OrderStatus.CANCEL)) {
            throw new BadRequestException("Trạng thái không hợp lệ! Đơn hàng chỉ có thể cập nhật trạng thái: " +
                    "CANCEL, WAITING, PICKUP_PENDING, PENDING, PROCESSING");
        }
        String username = (String) httpServletRequest.getAttribute("username");

        return ResponseEntity.ok(orderShopService.updateStatusOrder(username, orderId, status));
    }
}
