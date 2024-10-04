package hcmute.kltn.vtv.controller.shpping;


import hcmute.kltn.vtv.model.data.shipping.response.TransportPageResponse;
import hcmute.kltn.vtv.model.extra.TransportStatus;
import hcmute.kltn.vtv.service.shipping.IManagerTransport;
import hcmute.kltn.vtv.service.vtv.IDateService;
import hcmute.kltn.vtv.service.vtv.IPageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/shipping/manager/transport")
@RequiredArgsConstructor
public class ManagerTransportController {
    private final IDateService dateService;
    private final IPageService pageService;
    private final IManagerTransport managerTransport;

    @GetMapping("/page/status/{status}/date")
    public ResponseEntity<TransportPageResponse> getTransportPageByManagerAndDate(@PathVariable TransportStatus status,
                                                                                  @RequestParam int page, @RequestParam int size,
                                                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
                                                                                  HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        dateService.checkDatesRequest(startDate, endDate, 31);
        pageService.validatePageNumberAndSize(page, size);

        return ResponseEntity.ok(managerTransport.getTransportPageByManagerAndDate(username, page, size, status, startDate, endDate));
    }

}
