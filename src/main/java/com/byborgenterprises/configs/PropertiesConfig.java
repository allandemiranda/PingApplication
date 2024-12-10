package com.byborgenterprises.configs;

import java.util.List;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

/**
 * The PropertiesConfig interface provides access to application configuration properties
 * using the Owner library. It includes methods to retrieve settings related to hosts,
 * ping commands, timeouts, and traceroute parameters.
 */
@Sources("classpath:config.properties")
public interface PropertiesConfig extends Config {

  /**
   * Retrieves the list of hosts to be monitored.
   *
   * @return a list of hostnames or IP addresses.
   */
  @Key("job.hosts")
  @Separator(",")
  List<String> getHosts();

  /**
   * Retrieves the delay between ICMP ping jobs.
   *
   * @return the delay in milliseconds.
   */
  @Key("icmp.job.delay")
  @DefaultValue("5000")
  long getDelayIcmp();

  /**
   * Retrieves the ICMP ping command for Windows systems.
   *
   * @return the Windows-specific ping command.
   */
  @Key("icmp.job.command.windows")
  @DefaultValue("ping -n 5 HOST")
  String getPingCommandWindows();

  /**
   * Retrieves the ICMP ping command for Linux systems.
   *
   * @return the Linux-specific ping command.
   */
  @Key("icmp.job.command.linux")
  @DefaultValue("ping -c 5 HOST")
  String getPingCommandLinux();

  /**
   * Retrieves the delay between TCP/IP ping jobs.
   *
   * @return the delay in milliseconds.
   */
  @Key("tcp.job.delay")
  @DefaultValue("5000")
  long getDelayTcpIp();

  /**
   * Retrieves the timeout for TCP/IP requests.
   *
   * @return the timeout in milliseconds.
   */
  @Key("tcp.request.timeout")
  @DefaultValue("5000")
  long getTimeout();

  /**
   * Retrieves the protocol used for TCP/IP requests.
   *
   * @return the protocol as a string.
   */
  @Key("tcp.request.protocol")
  @DefaultValue("http")
  String getProtocol();

  /**
   * Retrieves the delay between traceroute jobs.
   *
   * @return the delay in milliseconds.
   */
  @Key("traceroute.job.delay")
  @DefaultValue("5000")
  long getDelayTraceroute();

  /**
   * Retrieves the traceroute command for Windows systems.
   *
   * @return the Windows-specific traceroute command.
   */
  @Key("traceroute.job.command.windows")
  @DefaultValue("tracert HOST")
  String getTracerouteCommandWindows();

  /**
   * Retrieves the traceroute command for Linux systems.
   *
   * @return the Linux-specific traceroute command.
   */
  @Key("traceroute.job.command.linux")
  @DefaultValue("traceroute HOST")
  String getTracerouteCommandLinux();

  /**
   * Retrieves the base URL for report submission.
   *
   * @return the URL as a string.
   */
  @Key("report.job.api.baseUrl")
  @DefaultValue("https://yourreporturl.com/report")
  String getReportUrl();

  /**
   * Retrieves the number of threads allocated for scheduled jobs.
   *
   * @return the number of threads as an integer.
   */
  @Key("job.scheduled.thread.number")
  @DefaultValue("0")
  int getScheduledThreadNumber();
}
