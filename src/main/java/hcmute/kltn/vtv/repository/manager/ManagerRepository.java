package hcmute.kltn.vtv.repository.manager;

import hcmute.kltn.vtv.model.entity.manager.Manager;
import hcmute.kltn.vtv.model.extra.Role;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    boolean existsByManagerUsername(String username);

    boolean existsByUsernameAdded(String usernameAdded);

    boolean existsByManagerUsernameAndStatus(String username, Status status);

    int countAllBy();

    Optional<Page<Manager>> findAllByOrderByManagerFullName(Pageable pageable);

    int countAllByStatus(Status status);


    Optional<Page<Manager>> findAllByStatusOrderByManagerFullName(Status status, Pageable pageable);

    @Query("SELECT m FROM Manager m " +
            "JOIN m.manager c " +
            "WHERE :role MEMBER OF c.roles AND m.status = :status")
    Optional<Page<Manager>> findAllByManagerRolesAndStatus(@Param("role") Role role, @Param("status") Status status, Pageable pageable);

    Optional<Manager> findByManagerUsername(String username);

    Optional<Manager> findByManagerUsernameAndStatus(String username, Status status);

    int countAllByManagerUsernameContainsAndStatus(String username, Status status);

    Optional<Page<Manager>> findAllByUsernameAddedAndStatus(String usernameAdded, Status status, Pageable pageable);

    Optional<Page<Manager>> findAllByManagerUsernameContainingAndStatusOrderByManagerFullName(String username,
                                                                                              Status status, Pageable pageable);
}
