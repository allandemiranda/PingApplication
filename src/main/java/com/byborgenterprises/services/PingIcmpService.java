package com.byborgenterprises.services;

import com.byborgenterprises.dtos.PingIcmpDto;
import com.byborgenterprises.dtos.TerminalDto;
import com.byborgenterprises.enums.OperatingSystem;
import java.util.Optional;
import lombok.NonNull;

/**
 * The PingIcmpService interface defines operations for managing ICMP ping tasks.
 * It includes methods for generating terminal commands, creating or updating
 * ping data, and retrieving existing ping details.
 */
public interface PingIcmpService {

  /**
   * Generates a terminal command for performing an ICMP ping based on the given host
   * and operating system.
   *
   * @param host the target host for the ICMP ping.
   * @param operatingSystem the operating system for which the command is generated.
   * @return the terminal command as a string.
   */
  @NonNull
  String getTerminalCommand(@NonNull final String host, @NonNull final OperatingSystem operatingSystem);

  /**
   * Creates or updates the ICMP ping data for a specified host.
   *
   * @param host the target host for the ICMP ping.
   * @param terminalDto the terminal details for executing the ICMP ping.
   * @param os the operating system used for the ICMP ping.
   * @return the updated or newly created {@link PingIcmpDto} containing the ping details.
   */
  @NonNull
  PingIcmpDto createOrUpdatePing(@NonNull final String host, @NonNull final TerminalDto terminalDto, @NonNull final OperatingSystem os);

  /**
   * Retrieves the ICMP ping data for a specified host, if available.
   *
   * @param host the target host for which to retrieve ICMP ping data.
   * @return an {@link Optional} containing the {@link PingIcmpDto} if found, or empty if no data is available.
   */
  @NonNull
  Optional<PingIcmpDto> getPing(@NonNull final String host);
}
