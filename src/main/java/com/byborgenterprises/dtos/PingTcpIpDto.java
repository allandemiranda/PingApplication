package com.byborgenterprises.dtos;

import java.io.Serial;
import java.io.Serializable;
import java.net.URI;
import java.time.LocalDateTime;
import lombok.NonNull;

/**
 * The PingTcpIpDto record encapsulates the details of a TCP/IP ping operation.
 * It includes information about the target URL, response details, and the result of the ping.
 *
 * <p>This record is immutable and implements {@link Serializable} for data transfer.
 *
 * @param url the target {@link URI} that was pinged.
 * @param responseCode the HTTP response code returned by the ping.
 * @param responseTime the time taken for the response, in milliseconds.
 * @param time the {@link LocalDateTime} when the ping was performed.
 * @param success a boolean indicating whether the ping operation was successful.
 */
public record PingTcpIpDto(@NonNull URI url, int responseCode, long responseTime, @NonNull LocalDateTime time, boolean success) implements Serializable {

  @Serial
  private static final long serialVersionUID = -3491549973406788517L;
}
