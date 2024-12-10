package com.byborgenterprises.requests;

import com.byborgenterprises.controllers.PingController;
import com.byborgenterprises.dtos.PingIcmpDto;
import com.byborgenterprises.dtos.PingTcpIpDto;
import com.byborgenterprises.dtos.TerminalDto;
import com.byborgenterprises.dtos.TraceRouteDto;
import com.byborgenterprises.enums.OperatingSystem;
import com.byborgenterprises.enums.ResponseStatus;
import com.byborgenterprises.factories.ResponseFactory;
import com.byborgenterprises.services.PingIcmpService;
import com.byborgenterprises.services.PingTcpIpService;
import com.byborgenterprises.services.TraceRouteService;
import com.byborgenterprises.utils.OperatingSystemTools;
import com.byborgenterprises.utils.TerminalTools;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * The PingRequest class implements {@link PingController} to handle ping and traceroute
 * operations via the services {@link PingIcmpService}, {@link PingTcpIpService}, and
 * {@link TraceRouteService}.
 *
 * <p>This class encapsulates the logic for executing and managing ICMP pings, TCP/IP pings,
 * and traceroutes. It interacts with terminal and operating system tools to construct
 * and execute commands, and standardizes responses using {@link ResponseFactory}.
 *
 */
@RequiredArgsConstructor
@Getter(AccessLevel.PRIVATE)
public class PingRequest implements PingController {

  private final PingIcmpService pingIcmpService;
  private final PingTcpIpService pingTcpIpService;
  private final TraceRouteService traceRouteService;

  @NonNull
  private static String getHostNotFoundMsg(final String host) {
    return "Host " + host + " not found on";
  }

  @Override
  public @NonNull ResponseFactory<PingIcmpDto> getIcmp(@NonNull String host) {
    return this.getPingIcmpService().getPing(host).map(pingIcmpDto -> ResponseFactory.<PingIcmpDto>builder().response(pingIcmpDto).status(ResponseStatus.OK).build())
        .orElse(ResponseFactory.<PingIcmpDto>builder().status(ResponseStatus.BAD_REQUEST).message(getHostNotFoundMsg(host) + " ICMP database").build());
  }

  @Override
  public @NonNull ResponseFactory<PingIcmpDto> postIcmp(@NonNull String host) {
    try {
      OperatingSystem operatingSystem = OperatingSystemTools.getOperatingSystem();
      String command = this.getPingIcmpService().getTerminalCommand(host, operatingSystem);
      TerminalDto terminalDto = TerminalTools.executeCommand(command);
      PingIcmpDto pingIcmpDto = this.getPingIcmpService().createOrUpdatePing(host, terminalDto, operatingSystem);
      return ResponseFactory.<PingIcmpDto>builder().response(pingIcmpDto).status(ResponseStatus.OK).build();
    } catch (Exception e) {
      return ResponseFactory.<PingIcmpDto>builder().status(ResponseStatus.INTERNAL_SERVER_ERROR).exception(e).build();
    }
  }

  @Override
  public @NonNull ResponseFactory<PingTcpIpDto> getTcp(@NonNull String host) {
    return this.getPingTcpIpService().getPing(host).map(pingTcpIpDto -> ResponseFactory.<PingTcpIpDto>builder().response(pingTcpIpDto).status(ResponseStatus.OK).build())
        .orElse(ResponseFactory.<PingTcpIpDto>builder().status(ResponseStatus.BAD_REQUEST).message(getHostNotFoundMsg(host) + " TCP/IP database").build());
  }

  @Override
  public @NonNull ResponseFactory<PingTcpIpDto> postTcp(@NonNull String host) {
    try {
      PingTcpIpDto pingTcpIpDto = this.getPingTcpIpService().createOrUpdatePing(host);
      return ResponseFactory.<PingTcpIpDto>builder().response(pingTcpIpDto).status(ResponseStatus.OK).build();
    } catch (Exception e) {
      return ResponseFactory.<PingTcpIpDto>builder().status(ResponseStatus.INTERNAL_SERVER_ERROR).exception(e).build();
    }
  }

  @Override
  public @NonNull ResponseFactory<TraceRouteDto> getTraceRoute(@NonNull String host) {
    return this.getTraceRouteService().getTraceRoute(host).map(traceRouteDto -> ResponseFactory.<TraceRouteDto>builder().response(traceRouteDto).status(ResponseStatus.OK).build())
        .orElse(ResponseFactory.<TraceRouteDto>builder().status(ResponseStatus.BAD_REQUEST).message(getHostNotFoundMsg(host) + " Trace Route database").build());
  }

  @Override
  public @NonNull ResponseFactory<TraceRouteDto> postTraceRoute(@NonNull String host) {
    try {
      OperatingSystem operatingSystem = OperatingSystemTools.getOperatingSystem();
      String command = this.getTraceRouteService().getTerminalCommand(host, operatingSystem);
      TerminalDto terminalDto = TerminalTools.executeCommand(command);
      TraceRouteDto traceRouteDto = this.getTraceRouteService().createOrUpdateTraceRoute(host, terminalDto, operatingSystem);
      return ResponseFactory.<TraceRouteDto>builder().response(traceRouteDto).status(ResponseStatus.OK).build();
    } catch (Exception e) {
      return ResponseFactory.<TraceRouteDto>builder().status(ResponseStatus.INTERNAL_SERVER_ERROR).exception(e).build();
    }
  }
}
