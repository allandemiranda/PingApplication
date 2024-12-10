package com.byborgenterprises.entities;

import com.byborgenterprises.annotations.Id;
import java.io.Serial;
import java.io.Serializable;
import java.net.URI;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NonNull;

/**
 * The PingTcpIp class represents the details of a TCP/IP ping operation.
 * It includes information about the target URL, response details, and the result of the ping.
 *
 * <p>This class is serializable and intended to encapsulate TCP/IP ping results.
 */
@Data
public class PingTcpIp implements Serializable {

  @Serial
  private static final long serialVersionUID = 429647186299220142L;

  /**
   * The target {@link URI} associated with the TCP/IP ping operation.
   * This field is marked as the unique identifier with {@link com.byborgenterprises.annotations.Id}.
   */
  @Id
  @NonNull
  private URI url;

  /**
   * The HTTP response code returned by the TCP/IP ping operation.
   */
  private int responseCode;

  /**
   * The time taken for the response, in milliseconds.
   */
  private long responseTime;

  /**
   * The {@link LocalDateTime} when the TCP/IP ping operation was performed.
   */
  @NonNull
  private LocalDateTime time;

  /**
   * Indicates whether the TCP/IP ping operation was successful.
   */
  private boolean success;

}
