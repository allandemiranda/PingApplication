package com.byborgenterprises.factories;

import com.byborgenterprises.enums.ResponseStatus;
import com.byborgenterprises.requests.PingRequest;
import com.byborgenterprises.requests.ReportRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * The ResponseFactory class provides a standardized structure for encapsulating
 * responses across controllers and service interactions. It is a generic class
 * that pairs the response payload with a {@link ResponseStatus} and additional
 * contextual information like exceptions or messages.
 *
 * <p>Designed to streamline and unify response handling, this class is
 * heavily utilized in controllers such as {@link ReportRequest},
 * {@link PingRequest}, and {@link BatchJobsFactory}.
 *
 * @param <T> the type of the response payload.
 */
@Getter
@Builder
public class ResponseFactory<T> {

  /**
   * The response payload of type {@link T}.
   */
  private final T response;

  /**
   * The status of the response, indicating the result of the operation.
   */
  @NonNull
  private final ResponseStatus status;

  /**
   * An optional exception providing details of any error that occurred.
   */
  private final Exception exception;

  /**
   * An optional message providing additional context or details about the response.
   */
  private final String message;

}

