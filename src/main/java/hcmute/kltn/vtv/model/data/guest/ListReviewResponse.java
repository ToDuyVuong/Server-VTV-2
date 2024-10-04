package hcmute.kltn.vtv.model.data.guest;

import hcmute.kltn.vtv.model.dto.user.ReviewDTO;
import hcmute.kltn.vtv.model.entity.user.Review;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListReviewResponse extends ResponseAbstract {

    private int count;
    private Long productId;
    private long averageRating;
    private List<ReviewDTO> reviewDTOs;



    public static ListReviewResponse listReviewResponse(List<Review> reviews, String message, Long productId, long averageRating) {
        ListReviewResponse listReviewResponse = new ListReviewResponse();
        listReviewResponse.setReviewDTOs(ReviewDTO.convertEntitiesToDTOs(reviews));
        listReviewResponse.setCount(reviews.size());
        listReviewResponse.setProductId(productId);
        listReviewResponse.setMessage(message);
        listReviewResponse.setStatus("OK");
        listReviewResponse.setCode(200);
        listReviewResponse.setAverageRating(averageRating);

        return listReviewResponse;
    }




}
