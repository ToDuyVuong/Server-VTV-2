package hcmute.kltn.vtv.repository.user;

import hcmute.kltn.vtv.model.entity.user.SearchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, UUID> {

    boolean existsByUsernameAndSearch(String username, String search);

    Optional<Page<SearchHistory>> findByUsernameOrderByCreateAtDesc(String username, PageRequest pageRequest);

    boolean existsByUsernameAndSearchHistoryId(String username, UUID searchHistoryId);

    void deleteByUsernameAndSearchHistoryId(String username, UUID searchHistoryId);

    Optional<SearchHistory> findByUsernameAndSearchHistoryId(String username, UUID searchHistoryId);

    Optional<SearchHistory> findByUsernameAndSearch(String username, String search);
}
