package hcmute.kltn.vtv.service.user.impl;

import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.repository.user.OrderRepository;
import hcmute.kltn.vtv.service.vtv.IImageService;
import hcmute.kltn.vtv.service.vtv.INotificationService;
import hcmute.kltn.vtv.service.wallet.ILoyaltyPointService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.user.request.ReviewRequest;
import hcmute.kltn.vtv.model.data.user.response.ReviewResponse;
import hcmute.kltn.vtv.model.entity.user.OrderItem;
import hcmute.kltn.vtv.model.entity.user.Review;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.user.ReviewRepository;
import hcmute.kltn.vtv.service.user.*;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewCustomerServiceImpl implements IReviewCustomerService {

    private final ReviewRepository reviewRepository;
    private final ICustomerService customerService;
    private final IImageService imageService;
    private final IOrderItemService orderItemService;
    private final ILoyaltyPointService loyaltyPointService;
    private final INotificationService notificationService;
    private final OrderRepository orderRepository;


    @Override
    @Transactional
    public ReviewResponse addNewReview(ReviewRequest request, String username) {
        orderItemService.checkExistOrderItem(request.getOrderItemId());
        orderItemService.checkOrderCompletedBeforeReview(request.getOrderItemId());
        checkOrderItem(request.getOrderItemId());
        orderItemService.checkExistOrderItemByIdAndUsername(request.getOrderItemId(), username);
        checkTimeReview(request.getOrderItemId());

        Review review = createReviewByRequest(request, username);
        try {
            reviewRepository.save(review);
            loyaltyPointService.updatePointInLoyaltyPointByUsername(username, 200L, "REVIEW");

            // Thêm thông báo cho chủ cửa hàng
            notificationService.addNewNotification(
                    "Có đánh giá mới",
                    "Bạn có một đánh giá mới từ tài khoản " + username + " cho sản phẩm " + review.getProduct().getName() + " của đơn hàng: #" + review.getOrderItem().getOrder().getOrderId(),
                    review.getProduct().getShop().getCustomer().getUsername(),
                    "system",
                    "REVIEW"
            );

            return ReviewResponse.reviewResponse(review, "Thêm mới đánh giá thành công!", "Success");
        } catch (Exception e) {
            imageService.deleteImageInFirebase(review.getImage());
            throw new InternalServerErrorException("Thêm mới đánh giá thất bại!" + e.getMessage());
        }
    }


    @Override
    public ReviewResponse getReviewByOrderItemId(UUID orderItemId) {
        Review review = reviewRepository.findByOrderItemOrderItemId(orderItemId)
                .orElseThrow(() -> new NotFoundException("Đánh giá không tồn tại!"));

        return ReviewResponse.reviewResponse(review, "Lấy thông tin đánh giá thành công!", "OK");
    }


    @Override
    @Transactional
    public ReviewResponse deleteReview(UUID reviewId, String username) {
        checkExistReview(reviewId);

        Review review = checkDeleteReview(reviewId, username);
        review.setStatus(Status.DELETED);
        review.setUpdateAt(LocalDateTime.now());
        try {
            reviewRepository.save(review);

            return ReviewResponse.reviewResponse(review, "Xóa đánh giá thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Xóa đánh giá thất bại!");
        }
    }


    @Override
    public Review checkReviewRole(UUID reviewId, String username, boolean isShop) {
        if (isShop) {
            checkExistReviewByIdAndShopUsername(reviewId, username);
        } else {
            checkExistReviewByIdAndUsername(reviewId, username);
        }

        return reviewRepository.findByReviewIdAndStatus(reviewId, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Đánh giá không tồn tại"));
    }


    @Override
    public Review checkReview(UUID reviewId) {
        Review review = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đánh giá theo mã đánh giá!"));
        if (review.getStatus() == Status.DELETED) {
            throw new BadRequestException("Đánh giá này đã bị xóa!");
        }

        return review;
    }


    private Review createReviewByRequest(ReviewRequest request, String username) {
        OrderItem orderItem = orderItemService.getOrderItemById(request.getOrderItemId());
        Review review = new Review();
        review.setOrderItem(orderItem);
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setImage(request.isHasImage() ? imageService.uploadImageToFirebase(request.getImage()) : null);
        review.setStatus(Status.ACTIVE);
        review.setCustomer(customerService.getCustomerByUsername(username));
        review.setProduct(orderItem.getCart().getProductVariant().getProduct());
        review.setCreateAt(LocalDateTime.now());
        review.setUpdateAt(LocalDateTime.now());

        return review;
    }


    private Review checkDeleteReview(UUID reviewId, String username) {
        checkExistReviewByIdAndUsername(reviewId, username);

        return checkReview(reviewId);
    }


    private void checkOrderItem(UUID orderItemId) {
        if (reviewRepository.existsByOrderItemOrderItemId(orderItemId)) {
            throw new BadRequestException("Đã đánh giá sản phẩm này!");
        }
    }

    private void checkTimeReview(UUID orderItemId) {
        UUID orderId = orderItemService.getOrderItemById(orderItemId).getOrder().getOrderId();

        if (!orderRepository.existsByOrderIdAndUpdateAtAfterAndStatus(orderId, LocalDateTime.now().minusDays(7), OrderStatus.COMPLETED)) {
            throw new BadRequestException("Đã hết thời gian đánh giá sản phẩm này! Thời gian đánh giá chỉ trong vòng 7 ngày sau khi đơn hàng hoàn thành.");
        }
    }


    private void checkExistReview(UUID reviewId) {
        if (!reviewRepository.existsByReviewId(reviewId)) {
            throw new BadRequestException("Đánh giá không tồn tại!");
        }
    }


    private void checkExistReviewByIdAndUsername(UUID reviewId, String username) {
        if (!reviewRepository.existsByReviewIdAndCustomerUsername(reviewId, username)) {
            throw new BadRequestException("Bạn không có quyền truy cập đánh giá này!");
        }
    }


    private void checkExistReviewByIdAndShopUsername(UUID reviewId, String username) {
        if (!reviewRepository.existsByReviewIdAndProductShopCustomerUsername(reviewId, username)) {
            throw new BadRequestException("Bạn không phải chủ cửa hàng. Bạn không có quyền trả lời đánh giá này.");
        }
    }


}
