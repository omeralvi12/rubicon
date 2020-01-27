package com.rubicon.water.order.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static Date getEndDate(Date startDate, int duration) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(startDate);
	    calendar.add(Calendar.HOUR_OF_DAY, duration);
	    return calendar.getTime();
	}
}
