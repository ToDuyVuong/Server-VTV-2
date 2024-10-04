package hcmute.kltn.vtv.repository.vendor;

import hcmute.kltn.vtv.model.entity.vendor.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {

    Attribute findByAttributeId(Long attributeId);

    Optional<Attribute> findByNameAndValueAndShop_ShopId(String name, String value, Long shopId);

    Optional<Attribute> findByAttributeIdAndShopShopId(Long attributeId, Long shopId);

    boolean existsAllByNameAndValueAndShop_ShopId(String name, String value, Long shopId);




    Optional<List<Attribute>> findAllByShop_ShopIdAndActive(Long shopId, boolean active);


    Optional<List<Attribute>> findAllByShop_ShopIdAndNameContainingAndValueContaining(Long shopId,  String name, String value);



}
