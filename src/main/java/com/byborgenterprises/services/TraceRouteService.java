package com.byborgenterprises.services;

import com.byborgenterprises.dtos.TerminalDto;
import com.byborgenterprises.dtos.TraceRouteDto;
import com.byborgenterprises.enums.OperatingSystem;
import java.util.Optional;
import lombok.NonNull;

/**
 * The TraceRouteService interface defines operations for managing traceroute tasks.
 * It includes methods for generating terminal commands, creating or updating
 * traceroute data, and retrieving existing traceroute details.
 */
public interface TraceRouteService {

  /**
   * Generates a terminal command for performing a traceroute based on the given host
   * and operating system.
   *
   * @param host the target host for the traceroute.
   * @param operatingSystem the operating system for which the command is generated.
   * @return the terminal command as a string.
   */
  @NonNull
  String getTerminalCommand(@NonNull final String host, @NonNull OperatingSystem operatingSystem);

  /**
   * Creates or updates the traceroute data for a specified host.
   *
   * @param host the target host for the traceroute.
   * @param terminalDto the terminal details for executing the traceroute.
   * @param os the operating system used for the traceroute.
   * @return the updated or newly created {@link TraceRouteDto} containing the traceroute details.
   */
  @NonNull
  TraceRouteDto createOrUpdateTraceRoute(@NonNull final String host, @NonNull final TerminalDto terminalDto, @NonNull final OperatingSystem os);

  /**
   * Retrieves the traceroute data for a specified host, if available.
   *
   * @param host the target host for which to retrieve traceroute data.
   * @return an {@link Optional} containing the {@link TraceRouteDto} if found, or empty if no data is available.
   */
  @NonNull
  Optional<TraceRouteDto> getTraceRoute(@NonNull final String host);
}
