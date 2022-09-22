package com.fanfixiv.auth.utils;

import java.util.Calendar;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class TimeProvider {

  private static Calendar cal = Calendar.getInstance();

  public static Date getTimeAfter3min() {
    cal.setTime(new Date());
    cal.add(Calendar.MINUTE, 3);
    return cal.getTime();
  }

  public static Date getTimeAfter1hour() {
    cal.setTime(new Date());
    cal.add(Calendar.HOUR, 1);
    return cal.getTime();
  }

  public static Date getTimeAfter14day() {
    cal.setTime(new Date());
    cal.add(Calendar.DAY_OF_WEEK, 14);
    return cal.getTime();
  }
}
