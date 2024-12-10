package com.byborgenterprises.assemblers;

import com.byborgenterprises.dtos.TerminalDto;
import com.byborgenterprises.dtos.TraceRouteDto;
import com.byborgenterprises.embeddables.Terminal;
import com.byborgenterprises.entities.TraceRoute;
import com.byborgenterprises.mappers.TerminalMapper;
import com.byborgenterprises.mappers.TraceRouteMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * The TraceRouteMapperAssembler class implements {@link TraceRouteMapper} to provide
 * mapping logic between {@link TraceRoute} entities and {@link TraceRouteDto} data transfer objects.
 *
 * <p>This class leverages the {@link TerminalMapper} to map the {@link Terminal}
 * field to its corresponding {@link TerminalDto} representation.
 *
 * <p>By encapsulating the mapping logic, this assembler ensures consistent and reusable
 * transformations between entity and DTO layers, particularly for traceroute operations.
 */
@RequiredArgsConstructor
@Getter(AccessLevel.PRIVATE)
public class TraceRouteMapperAssembler implements TraceRouteMapper {

  private final TerminalMapper terminalMapper;

  @Override
  public @NonNull TraceRouteDto toDto(@NonNull TraceRoute traceRoute) {
    TerminalDto terminalDto = this.getTerminalMapper().toDto(traceRoute.getTerminal());
    return new TraceRouteDto(traceRoute.getHost(), terminalDto, traceRoute.isSuccess());
  }
}
