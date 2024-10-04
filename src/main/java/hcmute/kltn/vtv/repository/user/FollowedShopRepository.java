package hcmute.kltn.vtv.repository.user;

import hcmute.kltn.vtv.model.entity.user.FollowedShop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowedShopRepository extends JpaRepository<FollowedShop, Long> {

    boolean existsByCustomerUsernameAndShopShopId(String username, Long shopId);

    Optional<List<FollowedShop>> findAllByCustomerUsername(String username);

    int countByShopShopId(Long shopId);
}