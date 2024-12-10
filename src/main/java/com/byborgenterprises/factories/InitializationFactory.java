package com.byborgenterprises.factories;

import com.byborgenterprises.assemblers.PingIcmpMapperAssembler;
import com.byborgenterprises.assemblers.PingTcpIpMapperAssembler;
import com.byborgenterprises.assemblers.TerminalMapperAssembler;
import com.byborgenterprises.assemblers.TraceRouteMapperAssembler;
import com.byborgenterprises.configs.PropertiesConfig;
import com.byborgenterprises.controllers.PingController;
import com.byborgenterprises.controllers.ReportController;
import com.byborgenterprises.mappers.PingIcmpMapper;
import com.byborgenterprises.mappers.PingTcpIpMapper;
import com.byborgenterprises.mappers.TerminalMapper;
import com.byborgenterprises.mappers.TraceRouteMapper;
import com.byborgenterprises.providers.PingIcmpProvider;
import com.byborgenterprises.providers.PingTcpIpProvider;
import com.byborgenterprises.providers.ReportProvider;
import com.byborgenterprises.providers.TraceRouteProvider;
import com.byborgenterprises.repositories.PingIcmpRepository;
import com.byborgenterprises.repositories.PingTcpIpRepository;
import com.byborgenterprises.repositories.TraceRouteRepository;
import com.byborgenterprises.requests.PingRequest;
import com.byborgenterprises.requests.ReportRequest;
import com.byborgenterprises.services.PingIcmpService;
import com.byborgenterprises.services.PingTcpIpService;
import com.byborgenterprises.services.ReportService;
import com.byborgenterprises.services.TraceRouteService;
import lombok.AccessLevel;
import lombok.Getter;
import org.aeonbits.owner.ConfigFactory;

/**
 * The InitializationFactory class is responsible for configuring and initializing
 * all core components of the application, including repositories, mappers, services,
 * and controllers.
 *
 * <p>This factory centralizes the creation and injection of dependencies, ensuring that
 * all components are properly instantiated and wired together. It provides a single
 * point of access for application-wide configurations and initialized components.
 *
 * <p>Main responsibilities include:
 * <ul>
 *   <li>Instantiating repositories for data storage and retrieval.</li>
 *   <li>Configuring mappers for data transformation between entities and DTOs.</li>
 *   <li>Initializing services that implement core business logic.</li>
 *   <li>Providing controllers for interacting with services and responding to requests.</li>
 * </ul>
 */
@Getter(AccessLevel.PRIVATE)
public class InitializationFactory {
  //@formatter:off
  /**
   * The configuration properties for the application, loaded using {@link ConfigFactory}.
   */
  @Getter(AccessLevel.PUBLIC)
  private final PropertiesConfig propertiesConfig = ConfigFactory.create(PropertiesConfig.class);

  /**
   * Repositories for ICMP, TCP/IP, and traceroute data.
   */
  private final PingIcmpRepository pingIcmpRepository = new PingIcmpRepository();
  private final PingTcpIpRepository pingTcpIpRepository = new PingTcpIpRepository();
  private final TraceRouteRepository traceRouteRepository = new TraceRouteRepository();

  /**
   * Mappers for transforming entities to DTOs and vice versa.
   */
  private final TerminalMapper terminalMapper = new TerminalMapperAssembler();
  private final PingIcmpMapper pingIcmpMapper = new PingIcmpMapperAssembler(this.getTerminalMapper());
  private final TraceRouteMapper traceRouteMapper = new TraceRouteMapperAssembler(this.getTerminalMapper());
  private final PingTcpIpMapper pingTcpIpMapper = new PingTcpIpMapperAssembler();

  /**
   * Services implementing core logic for ICMP, TCP/IP, traceroute, and reporting.
   */
  private final PingIcmpService pingIcmpService = new PingIcmpProvider(this.getPropertiesConfig().getPingCommandWindows(), this.getPropertiesConfig().getPingCommandLinux(), this.getPingIcmpRepository(), this.getPingIcmpMapper(), this.getTerminalMapper());
  private final PingTcpIpService pingTcpIpService = new PingTcpIpProvider(this.getPingTcpIpRepository(), this.getPingTcpIpMapper(), this.getPropertiesConfig().getTimeout(), this.getPropertiesConfig().getProtocol());
  private final TraceRouteService traceRouteService = new TraceRouteProvider(this.getTraceRouteRepository(), this.getTraceRouteMapper(), this.getTerminalMapper(), this.getPropertiesConfig().getTracerouteCommandWindows(), this.getPropertiesConfig().getTracerouteCommandLinux());
  private final ReportService reportService = new ReportProvider(this.getPropertiesConfig().getReportUrl());

  /**
   * Controllers for handling requests and interacting with services.
   */
  @Getter(AccessLevel.PUBLIC)
  private final PingController pingController = new PingRequest(this.getPingIcmpService(), this.getPingTcpIpService(), this.getTraceRouteService());
  @Getter(AccessLevel.PUBLIC)
  private final ReportController reportController = new ReportRequest(this.getReportService());
  //@formatter:on
}
