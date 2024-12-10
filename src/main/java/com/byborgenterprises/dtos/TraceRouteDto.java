package com.byborgenterprises.dtos;

import java.io.Serial;
import java.io.Serializable;
import lombok.NonNull;

/**
 * The TraceRouteDto record encapsulates the details of a traceroute operation.
 * It includes information about the host, terminal execution details, and the result of the traceroute.
 *
 * <p>This record is immutable and implements {@link Serializable} for data transfer.
 *
 * @param host the hostname or IP address where the traceroute was performed.
 * @param terminal the {@link TerminalDto} containing terminal execution details.
 * @param success a boolean indicating whether the traceroute operation was successful.
 */
public record TraceRouteDto(@NonNull String host, @NonNull TerminalDto terminal, boolean success) implements Serializable {

  @Serial
  private static final long serialVersionUID = 6953704549704662216L;

}
