package hcmute.kltn.vtv.controller.guest;

import hcmute.kltn.vtv.model.data.guest.ListCommentResponse;
import hcmute.kltn.vtv.service.guest.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final ICommentService commentService;

    @GetMapping("/get/{reviewId}")
    public ResponseEntity<ListCommentResponse> getCommentsByReviewId(@PathVariable UUID reviewId) {

        return ResponseEntity.ok(commentService.getCommentsByReviewId(reviewId));
    }

}
