package com.jong.spring.boot.web.mvc.constants;

import java.time.format.DateTimeFormatter;

public final class DateTimeFormats {

  public static final String TIME_FORMAT = "HH:mm:ss";
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

  public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);
  public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
  public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
  public static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT);

}
