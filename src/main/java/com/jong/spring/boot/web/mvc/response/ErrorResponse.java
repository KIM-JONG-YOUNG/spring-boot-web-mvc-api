package com.jong.spring.boot.web.mvc.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record ErrorResponse(
    String path,
    String message,
    List<ValidError> validErrors,
    LocalDateTime timestamp
) {

  @Builder
  public record ValidError(
      String field,
      String message
  ) {}

}
