package com.example.RoomReservationSystem.helper;
import java.text.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Compare {
    public static String compareDates(Date startDate, Date endDate) throws ParseException {
        if (startDate == null || endDate == null) {
            return "null";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        System.out.println("The start date is: " + simpleDateFormat.format(startDate));
        System.out.println("The end date is: " + simpleDateFormat.format(endDate));
        if(startDate.compareTo(endDate) > 0) {
            System.out.println("start occurs after end");
            return "false";
        } else if(startDate.compareTo(endDate) < 0) {
            System.out.println("start occurs before end");
            return "true";
        } else if(startDate.compareTo(endDate) == 0) {
            System.out.println("Both dates are equal");
            return "equal";
        }
        return "none";
    }

    public static Long subtractDates(Date startDate, Date endDate) throws ParseException {
        if (startDate == null || endDate == null) {
            return 4L; //or maybe return null
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        System.out.println("The start date is: " + simpleDateFormat.format(startDate));
        System.out.println("The end date is: " + simpleDateFormat.format(endDate));
        long diff = endDate.getTime() - startDate.getTime();
        TimeUnit timeUnit = TimeUnit.HOURS;
        long difference = timeUnit.convert(diff, TimeUnit.MILLISECONDS);
        return difference;
    }
}
