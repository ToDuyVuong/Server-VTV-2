package hcmute.kltn.vtv.model.dto.user;


import hcmute.kltn.vtv.model.entity.user.SearchHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class SearchHistoryDTO {

    private UUID searchHistoryId;

    private String search;

    private LocalDateTime createAt;

    public static SearchHistoryDTO convertEntityToDTO(SearchHistory searchHistory) {

        SearchHistoryDTO searchHistoryDTO = new SearchHistoryDTO();
        searchHistoryDTO.setSearchHistoryId(searchHistory.getSearchHistoryId());
        searchHistoryDTO.setSearch(searchHistory.getSearch());
        searchHistoryDTO.setCreateAt(searchHistory.getCreateAt());
        return searchHistoryDTO;
    }

    public static List<SearchHistoryDTO> convertEntitiesToDTOs(List<SearchHistory> searchHistories) {
        return searchHistories.stream().map(SearchHistoryDTO::convertEntityToDTO).sorted(Comparator.comparing(SearchHistoryDTO::getCreateAt).reversed()).toList();
    }
}
