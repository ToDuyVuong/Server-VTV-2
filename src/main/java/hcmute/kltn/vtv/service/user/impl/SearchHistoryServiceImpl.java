package hcmute.kltn.vtv.service.user.impl;


import hcmute.kltn.vtv.model.data.guest.ResponseClass;
import hcmute.kltn.vtv.model.data.user.response.SearchHistoryPageResponse;
import hcmute.kltn.vtv.model.data.user.response.SearchHistoryResponse;
import hcmute.kltn.vtv.model.dto.user.SearchHistoryDTO;
import hcmute.kltn.vtv.model.entity.user.SearchHistory;
import hcmute.kltn.vtv.repository.user.SearchHistoryRepository;
import hcmute.kltn.vtv.service.user.ISearchHistoryService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SearchHistoryServiceImpl implements ISearchHistoryService {


    private final SearchHistoryRepository searchHistoryRepository;


    @Override
    public SearchHistoryPageResponse getSearchHistoryByUsername(String username, int page, int size) {
        Page<SearchHistory> searchHistories = searchHistoryPage(username, page, size);

        return SearchHistoryPageResponse.searchHistoryPageResponse(searchHistories, size, "Lấy lịch sử tìm kiếm thành công!", "OK");
    }


    @Override
    @Transactional
    public SearchHistoryResponse addNewSearchHistory(String username, String search) {
        SearchHistory searchHistory = new SearchHistory();
        if (searchHistoryRepository.existsByUsernameAndSearch(username, search)) {
            searchHistory = searchHistoryRepository.findByUsernameAndSearch(username, search)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy lịch sử tìm kiếm!"));
        } else {
            searchHistory.setSearch(search);
            searchHistory.setUsername(username);
        }
        searchHistory.setCreateAt(LocalDateTime.now());

        try {
            searchHistoryRepository.save(searchHistory);

            return SearchHistoryResponse.searchHistoryResponse(searchHistory, "Thêm lịch sử tìm kiếm thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi thêm lịch sử tìm kiếm!");
        }
    }


    @Override
    @Transactional
    public ResponseClass deleteSearchHistory(String username, UUID searchHistoryId) {
        if (!searchHistoryRepository.existsByUsernameAndSearchHistoryId(username, searchHistoryId)) {
            throw new NotFoundException("Không tìm thấy lịch sử tìm kiếm để xóa có id: " + searchHistoryId);
        }
        try {
            searchHistoryRepository.deleteByUsernameAndSearchHistoryId(username, searchHistoryId);

            return ResponseClass.responseClass("Xóa lịch sử tìm kiếm thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi xóa lịch sử tìm kiếm!");
        }
    }


    @Override
    public String toStringSearchHistoryByUsername(String username, int page, int size) {

        Page<SearchHistory> searchHistories = searchHistoryPage(username, page, size);

        StringBuilder searchHistoriesToString = new StringBuilder();
        for (SearchHistory searchHistory : searchHistories.getContent()) {
            searchHistoriesToString.append(searchHistory.getSearch()).append(" ");
        }

        return searchHistoriesToString.toString();
    }


    private Page<SearchHistory> searchHistoryPage(String username, int page, int size) {
        return searchHistoryRepository.findByUsernameOrderByCreateAtDesc(username, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy lịch sử tìm kiếm!"));
    }


}
