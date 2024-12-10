package com.byborgenterprises.mappers;

import com.byborgenterprises.dtos.PingIcmpDto;
import com.byborgenterprises.entities.PingIcmp;
import lombok.NonNull;

/**
 * The PingIcmpMapper interface provides a method for mapping ICMP ping entities
 * to their corresponding Data Transfer Object (DTO) representations.
 */
public interface PingIcmpMapper {

  /**
   * Converts a {@link PingIcmp} entity to a {@link PingIcmpDto}.
   *
   * @param pingIcmp the ICMP ping entity to be converted.
   * @return the corresponding {@link PingIcmpDto} representation of the entity.
   */
  @NonNull
  PingIcmpDto toDto(@NonNull final PingIcmp pingIcmp);
}
