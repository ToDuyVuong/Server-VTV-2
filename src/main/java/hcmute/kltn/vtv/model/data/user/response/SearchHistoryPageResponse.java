package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.dto.user.SearchHistoryDTO;
import hcmute.kltn.vtv.model.entity.user.SearchHistory;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SearchHistoryPageResponse extends ResponseAbstract {

    private int count;
    private int page;
    private int size;
    private int totalPage;
    private List<SearchHistoryDTO> searchHistoryDTOs;


    public static SearchHistoryPageResponse searchHistoryPageResponse(Page<SearchHistory> searchHistories, int size, String message, String status) {

        SearchHistoryPageResponse response = new SearchHistoryPageResponse();
        response.setCount(searchHistories.getContent().size());
        response.setPage(searchHistories.getNumber() + 1);
        response.setSize(size);
        response.setTotalPage(searchHistories.getTotalPages());
        response.setCode(200);
        response.setMessage(message);
        response.setStatus(status);
        response.setSearchHistoryDTOs(SearchHistoryDTO.convertEntitiesToDTOs(searchHistories.getContent()));

        return response;
    }


}
