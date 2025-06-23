package com.jong.spring.boot.web.mvc.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
  ) throws ServletException, IOException {

    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

    filterChain.doFilter(requestWrapper, responseWrapper);

    Map<String, List<String>> requestHeaders = Collections.list(requestWrapper.getHeaderNames()).stream()
        .collect(Collectors.toMap(name -> name, name -> Collections.list(requestWrapper.getHeaders(name))));
    Map<String, List<String>> responseHeaders = responseWrapper.getHeaderNames().stream()
        .collect(Collectors.toMap(name -> name, name -> responseWrapper.getHeaders(name).stream().toList()));

    String requestBody = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
    String responseBody = new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);

    log.info("""
            === Request Info ==============================
              - URL       : [{}] {}
              - Query     : {}
              - Headers   : {}
              - Parameters: {}
              - Body      : {}
            === Response Info ==============================
              - Status    : {}
              - Headers   : {}
              - Body      : {}
            """,
        requestWrapper.getMethod(),
        requestWrapper.getRequestURI(),
        requestWrapper.getQueryString(),
        requestHeaders,
        requestWrapper.getParameterMap(),
        requestBody,
        responseWrapper.getStatus(),
        responseHeaders,
        responseBody);

    responseWrapper.copyBodyToResponse();
  }

}
