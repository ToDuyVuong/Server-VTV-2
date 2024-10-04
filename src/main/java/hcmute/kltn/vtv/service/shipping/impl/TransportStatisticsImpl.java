package hcmute.kltn.vtv.service.shipping.impl;

import hcmute.kltn.vtv.model.data.vtv.response.StatisticsTransportsResponse;
import hcmute.kltn.vtv.model.entity.shipping.Deliver;
import hcmute.kltn.vtv.model.entity.shipping.TransportProvider;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.model.extra.TransportStatus;
import hcmute.kltn.vtv.model.extra.TypeWork;
import hcmute.kltn.vtv.repository.shipping.TransportRepository;
import hcmute.kltn.vtv.repository.user.OrderRepository;
import hcmute.kltn.vtv.service.shipping.IDeliverService;
import hcmute.kltn.vtv.service.shipping.ITransportProviderService;
import hcmute.kltn.vtv.service.shipping.ITransportStatistics;
import hcmute.kltn.vtv.service.vtv.impl.DateServiceImpl;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransportStatisticsImpl implements ITransportStatistics {

    private final IDeliverService deliverService;
    private final ITransportProviderService transportProviderService;
    private final TransportRepository transportRepository;
    private final OrderRepository orderRepository;



    @Override
    public StatisticsTransportsResponse statisticsTransportsByDateAndUsername(Date startDate, Date endDate, String username) {

        Deliver deliver = deliverService.getDeliverByUsername(username);
        deliverService.checkTypeWorkWithManager(deliver);



        List<Order> orders = orderRepository
                .findAllByStatusAndShippingMethodAndOrderDateBetween(OrderStatus.COMPLETED, deliver.getTransportProvider().getShortName(), startDate, endDate)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào vận chuyển bởi " + deliver.getTransportProvider().getShortName() + "."));
        String message = "Thống kê vận chuyển từ ngày " + DateServiceImpl.formatStringDate(startDate)
                + " đến ngày " + DateServiceImpl.formatStringDate(endDate) + " thành công.";

        return StatisticsTransportsResponse.statisticsTransportsResponseByOrders(orders, startDate, endDate, message);
    }



}
