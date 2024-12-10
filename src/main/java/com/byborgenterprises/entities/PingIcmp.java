package com.byborgenterprises.entities;

import com.byborgenterprises.annotations.Id;
import com.byborgenterprises.embeddables.Terminal;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.NonNull;

/**
 * The PingIcmp class represents the details of an ICMP ping operation.
 * It includes information about the host, terminal execution details, and the result of the ping.
 *
 * <p>This class is serializable and intended to encapsulate ICMP ping results.
 */
@Data
public class PingIcmp implements Serializable {

  @Serial
  private static final long serialVersionUID = -1835936303993406763L;

  /**
   * The hostname or IP address associated with the ICMP ping operation.
   * This field is marked as the unique identifier with {@link com.byborgenterprises.annotations.Id}.
   */
  @Id
  @NonNull
  private String host;

  /**
   * The {@link Terminal} containing the details of the terminal execution for the ping.
   */
  @NonNull
  private Terminal terminal;

  /**
   * Indicates whether the ICMP ping operation was successful.
   */
  private boolean success;

}
