package com.byborgenterprises.factories;

import com.byborgenterprises.controllers.PingController;
import com.byborgenterprises.controllers.ReportController;
import com.byborgenterprises.dtos.PingIcmpDto;
import com.byborgenterprises.dtos.PingTcpIpDto;
import com.byborgenterprises.dtos.ReportDto;
import com.byborgenterprises.dtos.TerminalDto;
import com.byborgenterprises.dtos.TraceRouteDto;
import com.byborgenterprises.enums.ResponseStatus;
import com.byborgenterprises.exceptions.BatchJobsException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BatchJobsFactoryTest {

  @Mock
  private PingController pingController;

  @Mock
  private ReportController reportController;

  @Spy
  private BatchJobsFactory batchJobsFactory;

  @Test
  void pingIcmpProtocolJob_Successful() {
    //given
    ResponseStatus status = ResponseStatus.OK;
    String host = "localhost";
    ResponseFactory<PingIcmpDto> responseFactory = Mockito.mock(ResponseFactory.class);
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    PingIcmpDto pingIcmpDto = new PingIcmpDto("", terminalDto, true);
    //when
    Mockito.doReturn(pingController).when(batchJobsFactory).getPingController();
    Mockito.when(pingController.postIcmp(host)).thenReturn(responseFactory);
    Mockito.when(responseFactory.getStatus()).thenReturn(status);
    Mockito.when(responseFactory.getResponse()).thenReturn(pingIcmpDto);
    Runnable job = batchJobsFactory.pingIcmpProtocolJob(host);
    //then
    Assertions.assertDoesNotThrow(job::run);
    Mockito.verify(pingController, Mockito.times(1)).postIcmp(host);
    Mockito.verify(responseFactory, Mockito.times(1)).getResponse();
  }

  @Test
  void pingIcmpProtocolJob_BatchJobError() {
    //given
    String host = "localhost";
    ResponseFactory<PingIcmpDto> icmpResponseFactory = Mockito.mock(ResponseFactory.class);
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    PingIcmpDto pingIcmpDto = new PingIcmpDto("", terminalDto, false);
    Exception e = Mockito.mock(BatchJobsException.class);
    try (MockedStatic<CompletableFuture> cf = Mockito.mockStatic(CompletableFuture.class)) {
      //when
      cf.when(() -> CompletableFuture.supplyAsync(Mockito.any())).thenThrow(e);
      Mockito.when(pingController.postIcmp(host)).thenReturn(icmpResponseFactory);
      Mockito.when(icmpResponseFactory.getStatus()).thenReturn(ResponseStatus.OK);
      Mockito.when(icmpResponseFactory.getResponse()).thenReturn(pingIcmpDto);
      Mockito.doReturn(pingController).when(batchJobsFactory).getPingController();
      Runnable job = batchJobsFactory.pingIcmpProtocolJob(host);
      //then
      Assertions.assertDoesNotThrow(job::run);
      Mockito.verify(e, Mockito.times(1)).getCause();
    }
  }

  @SneakyThrows
  @Test
  void pingIcmpProtocolJob_ReportInformationSent() {
    //given
    String host = "localhost";
    ResponseFactory<PingIcmpDto> icmpResponseFactory = Mockito.mock(ResponseFactory.class);
    ResponseFactory<Void> reportResponseFactory = Mockito.mock(ResponseFactory.class);
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    PingIcmpDto pingIcmpDto = new PingIcmpDto("", terminalDto, false);
    PingTcpIpDto pingTcpIpDto = new PingTcpIpDto(URI.create("http://localhost"), 0,0, LocalDateTime.now(), true);
    TraceRouteDto traceRouteDto = new TraceRouteDto("host", terminalDto, false);
    CompletableFuture<PingIcmpDto> icmpFuture = Mockito.mock(CompletableFuture.class);
    CompletableFuture<PingTcpIpDto> tcpFuture = Mockito.mock(CompletableFuture.class);
    CompletableFuture<TraceRouteDto> traceFuture = Mockito.mock(CompletableFuture.class);
    CompletableFuture<Void> combinedFuture = Mockito.mock(CompletableFuture.class);
    try (MockedStatic<CompletableFuture> cf = Mockito.mockStatic(CompletableFuture.class)) {
      //when
      Mockito.when(pingController.postIcmp(host)).thenReturn(icmpResponseFactory);
      Mockito.when(icmpResponseFactory.getStatus()).thenReturn(ResponseStatus.OK);
      Mockito.when(icmpResponseFactory.getResponse()).thenReturn(pingIcmpDto);
      Mockito.when(reportResponseFactory.getStatus()).thenReturn(ResponseStatus.OK);
      cf.when(() -> CompletableFuture.supplyAsync(Mockito.any())).thenReturn(icmpFuture, tcpFuture, traceFuture);
      cf.when(() -> CompletableFuture.allOf(icmpFuture, tcpFuture, traceFuture)).thenReturn(combinedFuture);
      Mockito.when(combinedFuture.join()).thenReturn(null);
      Mockito.when(icmpFuture.get()).thenReturn(pingIcmpDto);
      Mockito.when(tcpFuture.get()).thenReturn(pingTcpIpDto);
      Mockito.when(traceFuture.get()).thenReturn(traceRouteDto);
      Mockito.doReturn(pingController).when(batchJobsFactory).getPingController();
      Mockito.doReturn(reportController).when(batchJobsFactory).getReportController();
      Mockito.doAnswer(invocation -> {
        ReportDto passedReport = invocation.getArgument(0);
        assert passedReport != null;
        return reportResponseFactory;
      }).when(reportController).postReport(Mockito.any(ReportDto.class));
      Runnable job = batchJobsFactory.pingIcmpProtocolJob(host);
      //then
      Assertions.assertDoesNotThrow(job::run);
      Mockito.verify(pingController, Mockito.times(1)).postIcmp(host);
      Mockito.verify(reportController, Mockito.times(1)).postReport(Mockito.any(ReportDto.class));
    }
  }

  @Test
  void pingTcpIpProtocolJob_Successful() {
    //given
    ResponseStatus status = ResponseStatus.OK;
    String host = "localhost";
    ResponseFactory<PingTcpIpDto> responseFactory = Mockito.mock(ResponseFactory.class);
    PingTcpIpDto pingTcpIpDto = new PingTcpIpDto(URI.create("http://localhost"), 0,0, LocalDateTime.now(), true);
    //when
    Mockito.doReturn(pingController).when(batchJobsFactory).getPingController();
    Mockito.when(pingController.postTcp(host)).thenReturn(responseFactory);
    Mockito.when(responseFactory.getStatus()).thenReturn(status);
    Mockito.when(responseFactory.getResponse()).thenReturn(pingTcpIpDto);
    Runnable job = batchJobsFactory.pingTcpIpProtocolJob(host);
    //then
    Assertions.assertDoesNotThrow(job::run);
    Mockito.verify(pingController, Mockito.times(1)).postTcp(host);
    Mockito.verify(responseFactory, Mockito.times(1)).getResponse();
  }

  @Test
  void pingTcpIpProtocolJob_BatchJobError() {
    //given
    String host = "localhost";
    ResponseFactory<PingTcpIpDto> tcpResponseFactory = Mockito.mock(ResponseFactory.class);
    PingTcpIpDto pingTcpIpDto = new PingTcpIpDto(URI.create("http://localhost"), 0,0, LocalDateTime.now(), false);
    Exception e = Mockito.mock(BatchJobsException.class);
    try (MockedStatic<CompletableFuture> cf = Mockito.mockStatic(CompletableFuture.class)) {
      //when
      cf.when(() -> CompletableFuture.supplyAsync(Mockito.any())).thenThrow(e);
      Mockito.when(pingController.postTcp(host)).thenReturn(tcpResponseFactory);
      Mockito.when(tcpResponseFactory.getStatus()).thenReturn(ResponseStatus.OK);
      Mockito.when(tcpResponseFactory.getResponse()).thenReturn(pingTcpIpDto);
      Mockito.doReturn(pingController).when(batchJobsFactory).getPingController();
      Runnable job = batchJobsFactory.pingTcpIpProtocolJob(host);
      //then
      Assertions.assertDoesNotThrow(job::run);
      Mockito.verify(e, Mockito.times(1)).getCause();
    }
  }

  @SneakyThrows
  @Test
  void pingTcpIpProtocolJob_ReportInformationSent() {
    //given
    String host = "localhost";
    ResponseFactory<PingTcpIpDto> tcpResponseFactory = Mockito.mock(ResponseFactory.class);
    ResponseFactory<Void> reportResponseFactory = Mockito.mock(ResponseFactory.class);
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    PingIcmpDto pingIcmpDto = new PingIcmpDto("", terminalDto, true);
    PingTcpIpDto pingTcpIpDto = new PingTcpIpDto(URI.create("http://localhost"), 0,0, LocalDateTime.now(), false);
    TraceRouteDto traceRouteDto = new TraceRouteDto("host", terminalDto, false);
    CompletableFuture<PingIcmpDto> icmpFuture = Mockito.mock(CompletableFuture.class);
    CompletableFuture<PingTcpIpDto> tcpFuture = Mockito.mock(CompletableFuture.class);
    CompletableFuture<TraceRouteDto> traceFuture = Mockito.mock(CompletableFuture.class);
    CompletableFuture<Void> combinedFuture = Mockito.mock(CompletableFuture.class);
    try (MockedStatic<CompletableFuture> mockedStatic = Mockito.mockStatic(CompletableFuture.class)) {
      //when
      Mockito.when(pingController.postTcp(host)).thenReturn(tcpResponseFactory);
      Mockito.when(tcpResponseFactory.getStatus()).thenReturn(ResponseStatus.OK);
      Mockito.when(tcpResponseFactory.getResponse()).thenReturn(pingTcpIpDto);
      Mockito.when(reportResponseFactory.getStatus()).thenReturn(ResponseStatus.OK);
      mockedStatic.when(() -> CompletableFuture.supplyAsync(Mockito.any())).thenReturn(icmpFuture, tcpFuture, traceFuture);
      mockedStatic.when(() -> CompletableFuture.allOf(icmpFuture, tcpFuture, traceFuture)).thenReturn(combinedFuture);
      Mockito.when(combinedFuture.join()).thenReturn(null);
      Mockito.when(icmpFuture.get()).thenReturn(pingIcmpDto);
      Mockito.when(tcpFuture.get()).thenReturn(pingTcpIpDto);
      Mockito.when(traceFuture.get()).thenReturn(traceRouteDto);
      Mockito.doReturn(pingController).when(batchJobsFactory).getPingController();
      Mockito.doReturn(reportController).when(batchJobsFactory).getReportController();
      Mockito.doAnswer(invocation -> {
        ReportDto passedReport = invocation.getArgument(0);
        assert passedReport != null;
        return reportResponseFactory;
      }).when(reportController).postReport(Mockito.any(ReportDto.class));
      Runnable job = batchJobsFactory.pingTcpIpProtocolJob(host);
      //then
      Assertions.assertDoesNotThrow(job::run);
      Mockito.verify(pingController, Mockito.times(1)).postTcp(host);
      Mockito.verify(reportController, Mockito.times(1)).postReport(Mockito.any(ReportDto.class));
    }
  }

  @Test
  void traceRouteJob_Successful() {
    //given
    ResponseStatus status = ResponseStatus.OK;
    String host = "localhost";
    ResponseFactory<TraceRouteDto> responseFactory = Mockito.mock(ResponseFactory.class);
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    TraceRouteDto traceRouteDto = new TraceRouteDto("host", terminalDto, true);
    //when
    Mockito.doReturn(pingController).when(batchJobsFactory).getPingController();
    Mockito.when(pingController.postTraceRoute(host)).thenReturn(responseFactory);
    Mockito.when(responseFactory.getStatus()).thenReturn(status);
    Mockito.when(responseFactory.getResponse()).thenReturn(traceRouteDto);
    Runnable job = batchJobsFactory.traceRouteJob(host);
    //then
    Assertions.assertDoesNotThrow(job::run);
    Mockito.verify(pingController, Mockito.times(1)).postTraceRoute(host);
    Mockito.verify(responseFactory, Mockito.times(1)).getResponse();
  }

  @Test
  void traceRouteJob_BatchJobError() {
    //given
    String host = "localhost";
    ResponseFactory<TraceRouteDto> traceResponseFactory = Mockito.mock(ResponseFactory.class);
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    TraceRouteDto traceRouteDto = new TraceRouteDto("host", terminalDto, false);
    Exception e = Mockito.mock(BatchJobsException.class);
    try (MockedStatic<CompletableFuture> cf = Mockito.mockStatic(CompletableFuture.class)) {
      //when
      cf.when(() -> CompletableFuture.supplyAsync(Mockito.any())).thenThrow(e);
      Mockito.when(pingController.postTraceRoute(host)).thenReturn(traceResponseFactory);
      Mockito.when(traceResponseFactory.getStatus()).thenReturn(ResponseStatus.OK);
      Mockito.when(traceResponseFactory.getResponse()).thenReturn(traceRouteDto);
      Mockito.doReturn(pingController).when(batchJobsFactory).getPingController();
      Runnable job = batchJobsFactory.traceRouteJob(host);
      //then
      Assertions.assertDoesNotThrow(job::run);
      Mockito.verify(e, Mockito.times(1)).getCause();
    }
  }

  @SneakyThrows
  @Test
  void traceRouteJob_ReportInformationSent() {
    //given
    String host = "localhost";
    ResponseFactory<TraceRouteDto> traceResponseFactory = Mockito.mock(ResponseFactory.class);
    ResponseFactory<Void> reportResponseFactory = Mockito.mock(ResponseFactory.class);
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    PingIcmpDto pingIcmpDto = new PingIcmpDto("", terminalDto, true);
    PingTcpIpDto pingTcpIpDto = new PingTcpIpDto(URI.create("http://localhost"), 0,0, LocalDateTime.now(), true);
    TraceRouteDto traceRouteDto = new TraceRouteDto("", terminalDto, false);
    CompletableFuture<PingIcmpDto> icmpFuture = Mockito.mock(CompletableFuture.class);
    CompletableFuture<PingTcpIpDto> tcpFuture = Mockito.mock(CompletableFuture.class);
    CompletableFuture<TraceRouteDto> traceFuture = Mockito.mock(CompletableFuture.class);
    CompletableFuture<Void> combinedFuture = Mockito.mock(CompletableFuture.class);
    try (MockedStatic<CompletableFuture> mockedStatic = Mockito.mockStatic(CompletableFuture.class)) {
      //when
      Mockito.when(reportResponseFactory.getStatus()).thenReturn(ResponseStatus.OK);
      Mockito.when(pingController.postTraceRoute(host)).thenReturn(traceResponseFactory);
      Mockito.when(traceResponseFactory.getStatus()).thenReturn(ResponseStatus.OK);
      Mockito.when(traceResponseFactory.getResponse()).thenReturn(traceRouteDto);
      mockedStatic.when(() -> CompletableFuture.supplyAsync(Mockito.any())).thenReturn(icmpFuture, tcpFuture, traceFuture);
      mockedStatic.when(() -> CompletableFuture.allOf(icmpFuture, tcpFuture, traceFuture)).thenReturn(combinedFuture);
      Mockito.when(combinedFuture.join()).thenReturn(null);
      Mockito.when(icmpFuture.get()).thenReturn(pingIcmpDto);
      Mockito.when(tcpFuture.get()).thenReturn(pingTcpIpDto);
      Mockito.when(traceFuture.get()).thenReturn(traceRouteDto);
      Mockito.doReturn(pingController).when(batchJobsFactory).getPingController();
      Mockito.doReturn(reportController).when(batchJobsFactory).getReportController();
      Mockito.doAnswer(invocation -> {
        ReportDto passedReport = invocation.getArgument(0);
        assert passedReport != null;
        return reportResponseFactory;
      }).when(reportController).postReport(Mockito.any(ReportDto.class));
      Runnable job = batchJobsFactory.traceRouteJob(host);
      //then
      Assertions.assertDoesNotThrow(job::run);
      Mockito.verify(pingController, Mockito.times(1)).postTraceRoute(host);
      Mockito.verify(reportController, Mockito.times(1)).postReport(Mockito.any(ReportDto.class));
    }
  }

  @Test
  void startWorkflow_SuccessfulExecution() {
    //given
    Runnable runnable = () -> {
      throw new RuntimeException();
    };
    //when
    Mockito.doReturn(runnable).when(batchJobsFactory).pingIcmpProtocolJob(Mockito.anyString());
    Mockito.doReturn(runnable).when(batchJobsFactory).pingTcpIpProtocolJob(Mockito.anyString());
    Mockito.doReturn(runnable).when(batchJobsFactory).traceRouteJob(Mockito.anyString());
    Assertions.assertDoesNotThrow(() -> batchJobsFactory.startWorkflow());
    //then
    Mockito.verify(batchJobsFactory, Mockito.times(2)).pingIcmpProtocolJob(Mockito.anyString());
    Mockito.verify(batchJobsFactory, Mockito.times(2)).pingTcpIpProtocolJob(Mockito.anyString());
    Mockito.verify(batchJobsFactory, Mockito.times(2)).traceRouteJob(Mockito.anyString());

  }

}
