package com.byborgenterprises.dtos;

import java.io.Serial;
import java.io.Serializable;
import lombok.NonNull;

/**
 * The PingIcmpDto record encapsulates the details of an ICMP ping operation.
 * It includes information about the host, terminal details, and the result of the ping.
 *
 * <p>This record is immutable and implements {@link Serializable} for data transfer.
 *
 * @param host the hostname or IP address that was pinged.
 * @param terminal the {@link TerminalDto} containing terminal execution details.
 * @param success a boolean indicating whether the ping operation was successful.
 */
public record PingIcmpDto(@NonNull String host, @NonNull TerminalDto terminal, boolean success) implements Serializable {

  @Serial
  private static final long serialVersionUID = -4084948623290952279L;

}
