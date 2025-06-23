package com.jong.spring.boot.web.mvc.config;

import com.jong.spring.boot.web.mvc.constants.DateTimeFormats;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(String.class, LocalTime.class,
        source -> LocalTime.parse(source, DateTimeFormats.TIME_FORMATTER));
    registry.addConverter(String.class, LocalDate.class,
        source -> LocalDate.parse(source, DateTimeFormats.DATE_FORMATTER));
    registry.addConverter(String.class, LocalDateTime.class,
        source -> LocalDateTime.parse(source, DateTimeFormats.DATETIME_FORMATTER));
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOriginPatterns("*")
        .allowedMethods("*")
        .allowedHeaders("*")
        .exposedHeaders("*");
  }



}
