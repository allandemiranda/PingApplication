package com.byborgenterprises.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * The HttpMethod enum represents common HTTP methods.
 * Each enum constant is associated with a string value representing
 * the HTTP method.
 */
@Getter
@RequiredArgsConstructor
public enum HttpMethod {

  /**
   * Represents an HTTP GET request.
   */
  GET("GET"),

  /**
   * Represents an HTTP POST request.
   */
  POST("POST"),

  /**
   * Represents an HTTP PUT request.
   */
  PUT("PUT"),

  /**
   * Represents an HTTP DELETE request.
   */
  DELETE("DELETE"),

  /**
   * Represents an HTTP PATCH request.
   */
  PATCH("PATCH");

  /**
   * The string value of the HTTP method.
   */
  @NonNull
  private final String methodValue;
}
