package com.byborgenterprises.factories;

import com.byborgenterprises.configs.BatchJobsConfig;
import com.byborgenterprises.dtos.PingIcmpDto;
import com.byborgenterprises.dtos.PingTcpIpDto;
import com.byborgenterprises.dtos.ReportDto;
import com.byborgenterprises.dtos.TraceRouteDto;
import com.byborgenterprises.exceptions.BatchJobsException;
import com.byborgenterprises.exceptions.ResponseServerErrorException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * The BatchJobsFactory class extends {@link InitializationFactory} and implements
 * {@link BatchJobsConfig} to manage and schedule batch jobs for network operations.
 *
 * <p>This class centralizes the execution of recurring tasks such as:
 * <ul>
 *   <li>ICMP ping jobs</li>
 *   <li>TCP/IP ping jobs</li>
 *   <li>Traceroute jobs</li>
 *   <li>Report generation and submission</li>
 * </ul>
 *
 * <p>BatchJobsFactory leverages the existing infrastructure, including controllers,
 * services, and configuration settings, to schedule and monitor these jobs efficiently.
 */
@Log4j2
@RequiredArgsConstructor
@Getter(AccessLevel.PRIVATE)
public class BatchJobsFactory extends InitializationFactory implements BatchJobsConfig {

  /**
   * Extracts the error stack from a given throwable, formatting it into a readable collection.
   *
   * @param e the throwable to process.
   * @return a collection of error messages, including causes.
   */
  private static @NonNull Collection<String> getErrorStack(final @NonNull Throwable e) {
    Collection<String> errorStack = new ArrayList<>();
    errorStack.add(String.format("Error: %s", e.getMessage()));
    Throwable cause = e.getCause();
    while (Objects.nonNull(cause)) {
      if (cause.getMessage() != null) {
        errorStack.add(String.format("-> Cause: %s", cause.getMessage()));
      }
      cause = cause.getCause();
    }
    return errorStack;
  }

  @Override
  public void startWorkflow() {
    int numThreads = this.getPropertiesConfig().getScheduledThreadNumber() == 0 ? Runtime.getRuntime().availableProcessors() : this.getPropertiesConfig().getScheduledThreadNumber();
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(numThreads);
    try {
      log.info("Starting workflow...");

      var jobs = this.getPropertiesConfig().getHosts().parallelStream().flatMap(host -> this.getJobsPerHost(host).stream())
          .map(info -> scheduledExecutorService.scheduleAtFixedRate(info.getKey(), 0L, info.getValue(), TimeUnit.MILLISECONDS)).toList();

      log.info("Jobs scheduled:\n{}", jobs.stream().map(Object::toString).collect(Collectors.joining("\n")));

      boolean jobsStopped = false;
      while (!jobs.isEmpty() && !jobsStopped) {
        jobsStopped = jobs.parallelStream().anyMatch(Future::isDone);
      }
      log.info("Internal error critical found");
      log.info("Jobs status:\n{}", jobs.stream().map(Object::toString).collect(Collectors.joining("\n")));
    } finally {
      scheduledExecutorService.shutdown();
      log.info("Shutting down the program ...");
    }
  }

  /**
   * Retrieves all jobs to be executed for a specific host.
   *
   * @param host the host for which jobs will be created.
   * @return a collection of runnable jobs paired with their execution delay.
   */
  private @NonNull Collection<@NonNull SimpleEntry<@NonNull Runnable, @NonNull Long>> getJobsPerHost(@NonNull final String host) {
    Collection<SimpleEntry<Runnable, Long>> jobs = new ArrayList<>();
    jobs.add(new SimpleEntry<>(this.pingIcmpProtocolJob(host), this.getPropertiesConfig().getDelayIcmp()));
    jobs.add(new SimpleEntry<>(this.pingTcpIpProtocolJob(host), this.getPropertiesConfig().getDelayTcpIp()));
    jobs.add(new SimpleEntry<>(this.traceRouteJob(host), this.getPropertiesConfig().getDelayTraceroute()));
    return jobs;
  }

  @Override
  public @NonNull Runnable pingIcmpProtocolJob(@NonNull String host) {
    return () -> {
      Thread.currentThread().setName("job-" + host + "-icmp");
      try {
        ResponseFactory<PingIcmpDto> responseFactory = this.getPingController().postIcmp(host);
        PingIcmpDto pingIcmpDto = this.responseValidation(responseFactory, host, "ICMP protocol Ping");
        if (!pingIcmpDto.success()) {
          log.debug("Unsuccessful Ping ICMP Protocol Job for host {}", host);
          ReportDto reportDto = this.getReport(host);
          ResponseFactory<Void> posted = this.getReportController().postReport(reportDto);
          this.responseValidation(posted, host, "Report for ICMP protocol Ping");
          log.debug("Report information sent to Report API");
        } else {
          log.debug("Successful Ping ICMP Protocol Job for host {}", host);
        }
      } catch (BatchJobsException e) {
        Collection<String> errorStack = getErrorStack(e);
        log.debug("Batch Job Error on ICMP protocol Ping:\n{}", String.join("\n", errorStack));
      }
    };
  }

  @Override
  public @NonNull Runnable pingTcpIpProtocolJob(@NonNull String host) {
    return () -> {
      Thread.currentThread().setName("job-" + host + "-tcp");
      try {
        ResponseFactory<PingTcpIpDto> responseFactory = this.getPingController().postTcp(host);
        PingTcpIpDto pingTcpIpDto = this.responseValidation(responseFactory, host, "TCP/IP protocol Ping");
        if (!pingTcpIpDto.success()) {
          log.debug("Unsuccessful TCP/IP Protocol Ping Job for host {}", host);
          ReportDto reportDto = this.getReport(host);
          this.responseValidation(this.getReportController().postReport(reportDto), host, "Report for TCP/IP protocol Ping");
        } else {
          log.debug("Successful TCP/IP Protocol Ping Job for host {}", host);
        }
      } catch (BatchJobsException e) {
        Collection<String> errorStack = getErrorStack(e);
        log.debug("Batch Job Error on TCP/IP protocol Ping:\n{}", String.join("\n", errorStack));
      }
    };
  }

  @Override
  public @NonNull Runnable traceRouteJob(@NonNull String host) {
    return () -> {
      Thread.currentThread().setName("job-" + host + "-traceroute");
      try {
        ResponseFactory<TraceRouteDto> responseFactory = this.getPingController().postTraceRoute(host);
        TraceRouteDto traceRouteDto = this.responseValidation(responseFactory, host, "Trace Route");
        if (!traceRouteDto.success()) {
          log.debug("Unsuccessful Trace Route Job for host {}", host);
          ReportDto reportDto = this.getReport(host);
          this.responseValidation(this.getReportController().postReport(reportDto), host, "Report for Trace Route");
        } else {
          log.debug("Successful Trace Route Job for host {}", host);
        }
      } catch (BatchJobsException e) {
        Collection<String> errorStack = getErrorStack(e);
        log.debug("Batch Job Error on Trace Route:\n{}", String.join("\n", errorStack));
      }
    };
  }

  /**
   * Validates the response from a job execution and handles any errors encountered.
   *
   * @param responseFactory the response factory encapsulating job execution results.
   * @param host the host associated with the job.
   * @param jobDescription a description of the job being executed.
   * @return the validated response payload.
   * @param <T> the type of the response payload.
   */
  @NonNull
  private <T> T responseValidation(final @NonNull ResponseFactory<T> responseFactory, @NonNull final String host, @NonNull final String jobDescription) {
    return switch (responseFactory.getStatus()) {
      case BAD_REQUEST -> throw new BatchJobsException(responseFactory.getMessage() + " for " + jobDescription);
      case SERVICE_UNAVAILABLE -> throw new BatchJobsException(jobDescription + ", service is unavailable, for host " + host, responseFactory.getException());
      case INTERNAL_SERVER_ERROR -> throw new ResponseServerErrorException("Internal error for executing " + jobDescription + " for Host " + host, responseFactory.getException());
      case OK -> responseFactory.getResponse();
    };
  }

  /**
   * Creates a report DTO for a specific host by aggregating results from various jobs.
   *
   * @param host the host for which the report is generated.
   * @return the generated {@link ReportDto}.
   */
  @NonNull
  private ReportDto getReport(@NonNull final String host) {
    CompletableFuture<PingIcmpDto> icmpRequest = CompletableFuture.supplyAsync(this.getPingIcmpDtoSupplier(host));
    CompletableFuture<PingTcpIpDto> tcpIpRequest = CompletableFuture.supplyAsync(this.getPingTcpIpDtoSupplier(host));
    CompletableFuture<TraceRouteDto> traceRouteRequest = CompletableFuture.supplyAsync(this.getGetTraceRoutePing(host));
    CompletableFuture<Void> getRequests = CompletableFuture.allOf(icmpRequest, tcpIpRequest, traceRouteRequest);
    getRequests.join();

    try {
      PingIcmpDto pingIcmp = icmpRequest.get();
      PingTcpIpDto pingTcpIp = tcpIpRequest.get();
      TraceRouteDto traceRoute = traceRouteRequest.get();
      return new ReportDto(host, pingIcmp, pingTcpIp, traceRoute);
    } catch (InterruptedException | ExecutionException e) {
      Thread.currentThread().interrupt();
      throw new BatchJobsException("Error on request data to make the Report", e);
    }
  }

  /**
   * Supplies a {@link TraceRouteDto} for a given host, handling request errors.
   *
   * @param host the host for which traceroute data is requested.
   * @return a supplier providing traceroute data.
   */
  private @NonNull Supplier<TraceRouteDto> getGetTraceRoutePing(@NonNull final String host) {
    return () -> {
      try {
        ResponseFactory<TraceRouteDto> responseFactory = this.getPingController().getTraceRoute(host);
        return this.responseValidation(responseFactory, host, "Get Trace Route Ping");
      } catch (BatchJobsException e) {
        Collection<String> errorStack = getErrorStack(e);
        log.debug("Error request at Get Trace Route Ping:\n{}", String.join("\n", errorStack));
        return null;
      }
    };
  }

  /**
   * Supplies a {@link PingTcpIpDto} for a given host, handling request errors.
   *
   * @param host the host for which TCP/IP ping data is requested.
   * @return a supplier providing TCP/IP ping data.
   */
  private @NonNull Supplier<PingTcpIpDto> getPingTcpIpDtoSupplier(@NonNull final String host) {
    return () -> {
      try {
        ResponseFactory<PingTcpIpDto> responseFactory = this.getPingController().getTcp(host);
        return this.responseValidation(responseFactory, host, "Get TCP/IP protocol Ping");
      } catch (BatchJobsException e) {
        Collection<String> errorStack = getErrorStack(e);
        log.debug("Error request at Get TCP/IP protocol Ping:\n{}", String.join("\n", errorStack));
        return null;
      }
    };
  }

  /**
   * Supplies a {@link PingIcmpDto} for a given host, handling request errors.
   *
   * @param host the host for which ICMP ping data is requested.
   * @return a supplier providing ICMP ping data.
   */
  private @NonNull Supplier<PingIcmpDto> getPingIcmpDtoSupplier(@NonNull final String host) {
    return () -> {
      try {
        ResponseFactory<PingIcmpDto> responseFactory = this.getPingController().getIcmp(host);
        return this.responseValidation(responseFactory, host, "Get ICMP protocol Ping");
      } catch (BatchJobsException e) {
        Collection<String> errorStack = getErrorStack(e);
        log.debug("Error request at Get ICMP protocol Ping:\n{}", String.join("\n", errorStack));
        return null;
      }
    };
  }

}
