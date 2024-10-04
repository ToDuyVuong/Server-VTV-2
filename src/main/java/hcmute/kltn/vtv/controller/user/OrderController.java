package hcmute.kltn.vtv.controller.user;

import hcmute.kltn.vtv.model.data.user.request.MultipleOrderRequestWithCart;
import hcmute.kltn.vtv.model.data.user.request.OrderRequestWithCart;
import hcmute.kltn.vtv.model.data.user.request.OrderRequestWithProductVariant;
import hcmute.kltn.vtv.model.data.user.response.ListOrderResponse;
import hcmute.kltn.vtv.model.data.user.response.MultipleOrderResponse;
import hcmute.kltn.vtv.model.data.user.response.OrderResponse;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.service.shipping.IShippingService;
import hcmute.kltn.vtv.service.user.IMultipleOrderService;
import hcmute.kltn.vtv.service.user.IOrderService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer/order")
@RequiredArgsConstructor
public class OrderController {

    private final IMultipleOrderService multipleOrderService;
    private final IOrderService orderService;
    private final IShippingService shippingService;

    @PostMapping("/create/by-cartIds")
    public ResponseEntity<OrderResponse> createOrderByCartIds(@RequestBody List<UUID> cartIds,
                                                              HttpServletRequest request) {
        if (cartIds.isEmpty()) {
            throw new NotFoundException("Danh sách mã giỏ hàng không được để trống!");
        }

        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(orderService.createOrderByCartIds(username, cartIds));
    }

    @PostMapping("/create-update/with-cart")
    public ResponseEntity<OrderResponse> createOrderWithCart(@RequestBody OrderRequestWithCart orderRequestWithCart,
                                                             HttpServletRequest request) {
        OrderRequestWithCart.validate(orderRequestWithCart);
        shippingService.checkShippingProvider(orderRequestWithCart.getShippingMethod());

        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(orderService.createOrderWithCart(orderRequestWithCart, username));
    }


    @PostMapping("/add/with-cart")
    public ResponseEntity<OrderResponse> addNewOrderWithCart(@RequestBody OrderRequestWithCart orderRequestWithCart,
                                                             HttpServletRequest request) {
        OrderRequestWithCart.validate(orderRequestWithCart);
        String username = (String) request.getAttribute("username");
        shippingService.checkShippingProvider(orderRequestWithCart.getShippingMethod());

        return ResponseEntity.ok(orderService.addNewOrderWithCart(orderRequestWithCart, username));
    }


    @PostMapping("/create/by-product-variant")
    public ResponseEntity<OrderResponse> createOrderByProductVariantIdsAndQuantities(@RequestBody Map<Long, Integer> productVariantIdsAndQuantities,
                                                                                     HttpServletRequest request) {
        if (productVariantIdsAndQuantities.isEmpty()) {
            throw new NotFoundException("Danh sách mã sản phẩm không được để trống!");
        }


        productVariantIdsAndQuantities.forEach((k, v) -> {
            if (k == null || v == null) {
                throw new BadRequestException("Danh sách sản phẩm không hợp lệ!");
            }
            if (v <= 0) {
                throw new BadRequestException("Số lượng biến thể sản phẩm của mã biến thể sản phẩm " + k + " không hợp lệ!");
            }
        });

        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(orderService.createOrderByMapProductVariantsAndQuantities(username, productVariantIdsAndQuantities));
    }


    @PostMapping("/create-update/with-product-variant")
    public ResponseEntity<OrderResponse> createOrderWithProductVariant(@RequestBody OrderRequestWithProductVariant orderRequestWithProductVariant,
                                                                       HttpServletRequest request) {
        OrderRequestWithProductVariant.validate(orderRequestWithProductVariant);
        String username = (String) request.getAttribute("username");
        shippingService.checkShippingProvider(orderRequestWithProductVariant.getShippingMethod());

        return ResponseEntity.ok(orderService.createOrderWithProductVariant(orderRequestWithProductVariant, username));
    }


    @PostMapping("/add/with-product-variant")
    public ResponseEntity<OrderResponse> addNewOrderWithProductVariant(@RequestBody OrderRequestWithProductVariant orderRequestWithProductVariant,
                                                                       HttpServletRequest request) {
        OrderRequestWithProductVariant.validate(orderRequestWithProductVariant);
        String username = (String) request.getAttribute("username");
        shippingService.checkShippingProvider(orderRequestWithProductVariant.getShippingMethod());


        return ResponseEntity.ok(orderService.addNewOrderWithProductVariant(orderRequestWithProductVariant, username));
    }


    @PostMapping("/create/multiple/by-cartIds")
    public ResponseEntity<MultipleOrderResponse> createMultipleOrderByCartIds(@RequestBody List<UUID> cartIds,
                                                                              HttpServletRequest request) {
        if (cartIds.isEmpty()) {
            throw new NotFoundException("Danh sách mã giỏ hàng không được để trống!");
        }

        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(multipleOrderService.createMultipleOrderByCartIds(cartIds, username));
    }


    @PostMapping("/create/multiple/by-request")
    public ResponseEntity<MultipleOrderResponse> createMultipleOrderByRequestRequest(@RequestBody MultipleOrderRequestWithCart request,
                                                                                     HttpServletRequest requestHttp) {
        MultipleOrderRequestWithCart.validate(request);
        String username = (String) requestHttp.getAttribute("username");
        for (OrderRequestWithCart orderRequestWithCart : request.getOrderRequestWithCarts()) {
            shippingService.checkShippingProvider(orderRequestWithCart.getShippingMethod());
        }

        return ResponseEntity.ok(multipleOrderService.createMultipleOrderByRequest(request, username));
    }


    @PostMapping("/add/multiple/by-request")
    public ResponseEntity<MultipleOrderResponse> addNewMultipleOrderByRequest(@RequestBody MultipleOrderRequestWithCart request,
                                                                              HttpServletRequest requestHttp) {
        MultipleOrderRequestWithCart.validate(request);
        for (OrderRequestWithCart orderRequestWithCart : request.getOrderRequestWithCarts()) {
            shippingService.checkShippingProvider(orderRequestWithCart.getShippingMethod());
        }
        String username = (String) requestHttp.getAttribute("username");
        return ResponseEntity.ok(multipleOrderService.addNewMultipleOrderByRequest(request, username));
    }


    @GetMapping("/list")
    public ResponseEntity<ListOrderResponse> getOrders(HttpServletRequest requestHttp) {
        String username = (String) requestHttp.getAttribute("username");
        return ResponseEntity.ok(orderService.getOrders(username));
    }


    @GetMapping("/list/status/{status}")
    public ResponseEntity<ListOrderResponse> getOrdersByStatus(@PathVariable OrderStatus status,
                                                               HttpServletRequest requestHttp) {
        String username = (String) requestHttp.getAttribute("username");
        return ResponseEntity.ok(orderService.getOrdersByStatus(username, status));
    }


    @GetMapping("/detail/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetail(@PathVariable UUID orderId,
                                                        HttpServletRequest requestHttp) {
        String username = (String) requestHttp.getAttribute("username");
        return ResponseEntity.ok(orderService.getOrderDetail(username, orderId));
    }


    @PatchMapping("/cancel/{orderId}")
    public ResponseEntity<OrderResponse> cancelOrderById(@PathVariable UUID orderId,
                                                         HttpServletRequest requestHttp) {
        String username = (String) requestHttp.getAttribute("username");
        return ResponseEntity.ok(orderService.cancelOrderById(username, orderId));
    }


    @PatchMapping("/return/{orderId}")
    public ResponseEntity<OrderResponse> returnOrderById(@PathVariable UUID orderId,
                                                         @RequestBody String reason,
                                                         HttpServletRequest requestHttp) {
        if (reason == null || reason.isEmpty()) {
            throw new BadRequestException("Lý do trả hàng không được để trống!");
        }
        String username = (String) requestHttp.getAttribute("username");
        return ResponseEntity.ok(orderService.returnOrderById(username, orderId, reason));
    }


    @PatchMapping("/complete/{orderId}")
    public ResponseEntity<OrderResponse> completeOrderById(@PathVariable UUID orderId,
                                                           HttpServletRequest requestHttp) {
        String username = (String) requestHttp.getAttribute("username");
        return ResponseEntity.ok(orderService.completeOrderById(username, orderId));
    }


}
