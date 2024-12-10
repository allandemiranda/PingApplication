package com.byborgenterprises.assemblers;

import com.byborgenterprises.dtos.PingTcpIpDto;
import com.byborgenterprises.entities.PingTcpIp;
import com.byborgenterprises.mappers.PingTcpIpMapper;
import lombok.NonNull;

/**
 * The PingTcpIpMapperAssembler class implements {@link PingTcpIpMapper} to provide
 * mapping logic between {@link PingTcpIp} entities and {@link PingTcpIpDto} data transfer objects.
 *
 * <p>This class ensures the transformation of TCP/IP ping entities into their DTO
 * representations, allowing for standardized data handling across the application.
 *
 * <p>Designed to streamline the mapping process, this assembler encapsulates
 * the conversion logic, separating it from business-layer concerns.
 */
public class PingTcpIpMapperAssembler implements PingTcpIpMapper {

  @Override
  public @NonNull PingTcpIpDto toDto(@NonNull PingTcpIp pingTcpIp) {
    return new PingTcpIpDto(pingTcpIp.getUrl(), pingTcpIp.getResponseCode(), pingTcpIp.getResponseTime(), pingTcpIp.getTime(), pingTcpIp.isSuccess());
  }
}
