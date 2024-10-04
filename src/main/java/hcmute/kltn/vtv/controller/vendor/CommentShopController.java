package hcmute.kltn.vtv.controller.vendor;

import hcmute.kltn.vtv.model.data.user.request.CommentRequest;
import hcmute.kltn.vtv.model.data.user.response.CommentResponse;
import hcmute.kltn.vtv.service.user.ICommentCustomerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vendor/comment")
@RequiredArgsConstructor
public class CommentShopController {

    private final ICommentCustomerService commentCustomerService;

    @PostMapping("/add")
    public ResponseEntity<CommentResponse> addNewCommentByShop(@RequestBody CommentRequest request,
                                                               HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        request.setUsername(username);
        request.setShop(true);
        request.validate();
        return ResponseEntity.ok(commentCustomerService.addNewCommentByCustomer(request));
    }
}
