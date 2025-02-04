package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.data.user.request.CartRequest;
import hcmute.kltn.vtv.model.data.user.response.CartResponse;
import hcmute.kltn.vtv.model.data.user.response.ListCartResponse;
import hcmute.kltn.vtv.model.entity.user.Cart;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.entity.vendor.ProductVariant;
import hcmute.kltn.vtv.model.extra.CartStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ICartService {
    @Transactional
    CartResponse addNewCart(CartRequest request, String username);

    @Transactional
    List<UUID> getCartIdsAfterAddNewCartsByMapProductVariantIdAndQuantity(Map<Long, Integer> mapProductVariantIdAndQuantity, String username);

    @Transactional
    List<Cart> addNewCartOrderByMapProductVariant(Map<Long, Integer> mapProductVariantIdAndQuantity, String username);

    @Transactional
    CartResponse updateCart(UUID cartId, int quantity, String username);


    @Transactional
    CartResponse deleteCartById(UUID cartId, String username);

    ListCartResponse getListCartByUsername(String username);

    ListCartResponse getListCartByUsernameAndListCartId(String username, List<UUID> cartIds);

    List<Cart> getListCartByUsernameAndIds(String username, List<UUID> cartIds);

    Cart getCartByCartIdAndUsername(UUID cartId, String username);

    @Transactional
    ListCartResponse deleteCartByShopId(Long shopId, String username);

    boolean checkCartsSameShop(String username, List<UUID> cartIds);

    Cart createCartByProductVariant(ProductVariant productVariant, int quantity, Customer customer);

    void checkDuplicateCartIds(List<UUID> cartIds);

    void checkListCartSameShop(String username, List<UUID> cartIds);

    @Transactional
    Cart updateOrderCart(UUID cartId, String username, CartStatus status);

    void checkExistsCartIdsInUsername(List<UUID> cartIds, String username);

    HashMap<Long, List<Cart>> groupCartByShopId(List<Cart> carts);
}
