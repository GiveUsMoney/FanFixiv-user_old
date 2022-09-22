package com.fanfixiv.auth.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class TimeProvider {

  private static Calendar cal = Calendar.getInstance();

  public static LocalDateTime getTimeAfter3min() {
    cal.setTime(new Date());
    cal.add(Calendar.MINUTE, 3);
    return cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  public static LocalDateTime getTimeAfter1hour() {
    cal.setTime(new Date());
    cal.add(Calendar.HOUR, 1);
    return cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }
}
