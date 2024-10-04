package hcmute.kltn.vtv.model.data.guest;

import hcmute.kltn.vtv.model.dto.user.CommentDTO;
import hcmute.kltn.vtv.model.entity.user.Comment;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListCommentResponse extends ResponseAbstract {

    private int count;
    private UUID reviewId;
    private List<CommentDTO> commentDTOs;

    public static ListCommentResponse listCommentResponse(List<Comment> comments, UUID reviewId, String message, String status) {
        ListCommentResponse listCommentResponse = new ListCommentResponse();
        listCommentResponse.setReviewId(reviewId);
        listCommentResponse.setCount(comments.size());
        listCommentResponse.setCommentDTOs(CommentDTO.convertEntitiesToDTOs(comments));
        listCommentResponse.setMessage(message);
        listCommentResponse.setStatus(status);
        listCommentResponse.setCode(200);

        return listCommentResponse;
    }
}
