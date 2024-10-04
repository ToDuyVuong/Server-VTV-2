package hcmute.kltn.vtv.controller.shpping;


import hcmute.kltn.vtv.model.data.vtv.response.StatisticsTransportsResponse;
import hcmute.kltn.vtv.service.shipping.ITransportStatistics;
import hcmute.kltn.vtv.service.vtv.IDateService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/shipping/manager/statistics")
@RequiredArgsConstructor
public class TransportStatisticsController {

    private final ITransportStatistics transportStatistics;
    private final IDateService dateService;


    @GetMapping("/revenue")
    public ResponseEntity<StatisticsTransportsResponse> statisticsTransportsByDateAndUsername(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        dateService.checkDatesRequest(startDate, endDate, 31);

        return ResponseEntity.ok(transportStatistics.statisticsTransportsByDateAndUsername(startDate, endDate, username));
    }


}
