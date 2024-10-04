package hcmute.kltn.vtv.service.vtv.impl;


import hcmute.kltn.vtv.service.vtv.IDateService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DateServiceImpl implements IDateService{



    public static boolean isSameDate(Date date, LocalDateTime createdAt) {
        LocalDateTime localDateTime = convertDateToLocalDateTime(date);

        return createdAt.getYear() == localDateTime.getYear() &&
                createdAt.getMonthValue() == localDateTime.getMonthValue() &&
                createdAt.getDayOfMonth() == localDateTime.getDayOfMonth();
    }


    @Override
    public void checkDatesRequest(Date startDate, Date endDate, int maxDays) {
        startDate = formatStartOfDate(startDate);
        endDate = formatStartOfDate(endDate);
        long diff = endDate.getTime() - startDate.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
        if (diffDays < 0) {
            throw new BadRequestException("Ngày kết thúc không được nhỏ hơn ngày bắt đầu.");
        }


        if (diffDays > maxDays) {
            throw new BadRequestException("Khoảng thời gian không được lớn hơn " + maxDays + " ngày.");
        }
    }


    public static List<Date> getDatesBetween(Date startDate, Date endDate) {
        startDate = formatStartOfDate(startDate);
        endDate = formatEndOfDate(endDate);
        List<Date> datesInRange = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (!calendar.getTime().after(endDate)) {
            Date result = calendar.getTime();
            datesInRange.add(result);
            calendar.add(Calendar.DATE, 1);
        }

        return datesInRange;
    }


    public static Date formatStartOfDate(Date date) {
        Calendar calendar = formatCalendar(date, 0, 0, 0, 0);
        return calendar.getTime();
    }

    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime convertDateToLocalDateTimeStart(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    public static LocalDateTime convertDateToLocalDateTimeEnd(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().withHour(23).withMinute(59).withSecond(59).withNano(999);
    }


    public static Date formatMiddleOfDate(Date date) {
        Calendar calendar = formatCalendar(date, 12, 0, 0, 0);
        return calendar.getTime();
    }


    public static Date formatEndOfDate(Date date) {
        Calendar calendar = formatCalendar(date, 23, 59, 59, 999);
        return calendar.getTime();
    }





   // dd/MM/yyyy
    public static String formatStringDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
    }


    // Convert LocalDateTime to Date
    public static Date convertLocalDateTimeToDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }





    private static Calendar formatCalendar(Date date, int hour, int minute, int second, int millisecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
        return calendar;
    }
}
