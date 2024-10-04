package hcmute.kltn.vtv.service.vtv;

import java.util.Date;

public interface IDateService {

    void checkDatesRequest(Date startDate, Date endDate, int maxDays);
}
