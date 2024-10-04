package hcmute.kltn.vtv.controller.user;


import hcmute.kltn.vtv.model.data.guest.ResponseClass;
import hcmute.kltn.vtv.model.data.user.response.SearchHistoryPageResponse;
import hcmute.kltn.vtv.model.data.user.response.SearchHistoryResponse;
import hcmute.kltn.vtv.service.user.ISearchHistoryService;
import hcmute.kltn.vtv.service.vtv.IPageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customer/search-history")
@RequiredArgsConstructor
public class SearchHistoryController {

    private final IPageService pageService;
    private final ISearchHistoryService searchHistoryService;

    @GetMapping("/get/page/{page}/size/{size}")
    public ResponseEntity<SearchHistoryPageResponse> getSearchHistoryByUsername(@PathVariable("page") int page,
                                                                                @PathVariable("size") int size,
                                                                                HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        pageService.validatePageNumberAndSize(page, size);

        return ResponseEntity.ok(searchHistoryService.getSearchHistoryByUsername(username, page, size));
    }


    @PostMapping("/add")
    public ResponseEntity<SearchHistoryResponse> addNewSearchHistory(@RequestBody String search,
                                                                     HttpServletRequest request) {
        String username = (String) request.getAttribute("username");

        return ResponseEntity.ok(searchHistoryService.addNewSearchHistory(username, search));
    }


    @DeleteMapping("/delete/{searchHistoryId}")
    public ResponseEntity<ResponseClass> deleteSearchHistoryById(@PathVariable("searchHistoryId") UUID searchHistoryId,
                                                                 HttpServletRequest request) {
        String username = (String) request.getAttribute("username");

        return ResponseEntity.ok(searchHistoryService.deleteSearchHistory(username, searchHistoryId));
    }


}
