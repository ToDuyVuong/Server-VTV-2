package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.dto.user.SearchHistoryDTO;
import hcmute.kltn.vtv.model.entity.user.SearchHistory;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SearchHistoryResponse extends ResponseAbstract {

    private SearchHistoryDTO searchHistoryDTO;


    public static SearchHistoryResponse searchHistoryResponse(SearchHistory searchHistory, String message, String status) {
        SearchHistoryResponse response = new SearchHistoryResponse();
        response.setCode(200);
        response.setMessage(message);
        response.setStatus(status);
        response.setSearchHistoryDTO(SearchHistoryDTO.convertEntityToDTO(searchHistory));

        return response;
    }

}
