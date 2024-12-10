package com.byborgenterprises.providers;

import com.byborgenterprises.dtos.TerminalDto;
import com.byborgenterprises.dtos.TraceRouteDto;
import com.byborgenterprises.embeddables.Terminal;
import com.byborgenterprises.entities.TraceRoute;
import com.byborgenterprises.enums.OperatingSystem;
import com.byborgenterprises.mappers.TerminalMapper;
import com.byborgenterprises.mappers.TraceRouteMapper;
import com.byborgenterprises.repositories.TraceRouteRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraceRouteProviderTest {

  @Mock
  private TraceRouteRepository traceRouteRepository;

  @Mock
  private TraceRouteMapper traceRouteMapper;

  @Mock
  private TerminalMapper terminalMapper;

  @InjectMocks
  private TraceRouteProvider traceRouteProvider;

  @BeforeEach
  void setUp() {
    String tracerouteCommandWindows = "tracert HOST";
    String tracerouteCommandLinux = "traceroute HOST";
    traceRouteProvider = new TraceRouteProvider(traceRouteRepository, traceRouteMapper, terminalMapper, tracerouteCommandWindows, tracerouteCommandLinux);
  }

  @Test
  void testGetTerminalCommand_Linux() {
    //given
    String host = "example.com";
    OperatingSystem os = OperatingSystem.LINUX;
    String expectedCommand = "traceroute example.com";
    //when
    String command = traceRouteProvider.getTerminalCommand(host, os);
    //then
    Assertions.assertEquals(expectedCommand, command);
  }

  @Test
  void testGetTerminalCommand_Windows() {
    //given
    String host = "example.com";
    OperatingSystem os = OperatingSystem.WINDOWS;
    String expectedCommand = "cmd.exe /c tracert example.com";
    //when
    String command = traceRouteProvider.getTerminalCommand(host, os);
    //then
    Assertions.assertEquals(expectedCommand, command);
  }

  @Test
  void testGetTerminalCommand_HostWithPort() {
    //given
    String host = "example.com:8080";
    OperatingSystem os = OperatingSystem.LINUX;
    String expectedCommand = "traceroute example.com";
    //when
    String command = traceRouteProvider.getTerminalCommand(host, os);
    //then
    Assertions.assertEquals(expectedCommand, command);
  }

  @Test
  void testGetTerminalCommand_NullOperatingSystem() {
    //given
    String host = "example.com";
    //when
    Executable executable = () -> traceRouteProvider.getTerminalCommand(host, null);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testGetTerminalCommand_NullHost() {
    //given
    OperatingSystem os = Mockito.mock(OperatingSystem.class);
    //when
    Executable executable = () -> traceRouteProvider.getTerminalCommand(null, os);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testGetTerminalCommand_NullHostAndOperatingSystem() {
    //given
    //when
    Executable executable = () -> traceRouteProvider.getTerminalCommand(null, null);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testCreateOrUpdateTraceRoute_Success() {
    //given
    String host = "example.com";
    Terminal terminal = Mockito.mock(Terminal.class);
    TraceRoute traceRoute = Mockito.mock(TraceRoute.class);
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    TraceRouteDto traceRouteDto = new TraceRouteDto("host", terminalDto, false);
    OperatingSystem os = OperatingSystem.LINUX;
    //when
    Mockito.when(terminalMapper.toEntity(terminalDto)).thenReturn(terminal);
    Mockito.when(traceRouteRepository.save(Mockito.any(TraceRoute.class))).thenReturn(traceRoute);
    Mockito.when(traceRouteMapper.toDto(traceRoute)).thenReturn(traceRouteDto);
    TraceRouteDto result = traceRouteProvider.createOrUpdateTraceRoute(host, terminalDto, os);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(traceRouteDto, result);
  }

  @Test
  void testCreateOrUpdateTraceRoute_HostWithPort() {
    //given
    String host = "example.com:8080";
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    Terminal terminal = Mockito.mock(Terminal.class);
    TraceRoute traceRoute = Mockito.mock(TraceRoute.class);
    TraceRouteDto traceRouteDto = new TraceRouteDto("host", terminalDto, false);
    OperatingSystem os = OperatingSystem.LINUX;
    //when
    Mockito.when(terminalMapper.toEntity(terminalDto)).thenReturn(terminal);
    Mockito.when(traceRouteRepository.save(Mockito.any(TraceRoute.class))).thenReturn(traceRoute);
    Mockito.when(traceRouteMapper.toDto(traceRoute)).thenReturn(traceRouteDto);
    TraceRouteDto result = traceRouteProvider.createOrUpdateTraceRoute(host, terminalDto, os);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(traceRouteDto, result);
  }

  @Test
  void testCreateOrUpdateTraceRoute_NullHost() {
    //given
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    OperatingSystem os = OperatingSystem.LINUX;
    //when
    Executable executable = () -> traceRouteProvider.createOrUpdateTraceRoute(null, terminalDto, os);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testCreateOrUpdateTraceRoute_NullTerminalDto() {
    //given
    String host = "example.com";
    OperatingSystem os = OperatingSystem.LINUX;
    //when
    Executable executable = () -> traceRouteProvider.createOrUpdateTraceRoute(host, null, os);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testCreateOrUpdateTraceRoute_NullOperatingSystem() {
    //given
    String host = "example.com";
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    //when
    Executable executable = () -> traceRouteProvider.createOrUpdateTraceRoute(host, terminalDto, null);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testGetTraceRoute_Existing() {
    //given
    String host = "example.com";
    TraceRoute traceRoute = Mockito.mock(TraceRoute.class);
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    TraceRouteDto traceRouteDto = new TraceRouteDto("host", terminalDto, false);
    //when
    Mockito.when(traceRouteRepository.findById(host)).thenReturn(Optional.of(traceRoute));
    Mockito.when(traceRouteMapper.toDto(traceRoute)).thenReturn(traceRouteDto);
    Optional<TraceRouteDto> result = traceRouteProvider.getTraceRoute(host);
    //then
    Assertions.assertTrue(result.isPresent());
    Assertions.assertEquals(traceRouteDto, result.get());
  }

  @Test
  void testGetTraceRoute_NotFound() {
    //given
    String host = "example.com";
    //when
    Mockito.when(traceRouteRepository.findById(host)).thenReturn(Optional.empty());
    Optional<TraceRouteDto> result = traceRouteProvider.getTraceRoute(host);
    //then
    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  void testGetTraceRoute_NullHost() {
    //given
    //when
    Executable executable = () -> traceRouteProvider.getTraceRoute(null);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }
}