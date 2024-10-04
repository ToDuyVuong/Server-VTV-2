package hcmute.kltn.vtv.repository.manager;

import hcmute.kltn.vtv.model.entity.manager.ManagerProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerProductRepository extends JpaRepository<ManagerProduct, Long> {

    Optional<ManagerProduct> findByProduct_ProductId(Long productId);

    boolean existsByProductProductIdAndLock(Long productId, boolean lock);

    int countAllByLock(boolean lock); //

    Optional<Page<ManagerProduct>> findAllByLock(boolean lock, Pageable pageable);

    int countAllByLockAndProductNameContains(boolean lock, String productName);

    Optional<Page<ManagerProduct>> findAllByLockAndProductNameContains(boolean lock, String productName,
            Pageable pageable);

    boolean existsByProduct_ProductId(Long productId);
}
