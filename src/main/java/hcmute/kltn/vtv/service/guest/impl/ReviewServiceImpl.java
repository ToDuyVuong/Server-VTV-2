package hcmute.kltn.vtv.service.guest.impl;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.guest.ListReviewResponse;
import hcmute.kltn.vtv.model.data.user.response.ReviewResponse;
import hcmute.kltn.vtv.model.entity.user.Review;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.user.ReviewRepository;
import hcmute.kltn.vtv.service.guest.IReviewService;
import hcmute.kltn.vtv.service.user.IReviewCustomerService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final IReviewCustomerService reviewCustomerService;

    @Override
    public ReviewResponse getReviewDetailById(UUID reviewId) {
        Review review = reviewCustomerService.checkReview(reviewId);

        return ReviewResponse.reviewResponse(review, "Lấy thông tin đánh giá thành công!", "OK");
    }

    @Override
    public ListReviewResponse getReviewsByProductId(Long productId) {

        List<Review> reviews = reviewRepository.findAllByProductProductIdAndStatus(productId, Status.ACTIVE)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy đánh giá nào!"));

        return ListReviewResponse.listReviewResponse(reviews, "Lấy danh sách đánh giá thành công!", productId, averageRating(reviews));
    }

    @Override
    public ListReviewResponse getReviewsByProductIdAndRating(Long productId, int rating) {

        List<Review> reviews = reviewRepository
                .findAllByProductProductIdAndRatingAndStatus(productId, rating, Status.ACTIVE)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy đánh giá nào!"));

        return ListReviewResponse.listReviewResponse(reviews, "Lấy danh sách đánh giá theo xếp hạng thành công!", productId, averageRating(reviews));
    }

    @Override
    public ListReviewResponse getReviewsByProductIdAndImageNotNull(Long productId) {

        List<Review> reviews = reviewRepository.findAllByProductProductIdAndImageNotNull(productId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy đánh giá nào!"));

        return ListReviewResponse.listReviewResponse(reviews, "Lấy danh sách đánh giá có hình ảnh thành công!", productId, averageRating(reviews));
    }

    @Override
    public int countReviewByProductId(Long productId) {
        return reviewRepository.countByProductProductId(productId);
    }

    @Override
    public float countAverageRatingByProductId(Long productId) {

        List<Review> reviews = reviewRepository.findAllByProductProductIdAndImageNotNull(productId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy đánh giá nào!"));

        return averageRating(reviews);
    }


    @Override
    public float averageRatingByShopId(Long shopId) {
        List<Review> reviews = getReviewsByShopId(shopId);

        return averageRatingShop(reviews);
    }



    private List<Review> getReviewsByShopId(Long shopId) {
        return reviewRepository.findAllByProductShopShopId(shopId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đánh giá nào!"));
    }

    private float averageRatingShop(List<Review> reviews) {
        //return after ',' the first 2 digits
        return (float) (Math.round((float) averageRating(reviews) * 100.0) / 100.0);
    }


    private long averageRating(List<Review> reviews) {
        long sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        return !reviews.isEmpty() ? sum / reviews.size() : 0;
    }



}
