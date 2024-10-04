package hcmute.kltn.vtv.service.user.impl;

import hcmute.kltn.vtv.model.data.user.request.MultipleOrderRequestWithCart;
import hcmute.kltn.vtv.model.data.user.request.OrderRequestWithCart;
import hcmute.kltn.vtv.model.data.user.response.MultipleOrderResponse;
import hcmute.kltn.vtv.model.data.user.response.OrderResponse;
import hcmute.kltn.vtv.model.entity.user.Cart;
import hcmute.kltn.vtv.service.guest.IVoucherService;
import hcmute.kltn.vtv.service.user.*;
import hcmute.kltn.vtv.service.wallet.IWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
public class MultipleOrderServiceImpl implements IMultipleOrderService {

    private final IOrderService orderService;
    private final ICartService cartService;
    private final IVoucherService voucherService;
    private final IWalletService walletService;


    @Override
    public MultipleOrderResponse createMultipleOrderByCartIds(List<UUID> cartIds, String username) {
        cartService.checkDuplicateCartIds(cartIds);
        cartService.checkExistsCartIdsInUsername(cartIds, username);

        List<Cart> carts = cartService.getListCartByUsernameAndIds(username, cartIds);
        HashMap<Long, List<Cart>> mapShopIdToCart = cartService.groupCartByShopId(carts);
        List<OrderResponse> orderResponses = createOrderResponsesByMapCarts(mapShopIdToCart, username);


        return MultipleOrderResponse.multipleOrderResponse(orderResponses, "Tạo đơn nhiều đơn hàng thành công!", "OK");
    }


    @Override
    public MultipleOrderResponse createMultipleOrderByRequest(MultipleOrderRequestWithCart request, String username) {
        checkVoucherSystem(request);
        List<OrderResponse> orderResponses = createMultipleOrderResponsesByRequest(request, username);
        if (request.getOrderRequestWithCarts().get(0).getPaymentMethod().equals("wallet")) {
            walletService.checkBalanceByUsernameAndMoney(username, totalPaymentByOrderResponses(orderResponses));
        }

        return MultipleOrderResponse.multipleOrderResponse(orderResponses, "Cập nhật tạo nhiều đơn hàng thành công!", "OK");
    }


    @Override
    @Transactional
    public MultipleOrderResponse addNewMultipleOrderByRequest(MultipleOrderRequestWithCart request, String username) {
        checkVoucherSystem(request);
        List<OrderResponse> orderResponses = addNewMultipleOrderResponsesByRequest(request, username);
        if (request.getOrderRequestWithCarts().get(0).getPaymentMethod().equals("wallet")) {
            walletService.checkBalanceByUsernameAndMoney(username, totalPaymentByOrderResponses(orderResponses));
        }

        return MultipleOrderResponse.multipleOrderResponse(orderResponses, "Đặt hàng nhiều đơn hàng thành công!", "Success");
    }


    private void checkVoucherSystem(MultipleOrderRequestWithCart request) {
        Map<String, Integer> voucherCodes = new HashMap<>();
        for (OrderRequestWithCart orderRequestWithCart : request.getOrderRequestWithCarts()) {
            if (orderRequestWithCart.getSystemVoucherCode() != null) {
                if (voucherCodes.containsKey(orderRequestWithCart.getSystemVoucherCode())) {
                    voucherCodes.put(orderRequestWithCart.getSystemVoucherCode(), voucherCodes.get(orderRequestWithCart.getSystemVoucherCode()) + 1);
                } else {
                    voucherCodes.put(orderRequestWithCart.getSystemVoucherCode(), 1);
                }
            }
        }

        for (Map.Entry<String, Integer> entry : voucherCodes.entrySet()) {
            if (entry.getValue() > 1) {
                voucherService.checkQuantityVoucher(entry.getKey(), entry.getValue());
            }
        }
    }


    private List<OrderResponse> addNewMultipleOrderResponsesByRequest(MultipleOrderRequestWithCart request, String username) {
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (OrderRequestWithCart orderRequestWithCart : request.getOrderRequestWithCarts()) {
            orderResponses.add(orderService.addNewOrderWithCart(orderRequestWithCart, username));
        }

        return orderResponses;
    }


    private List<OrderResponse> createMultipleOrderResponsesByRequest(MultipleOrderRequestWithCart request, String username) {
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (OrderRequestWithCart orderRequestWithCart : request.getOrderRequestWithCarts()) {
            orderResponses.add(orderService.createOrderWithCart(orderRequestWithCart, username));
        }

        return orderResponses;
    }


    private List<OrderResponse> createOrderResponsesByMapCarts(Map<Long, List<Cart>> mapShopIdToCart, String username) {
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Map.Entry<Long, List<Cart>> entry : mapShopIdToCart.entrySet()) {
            List<UUID> cartIds = entry.getValue().stream().map(Cart::getCartId).toList();
            orderResponses.add(orderService.createOrderByCartIds(username, cartIds));
        }

        return orderResponses;
    }


    private Long totalPaymentByOrderResponses(List<OrderResponse> orderResponses) {
        return orderResponses.stream()
                .mapToLong(orderResponse -> orderResponse.getOrderDTO().getPaymentTotal())
                .sum();
    }



}
