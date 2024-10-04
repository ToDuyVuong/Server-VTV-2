package hcmute.kltn.vtv.service.shipping;

import hcmute.kltn.vtv.model.data.vtv.response.StatisticsTransportsResponse;
import hcmute.kltn.vtv.model.extra.TransportStatus;

import java.util.Date;

public interface ITransportStatistics {
    StatisticsTransportsResponse statisticsTransportsByDateAndUsername( Date startDate, Date endDate, String username);
}
