package com.byborgenterprises.controllers;

import com.byborgenterprises.dtos.PingIcmpDto;
import com.byborgenterprises.dtos.PingTcpIpDto;
import com.byborgenterprises.dtos.TraceRouteDto;
import com.byborgenterprises.factories.ResponseFactory;
import lombok.NonNull;

/**
 * The PingController interface defines operations for performing ping using ICMP and
 * TCP/IP protocols and traceroute tasks. It provides methods for retrieving and
 * submitting results for hosts using different network diagnostic techniques.
 *
 * <p>Each method returns a {@link ResponseFactory} containing the corresponding DTO.
 */
public interface PingController {

  /**
   * Performs a GET operation to retrieve ICMP ping results for the specified host.
   *
   * @param host the target host to perform the ICMP ping operation on.
   * @return a {@link ResponseFactory} containing the results of the ICMP ping.
   */
  @NonNull
  ResponseFactory<PingIcmpDto> getIcmp(@NonNull final String host);

  /**
   * Performs a POST operation to initiate an ICMP ping for the specified host.
   *
   * @param host the target host to initiate the ICMP ping operation on.
   * @return a {@link ResponseFactory} containing the results of the initiated ICMP ping.
   */
  @NonNull
  ResponseFactory<PingIcmpDto> postIcmp(@NonNull final String host);

  /**
   * Performs a GET operation to retrieve TCP/IP ping results for the specified host.
   *
   * @param host the target host to perform the TCP/IP ping operation on.
   * @return a {@link ResponseFactory} containing the results of the TCP/IP ping.
   */
  @NonNull
  ResponseFactory<PingTcpIpDto> getTcp(@NonNull final String host);

  /**
   * Performs a POST operation to initiate a TCP/IP ping for the specified host.
   *
   * @param host the target host to initiate the TCP/IP ping operation on.
   * @return a {@link ResponseFactory} containing the results of the initiated TCP/IP ping.
   */
  @NonNull
  ResponseFactory<PingTcpIpDto> postTcp(@NonNull final String host);

  /**
   * Performs a GET operation to retrieve traceroute results for the specified host.
   *
   * @param host the target host to perform the traceroute operation on.
   * @return a {@link ResponseFactory} containing the results of the traceroute.
   */
  @NonNull
  ResponseFactory<TraceRouteDto> getTraceRoute(@NonNull final String host);

  /**
   * Performs a POST operation to initiate a traceroute for the specified host.
   *
   * @param host the target host to initiate the traceroute operation on.
   * @return a {@link ResponseFactory} containing the results of the initiated traceroute.
   */
  @NonNull
  ResponseFactory<TraceRouteDto> postTraceRoute(@NonNull final String host);

}
