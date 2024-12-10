package com.byborgenterprises.configs;

import lombok.NonNull;

/**
 * The BatchJobsConfig interface defines the configuration for batch jobs
 * in the application. It includes methods to initialize workflows and
 * configure jobs for ICMP ping, TCP/IP ping, and traceroute tasks.
 */
public interface BatchJobsConfig {

  /**
   * Starts the workflow for executing scheduled jobs.
   */
  void startWorkflow();

  /**
   * Configures a job to perform ICMP ping tasks for the specified host.
   *
   * @param host the target host for the ICMP ping job.
   * @return a {@link Runnable} representing the ICMP ping job.
   */
  @NonNull
  Runnable pingIcmpProtocolJob(@NonNull final String host);

  /**
   * Configures a job to perform TCP/IP ping tasks for the specified host.
   *
   * @param host the target host for the TCP/IP ping job.
   * @return a {@link Runnable} representing the TCP/IP ping job.
   */
  @NonNull
  Runnable pingTcpIpProtocolJob(@NonNull final String host);

  /**
   * Configures a job to perform traceroute tasks for the specified host.
   *
   * @param host the target host for the traceroute job.
   * @return a {@link Runnable} representing the traceroute job.
   */
  @NonNull
  Runnable traceRouteJob(@NonNull final String host);
}

