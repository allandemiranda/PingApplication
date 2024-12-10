package com.byborgenterprises.enums;

import com.byborgenterprises.factories.ResponseFactory;
import com.byborgenterprises.requests.PingRequest;
import com.byborgenterprises.requests.ReportRequest;

/**
 * The ResponseStatus enum represents possible statuses for responses from the controller in the application.
 * It is used in classes such as {@link ReportRequest}, {@link PingRequest}, and {@link ResponseFactory}
 * to indicate the outcome of operations.
 */
public enum ResponseStatus {

  /**
   * Indicates that the operation was successful.
   * This request suppose return an object answer.
   */
  OK,

  /**
   * Indicates that the request was invalid or malformed, maybe an input that not exist.
   * Can return a message.
   */
  BAD_REQUEST,

  /**
   * Indicates that an internal server error occurred during the operation.
   * This is critical. This error return the internal exception for debug.
   */
  INTERNAL_SERVER_ERROR,

  /**
   * Indicates that the service is currently unavailable.
   * Maybe you are requesting an endpoint on controller that is using an external
   * API and the external API is unavailable.
   */
  SERVICE_UNAVAILABLE
}
