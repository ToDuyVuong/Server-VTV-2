package hcmute.kltn.vtv.repository.manager;

import hcmute.kltn.vtv.model.entity.manager.ManagerCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerCustomerRepository extends JpaRepository<ManagerCustomer, Long> {
}
