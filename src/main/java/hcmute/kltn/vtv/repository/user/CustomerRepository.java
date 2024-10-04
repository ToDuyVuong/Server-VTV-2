package hcmute.kltn.vtv.repository.user;

import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.Role;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Customer findByUsername(String username);
    // Customer findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Customer> findByEmail(String email);

    Optional<List<Customer>> findAllByStatusAndCreateAtBetween(Status status, LocalDateTime createAt, LocalDateTime createAt2);

    Optional<Customer> findByUsername(String username);

    Optional<Customer> findByUsernameAndStatus(String username, Status status);

    int countAllByStatus(Status status);

    Optional<Page<Customer>> findAllByStatus(Status status, Pageable pageable);

    Optional<Page<Customer>> findAllByStatusOrderByUsername(Status status, Pageable pageable);

    Optional<Page<Customer>> findAllByStatusOrderByFullName(Status status, Pageable pageable);

    Optional<Page<Customer>> findAllByStatusOrderByFullNameDesc(Status status, Pageable pageable);

    Optional<Page<Customer>> findAllByStatusOrderByCreateAtAsc(Status status, Pageable pageable);

    Optional<Page<Customer>> findAllByStatusOrderByCreateAtDesc(Status status, Pageable pageable);

    int countAllByFullNameContainingAndStatus(String fullName, Status status);

    Optional<Page<Customer>> findAllByFullNameContainingAndStatus(String fullName, Status status, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.status = :status AND :role MEMBER OF c.roles")
    Optional<Page<Customer>> findAllByStatusAndRoles(@Param("status") Status status, @Param("role") Role role, Pageable pageable);

}
