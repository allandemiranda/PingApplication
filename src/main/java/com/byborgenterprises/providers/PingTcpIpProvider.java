package com.byborgenterprises.providers;

import com.byborgenterprises.dtos.PingTcpIpDto;
import com.byborgenterprises.entities.PingTcpIp;
import com.byborgenterprises.enums.HttpMethod;
import com.byborgenterprises.mappers.PingTcpIpMapper;
import com.byborgenterprises.repositories.PingTcpIpRepository;
import com.byborgenterprises.services.PingTcpIpService;
import com.byborgenterprises.utils.NetworkTools;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

/**
 * The PingTcpIpProvider class implements {@link PingTcpIpService} to manage
 * TCP/IP ping operations. It interacts with repositories, mappers, and utilities
 * to handle the creation, update, and retrieval of TCP/IP ping data.
 *
 * <p>This class encapsulates the business logic for processing TCP/IP ping operations,
 * including constructing requests, handling responses, and evaluating success criteria.
 */
@RequiredArgsConstructor
@Getter(AccessLevel.PRIVATE)
public class PingTcpIpProvider implements PingTcpIpService {

  private final PingTcpIpRepository pingTcpIpRepository;
  private final PingTcpIpMapper pingTcpIpMapper;
  private final long timeout;
  private final String protocol;

  /**
   * Creates a {@link PingTcpIp} entity with the given details.
   *
   * @param url the target URL of the ping.
   * @param responseCode the HTTP response code returned.
   * @param responseTime the time taken for the response, in milliseconds.
   * @param time the timestamp when the ping was performed.
   * @param success whether the ping operation was successful.
   * @return a {@link PingTcpIp} entity.
   */
  @NonNull
  private static PingTcpIp getPingTcpIp(@NonNull final URI url, final int responseCode, final long responseTime, @NonNull final LocalDateTime time, final boolean success) {
    PingTcpIp pingTcpIp = new PingTcpIp(url, time);
    pingTcpIp.setResponseCode(responseCode);
    pingTcpIp.setResponseTime(responseTime);
    pingTcpIp.setSuccess(success);
    return pingTcpIp;
  }

  @Override
  @Synchronized
  public @NonNull PingTcpIpDto createOrUpdatePing(@NonNull String host) {
    LocalDateTime time = LocalDateTime.now();
    long startTime = System.currentTimeMillis();
    URI uri = NetworkTools.getUri(this.getProtocol(), host, Map.of());

    try {
      HttpResponse<String> httpResponse = NetworkTools.httpRequest(uri, Map.of("Content-Type", "text/plain"), HttpMethod.GET, "", this.getTimeout());
      return this.savePing(uri, httpResponse.statusCode(), System.currentTimeMillis() - startTime, time);
    } catch (IOException e) {
      // timeout
      return this.savePing(uri, -1, System.currentTimeMillis() - startTime, time);
    }
  }

  /**
   * Saves the details of a TCP/IP ping operation to the repository.
   *
   * @param uri the target URL of the ping.
   * @param responseCode the HTTP response code returned.
   * @param responseTime the time taken for the response, in milliseconds.
   * @param time the timestamp when the ping was performed.
   * @return a {@link PingTcpIpDto} representing the saved ping operation.
   */
  private @NonNull PingTcpIpDto savePing(@NonNull final URI uri, int responseCode, long responseTime, @NonNull final LocalDateTime time) {
    boolean isSuccess = responseCode >= 100 && responseCode <= 599;
    PingTcpIp pingTcpIp = getPingTcpIp(uri, responseCode, responseTime, time, isSuccess);
    PingTcpIp savedPing = this.getPingTcpIpRepository().save(pingTcpIp);
    return this.getPingTcpIpMapper().toDto(savedPing);
  }

  @Override
  public @NonNull Optional<PingTcpIpDto> getPing(@NonNull String host) {
    URI url = NetworkTools.getUri(this.getProtocol(), host, Map.of());
    return this.getPingTcpIpRepository().findById(url).map(pingTcpIp -> this.getPingTcpIpMapper().toDto(pingTcpIp));
  }
}
