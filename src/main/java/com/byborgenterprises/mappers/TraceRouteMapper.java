package com.byborgenterprises.mappers;

import com.byborgenterprises.dtos.TraceRouteDto;
import com.byborgenterprises.entities.TraceRoute;
import lombok.NonNull;

/**
 * The TraceRouteMapper interface provides a method for mapping traceroute entities
 * to their corresponding Data Transfer Object (DTO) representations.
 */
public interface TraceRouteMapper {

  /**
   * Converts a {@link TraceRoute} entity to a {@link TraceRouteDto}.
   *
   * @param traceRoute the traceroute entity to be converted.
   * @return the corresponding {@link TraceRouteDto} representation of the entity.
   */
  @NonNull
  TraceRouteDto toDto(@NonNull final TraceRoute traceRoute);
}
