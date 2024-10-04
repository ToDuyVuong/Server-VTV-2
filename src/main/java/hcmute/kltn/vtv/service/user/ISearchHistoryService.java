package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.data.guest.ResponseClass;
import hcmute.kltn.vtv.model.data.user.response.SearchHistoryPageResponse;
import hcmute.kltn.vtv.model.data.user.response.SearchHistoryResponse;
import hcmute.kltn.vtv.model.dto.user.SearchHistoryDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ISearchHistoryService {

    SearchHistoryPageResponse getSearchHistoryByUsername(String username, int page, int size);

    @Transactional
    SearchHistoryResponse addNewSearchHistory(String username, String search);

    @Transactional
    ResponseClass deleteSearchHistory(String username, UUID searchHistoryId);

    String toStringSearchHistoryByUsername(String username, int size, int page);
}
