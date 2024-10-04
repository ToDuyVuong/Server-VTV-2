package hcmute.kltn.vtv.service.shipping.impl;

import hcmute.kltn.vtv.model.data.shipping.response.TransportPageResponse;
import hcmute.kltn.vtv.model.entity.shipping.Deliver;
import hcmute.kltn.vtv.model.entity.shipping.Transport;
import hcmute.kltn.vtv.model.extra.TransportStatus;
import hcmute.kltn.vtv.repository.shipping.TransportRepository;
import hcmute.kltn.vtv.repository.user.OrderRepository;
import hcmute.kltn.vtv.service.shipping.IDeliverService;
import hcmute.kltn.vtv.service.shipping.IManagerTransport;
import hcmute.kltn.vtv.service.shipping.ITransportProviderService;
import hcmute.kltn.vtv.service.vtv.impl.DateServiceImpl;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ManagerTransportImpl implements IManagerTransport {


    private final IDeliverService deliverService;
    private final ITransportProviderService transportProviderService;
    private final TransportRepository transportRepository;
    private final OrderRepository orderRepository;


    @Override
    public TransportPageResponse getTransportPageByManagerAndDate(String username, int page, int size,
                                                                  TransportStatus status, Date startDate, Date endDate) {
        Deliver deliver = deliverService.getDeliverByUsername(username);
        deliverService.checkTypeWorkWithManager(deliver);

        LocalDateTime start = DateServiceImpl.convertDateToLocalDateTimeStart(startDate);
        LocalDateTime end = DateServiceImpl.convertDateToLocalDateTimeEnd(endDate);

        Page<Transport> transports = transportRepository.findAllByShippingMethodAndStatusAndCreateAtBetween(
                        deliver.getTransportProvider().getShortName(), status, start, end, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào vận chuyển bởi "
                        + deliver.getTransportProvider().getShortName() + " và trong khoảng thời gian từ " + startDate
                        + " đến " + endDate + " với trạng thái " + status + "."));

        String message = "Thống kê vận chuyển từ ngày " + startDate + " đến ngày " + endDate + " và trạng thái "
                + status + " thành công.";

        return TransportPageResponse.transportPageResponse(transports, message);

    }

}
