package com.byborgenterprises.dtos;

import java.io.Serial;
import java.io.Serializable;
import lombok.NonNull;

/**
 * The ReportDto record encapsulates the details of a report containing
 * network diagnostic information for a specific host.
 *
 * <p>This record is immutable and implements {@link Serializable} for data transfer.
 *
 * @param host the hostname or IP address associated with the report.
 * @param pingIcmp the {@link PingIcmpDto} containing ICMP ping details.
 * @param pingTcpIp the {@link PingTcpIpDto} containing TCP/IP ping details.
 * @param traceRoute the {@link TraceRouteDto} containing traceroute details.
 */
public record ReportDto(@NonNull String host, PingIcmpDto pingIcmp, PingTcpIpDto pingTcpIp, TraceRouteDto traceRoute) implements Serializable {

  @Serial
  private static final long serialVersionUID = -6881477815168082163L;

}
