package hcmute.kltn.vtv.repository.vendor;


import hcmute.kltn.vtv.model.entity.vendor.CategoryShop;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryShopRepository extends JpaRepository<CategoryShop, Long> {


    boolean existsByCategoryShopId(Long categoryShopId);

    boolean existsByCategoryShopIdAndShopShopId(Long categoryShopId, Long shopId);

    boolean existsByCategoryShopIdNotAndNameAndShopShopId(Long categoryShopId, String name, Long shopId);


    boolean existsByProductsInAndCategoryShopId(Collection<List<Product>> products, Long categoryShopId);

    int countCategoryShopByShopShopId(Long shopId);


    boolean existsByNameAndCategoryShopIdNot(String name, Long shopId);

    Optional<CategoryShop> findByCategoryShopId(Long categoryShopId);

    Optional<List<CategoryShop>> findAllByShopShopId(Long shopId);

}
