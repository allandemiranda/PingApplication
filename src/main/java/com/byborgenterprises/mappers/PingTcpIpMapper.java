package com.byborgenterprises.mappers;

import com.byborgenterprises.dtos.PingTcpIpDto;
import com.byborgenterprises.entities.PingTcpIp;
import lombok.NonNull;

/**
 * The PingTcpIpMapper interface provides a method for mapping TCP/IP ping entities
 * to their corresponding Data Transfer Object (DTO) representations.
 */
public interface PingTcpIpMapper {

  /**
   * Converts a {@link PingTcpIp} entity to a {@link PingTcpIpDto}.
   *
   * @param pingTcpIp the TCP/IP ping entity to be converted.
   * @return the corresponding {@link PingTcpIpDto} representation of the entity.
   */
  @NonNull
  PingTcpIpDto toDto(@NonNull final PingTcpIp pingTcpIp);
}
