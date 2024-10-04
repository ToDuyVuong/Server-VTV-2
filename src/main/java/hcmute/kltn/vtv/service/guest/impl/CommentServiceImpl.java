package hcmute.kltn.vtv.service.guest.impl;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.guest.ListCommentResponse;
import hcmute.kltn.vtv.model.dto.user.CommentDTO;
import hcmute.kltn.vtv.model.entity.user.Comment;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.user.CommentRepository;
import hcmute.kltn.vtv.service.guest.ICommentService;
import hcmute.kltn.vtv.service.user.IReviewCustomerService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements ICommentService {

    private final CommentRepository commentRepository;
    private final IReviewCustomerService reviewCustomerService;

    @Override
    public List<CommentDTO> getListCommentDTO(UUID reviewId) {
        reviewCustomerService.checkReview(reviewId);

        List<Comment> comments = getCommentsByReviewIdAndStatus(reviewId, Status.ACTIVE);

        return CommentDTO.convertEntitiesToDTOs(comments);
    }

    @Override
    public ListCommentResponse getCommentsByReviewId(UUID reviewId) {
        reviewCustomerService.checkReview(reviewId);

        List<Comment> comments = getCommentsByReviewIdAndStatus(reviewId, Status.ACTIVE);

        return ListCommentResponse.listCommentResponse(comments, reviewId,
                "Lấy danh sách bình luận của đánh giá theo mã đánh giá thành công.", "OK");
    }


    private List<Comment> getCommentsByReviewIdAndStatus(UUID reviewId, Status status) {
        return commentRepository.findAllByReviewReviewIdAndStatus(reviewId, status)
                .orElseThrow(() -> new NotFoundException("Không có bình luận nào cho đánh giá này."));
    }



}
