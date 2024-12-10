package com.byborgenterprises.services;

import com.byborgenterprises.dtos.PingTcpIpDto;
import java.util.Optional;
import lombok.NonNull;

/**
 * The PingTcpIpService interface defines operations for managing TCP/IP ping tasks.
 * It includes methods for creating or updating ping data and retrieving existing ping details.
 */
public interface PingTcpIpService {

  /**
   * Creates or updates the TCP/IP ping data for a specified host.
   *
   * @param host the target host for the TCP/IP ping.
   * @return the updated or newly created {@link PingTcpIpDto} containing the ping details.
   */
  @NonNull
  PingTcpIpDto createOrUpdatePing(@NonNull final String host);

  /**
   * Retrieves the TCP/IP ping data for a specified host, if available.
   *
   * @param host the target host for which to retrieve TCP/IP ping data.
   * @return an {@link Optional} containing the {@link PingTcpIpDto} if found, or empty if no data is available.
   */
  @NonNull
  Optional<PingTcpIpDto> getPing(@NonNull final String host);
}
