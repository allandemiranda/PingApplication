package com.byborgenterprises.entities;

import com.byborgenterprises.annotations.Id;
import com.byborgenterprises.embeddables.Terminal;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.NonNull;

/**
 * The TraceRoute class represents the details of a traceroute operation.
 * It includes information about the target host, execution details, and the result of the operation.
 *
 * <p>This class is serializable and intended to encapsulate traceroute results.
 */
@Data
public class TraceRoute implements Serializable {

  @Serial
  private static final long serialVersionUID = 1234567890123456789L;

  /**
   * The hostname or IP address associated with the traceroute operation.
   * This field is marked as the unique identifier with {@link com.byborgenterprises.annotations.Id}.
   */
  @Id
  @NonNull
  private String host;

  /**
   * The {@link Terminal} containing the details of the terminal execution for the traceroute.
   */
  @NonNull
  private Terminal terminal;

  /**
   * Indicates whether the traceroute operation was successful.
   */
  private boolean success;

}

