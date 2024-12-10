package com.byborgenterprises.providers;

import com.byborgenterprises.dtos.TerminalDto;
import com.byborgenterprises.dtos.TraceRouteDto;
import com.byborgenterprises.embeddables.Terminal;
import com.byborgenterprises.entities.TraceRoute;
import com.byborgenterprises.enums.OperatingSystem;
import com.byborgenterprises.mappers.TerminalMapper;
import com.byborgenterprises.mappers.TraceRouteMapper;
import com.byborgenterprises.repositories.TraceRouteRepository;
import com.byborgenterprises.services.TraceRouteService;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * The TraceRouteProvider class implements {@link TraceRouteService} to manage
 * traceroute operations. It interacts with repositories and mappers to handle
 * the creation, update, and retrieval of traceroute data.
 *
 * <p>This class encapsulates the logic for processing traceroute operations,
 * including constructing terminal commands, evaluating execution success, and
 * transforming data between entity and DTO representations.
 */
@RequiredArgsConstructor
@Getter(AccessLevel.PRIVATE)
public class TraceRouteProvider implements TraceRouteService {

  private static final String HOST_TARGET = "HOST";
  private static final String WINDOWS_CMD_EXE = "cmd.exe /c ";
  private static final String PORT_TARGET = ":";
  private final TraceRouteRepository traceRouteRepository;
  private final TraceRouteMapper traceRouteMapper;
  private final TerminalMapper terminalMapper;
  private final String tracerouteCommandWindows;
  private final String tracerouteCommandLinux;

  /**
   * Creates a {@link TraceRoute} entity with the given details.
   *
   * @param host the hostname or IP address.
   * @param terminal the terminal execution details.
   * @return a {@link TraceRoute} entity.
   */
  @NonNull
  private static TraceRoute getTraceRoute(final String host, final Terminal terminal) {
    TraceRoute traceRoute = new TraceRoute(host, terminal);
    traceRoute.setSuccess(terminal.getExitCode() == 0);
    return traceRoute;
  }

  @Override
  public @NonNull String getTerminalCommand(@NonNull String host, @NonNull OperatingSystem operatingSystem) {
    String hostFiltrated = host.contains(PORT_TARGET) ? host.substring(0, host.indexOf(PORT_TARGET)) : host;
    return switch (operatingSystem) {
      case LINUX -> this.getTracerouteCommandLinux().replace(HOST_TARGET, hostFiltrated);
      case WINDOWS -> WINDOWS_CMD_EXE.concat(this.getTracerouteCommandWindows().replace(HOST_TARGET, hostFiltrated));
    };
  }

  @Override
  public @NonNull TraceRouteDto createOrUpdateTraceRoute(@NonNull String host, @NonNull TerminalDto terminalDto, @NonNull OperatingSystem os) {
    Terminal terminal = this.getTerminalMapper().toEntity(terminalDto);
    TraceRoute traceRoute = getTraceRoute(host, terminal);
    TraceRoute save = this.getTraceRouteRepository().save(traceRoute);
    return this.getTraceRouteMapper().toDto(save);
  }

  @Override
  public @NonNull Optional<TraceRouteDto> getTraceRoute(@NonNull String host) {
    return this.getTraceRouteRepository().findById(host).map(traceRoute -> this.getTraceRouteMapper().toDto(traceRoute));
  }
}
