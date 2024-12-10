package com.byborgenterprises.requests;

import com.byborgenterprises.dtos.PingIcmpDto;
import com.byborgenterprises.dtos.PingTcpIpDto;
import com.byborgenterprises.dtos.TerminalDto;
import com.byborgenterprises.dtos.TraceRouteDto;
import com.byborgenterprises.enums.OperatingSystem;
import com.byborgenterprises.enums.ResponseStatus;
import com.byborgenterprises.exceptions.NetworkToolsException;
import com.byborgenterprises.exceptions.OperatingSystemNotFoundException;
import com.byborgenterprises.exceptions.TerminalCommandException;
import com.byborgenterprises.factories.ResponseFactory;
import com.byborgenterprises.services.PingIcmpService;
import com.byborgenterprises.services.PingTcpIpService;
import com.byborgenterprises.services.TraceRouteService;
import com.byborgenterprises.utils.OperatingSystemTools;
import com.byborgenterprises.utils.TerminalTools;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PingRequestTest {

  @Mock
  private PingIcmpService pingIcmpService;

  @Mock
  private PingTcpIpService pingTcpIpService;

  @Mock
  private TraceRouteService traceRouteService;

  @InjectMocks
  private PingRequest pingRequest;

  @Test
  void testGetIcmp_Success() {
    //given
    String host = "host";
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    PingIcmpDto pingIcmpDto = new PingIcmpDto("", terminalDto, true);
    //when
    Mockito.when(pingIcmpService.getPing(host)).thenReturn(Optional.of(pingIcmpDto));
    ResponseFactory<PingIcmpDto> response = pingRequest.getIcmp(host);
    //then
    Assertions.assertNotNull(response);
    Assertions.assertEquals(ResponseStatus.OK, response.getStatus());
    Assertions.assertEquals(pingIcmpDto, response.getResponse());
  }

  @Test
  void testGetIcmp_InvalidHost() {
    //given
    String host = "host";
    //when
    Mockito.when(pingIcmpService.getPing(host)).thenReturn(Optional.empty());
    ResponseFactory<PingIcmpDto> response = pingRequest.getIcmp(host);
    //then
    Assertions.assertNotNull(response);
    Assertions.assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
  }

  @Test
  void testGetIcmp_NullHost() {
    //given
    String host = null;
    //when
    Executable executable = () -> pingRequest.getIcmp(host);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testPostIcmp_Success() {
    //given
    String host = "host";
    OperatingSystem operatingSystem = Mockito.mock(OperatingSystem.class);
    String command = "command";
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    PingIcmpDto pingIcmpDto = new PingIcmpDto("", terminalDto, true);
    try (MockedStatic<OperatingSystemTools> operatingSystemTools = Mockito.mockStatic(OperatingSystemTools.class); MockedStatic<TerminalTools> terminalTools = Mockito.mockStatic(
        TerminalTools.class)) {
      //when
      operatingSystemTools.when(OperatingSystemTools::getOperatingSystem).thenReturn(operatingSystem);
      Mockito.when(pingIcmpService.getTerminalCommand(host, operatingSystem)).thenReturn(command);
      terminalTools.when(() -> TerminalTools.executeCommand(command)).thenReturn(terminalDto);
      Mockito.when(pingIcmpService.createOrUpdatePing(host, terminalDto, operatingSystem)).thenReturn(pingIcmpDto);
      ResponseFactory<PingIcmpDto> response = pingRequest.postIcmp(host);
      //then
      Assertions.assertNotNull(response);
      Assertions.assertEquals(ResponseStatus.OK, response.getStatus());
      Assertions.assertEquals(pingIcmpDto, response.getResponse());
    }
  }

  @Test
  void testPostIcmp_InternalServerError_OperatingSystemNotFoundException() {
    //given
    String host = "host";
    OperatingSystemNotFoundException e = Mockito.mock(OperatingSystemNotFoundException.class);
    try (MockedStatic<OperatingSystemTools> operatingSystemTools = Mockito.mockStatic(OperatingSystemTools.class)) {
      //when
      operatingSystemTools.when(OperatingSystemTools::getOperatingSystem).thenThrow(e);
      ResponseFactory<PingIcmpDto> response = pingRequest.postIcmp(host);
      //then
      Assertions.assertNotNull(response);
      Assertions.assertEquals(ResponseStatus.INTERNAL_SERVER_ERROR, response.getStatus());
      Assertions.assertEquals(e, response.getException());
    }
  }

  @Test
  void testPostIcmp_InternalServerError_TerminalCommandException() {
    //given
    String host = "host";
    OperatingSystem operatingSystem = Mockito.mock(OperatingSystem.class);
    String command = "command";
    TerminalCommandException e = Mockito.mock(TerminalCommandException.class);
    try (MockedStatic<OperatingSystemTools> operatingSystemTools = Mockito.mockStatic(OperatingSystemTools.class); MockedStatic<TerminalTools> terminalTools = Mockito.mockStatic(
        TerminalTools.class)) {
      //when
      operatingSystemTools.when(OperatingSystemTools::getOperatingSystem).thenReturn(operatingSystem);
      Mockito.when(pingIcmpService.getTerminalCommand(host, operatingSystem)).thenReturn(command);
      terminalTools.when(() -> TerminalTools.executeCommand(command)).thenThrow(e);
      ResponseFactory<PingIcmpDto> response = pingRequest.postIcmp(host);
      //then
      Assertions.assertNotNull(response);
      Assertions.assertEquals(ResponseStatus.INTERNAL_SERVER_ERROR, response.getStatus());
      Assertions.assertEquals(e, response.getException());
    }
  }

  @Test
  void testPostIcmp_NullHost() {
    //given
    String host = null;
    //when
    Executable executable = () -> pingRequest.postIcmp(host);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testGetTcp_Success() {
    //given
    String host = "host";
    PingTcpIpDto pingTcpIpDto = new PingTcpIpDto(URI.create("http://localhost"), 0,0, LocalDateTime.now(), false);
    //when
    Mockito.when(pingTcpIpService.getPing(host)).thenReturn(Optional.of(pingTcpIpDto));
    ResponseFactory<PingTcpIpDto> response = pingRequest.getTcp(host);
    //then
    Assertions.assertNotNull(response);
    Assertions.assertEquals(ResponseStatus.OK, response.getStatus());
    Assertions.assertEquals(pingTcpIpDto, response.getResponse());
  }

  @Test
  void testGetTcp_InvalidHost() {
    //given
    String host = "host";
    //when
    Mockito.when(pingTcpIpService.getPing(host)).thenReturn(Optional.empty());
    ResponseFactory<PingTcpIpDto> response = pingRequest.getTcp(host);
    //then
    Assertions.assertNotNull(response);
    Assertions.assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
  }

  @Test
  void testGetTcp_NullHost() {
    //given
    String host = null;
    //when
    Executable executable = () -> pingRequest.getTcp(host);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testPostTcp_Success() {
    //given
    String host = "host";
    PingTcpIpDto pingTcpIpDto = new PingTcpIpDto(URI.create("http://localhost"), 0,0, LocalDateTime.now(), true);
    //when
    Mockito.when(pingTcpIpService.createOrUpdatePing(host)).thenReturn(pingTcpIpDto);
    ResponseFactory<PingTcpIpDto> posted = pingRequest.postTcp(host);
    //then
    Assertions.assertNotNull(posted);
    Assertions.assertEquals(ResponseStatus.OK, posted.getStatus());
    Assertions.assertEquals(pingTcpIpDto, posted.getResponse());
  }

  @Test
  void testPostTcp_InternalServerError_NetworkToolsException() {
    //given
    String host = "host";
    NetworkToolsException e = Mockito.mock(NetworkToolsException.class);
    //when
    Mockito.when(pingTcpIpService.createOrUpdatePing(host)).thenThrow(e);
    ResponseFactory<PingTcpIpDto> posted = pingRequest.postTcp(host);
    //then
    Assertions.assertNotNull(posted);
    Assertions.assertEquals(ResponseStatus.INTERNAL_SERVER_ERROR, posted.getStatus());
    Assertions.assertEquals(e, posted.getException());
  }

  @Test
  void testPostTcp_NullHost() {
    //given
    String host = null;
    //when
    Executable executable = () -> pingRequest.postTcp(host);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testGetTraceRoute_Success() {
    //given
    String host = "host";
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    TraceRouteDto traceRouteDto = new TraceRouteDto("host", terminalDto, true);
    //when
    Mockito.when(traceRouteService.getTraceRoute(host)).thenReturn(Optional.of(traceRouteDto));
    ResponseFactory<TraceRouteDto> response = pingRequest.getTraceRoute(host);
    //then
    Assertions.assertNotNull(response);
    Assertions.assertEquals(ResponseStatus.OK, response.getStatus());
    Assertions.assertEquals(traceRouteDto, response.getResponse());
  }

  @Test
  void testGetTraceRoute_InvalidHost() {
    //given
    String host = "host";
    //when
    Mockito.when(traceRouteService.getTraceRoute(host)).thenReturn(Optional.empty());
    ResponseFactory<TraceRouteDto> response = pingRequest.getTraceRoute(host);
    //then
    Assertions.assertNotNull(response);
    Assertions.assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
  }

  @Test
  void testGetTraceRoute_NullHost() {
    //given
    String host = null;
    //when
    Executable executable = () -> pingRequest.getTraceRoute(host);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testPostTraceRoute_Success() {
    //given
    String host = "host";
    OperatingSystem operatingSystem = Mockito.mock(OperatingSystem.class);
    String command = "command";
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    TraceRouteDto traceRouteDto = new TraceRouteDto("host", terminalDto, false);
    try (MockedStatic<OperatingSystemTools> operatingSystemTools = Mockito.mockStatic(OperatingSystemTools.class); MockedStatic<TerminalTools> terminalTools = Mockito.mockStatic(
        TerminalTools.class)) {
      //when
      operatingSystemTools.when(OperatingSystemTools::getOperatingSystem).thenReturn(operatingSystem);
      Mockito.when(traceRouteService.getTerminalCommand(host, operatingSystem)).thenReturn(command);
      terminalTools.when(() -> TerminalTools.executeCommand(command)).thenReturn(terminalDto);
      Mockito.when(traceRouteService.createOrUpdateTraceRoute(host, terminalDto, operatingSystem)).thenReturn(traceRouteDto);
      ResponseFactory<TraceRouteDto> response = pingRequest.postTraceRoute(host);
      //then
      Assertions.assertNotNull(response);
      Assertions.assertEquals(ResponseStatus.OK, response.getStatus());
      Assertions.assertEquals(traceRouteDto, response.getResponse());
    }
  }

  @Test
  void testPostTraceRoute_InternalServerError_OperatingSystemNotFoundException() {
    //given
    String host = "host";
    OperatingSystemNotFoundException e = Mockito.mock(OperatingSystemNotFoundException.class);
    try (MockedStatic<OperatingSystemTools> operatingSystemTools = Mockito.mockStatic(OperatingSystemTools.class)) {
      //when
      operatingSystemTools.when(OperatingSystemTools::getOperatingSystem).thenThrow(e);
      ResponseFactory<TraceRouteDto> response = pingRequest.postTraceRoute(host);
      //then
      Assertions.assertNotNull(response);
      Assertions.assertEquals(ResponseStatus.INTERNAL_SERVER_ERROR, response.getStatus());
      Assertions.assertEquals(e, response.getException());
    }
  }

  @Test
  void testPostTraceRoute_InternalServerError_TerminalCommandException() {
    //given
    String host = "host";
    OperatingSystem operatingSystem = Mockito.mock(OperatingSystem.class);
    String command = "command";
    TerminalCommandException e = Mockito.mock(TerminalCommandException.class);
    try (MockedStatic<OperatingSystemTools> operatingSystemTools = Mockito.mockStatic(OperatingSystemTools.class); MockedStatic<TerminalTools> terminalTools = Mockito.mockStatic(
        TerminalTools.class)) {
      //when
      operatingSystemTools.when(OperatingSystemTools::getOperatingSystem).thenReturn(operatingSystem);
      Mockito.when(traceRouteService.getTerminalCommand(host, operatingSystem)).thenReturn(command);
      terminalTools.when(() -> TerminalTools.executeCommand(command)).thenThrow(e);
      ResponseFactory<TraceRouteDto> response = pingRequest.postTraceRoute(host);
      //then
      Assertions.assertNotNull(response);
      Assertions.assertEquals(ResponseStatus.INTERNAL_SERVER_ERROR, response.getStatus());
      Assertions.assertEquals(e, response.getException());
    }
  }

  @Test
  void testPostTraceRoute_NullHost() {
    //given
    String host = null;
    //when
    Executable executable = () -> pingRequest.postTraceRoute(host);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }
}
