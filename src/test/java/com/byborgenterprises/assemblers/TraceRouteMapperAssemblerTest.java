package com.byborgenterprises.assemblers;

import com.byborgenterprises.dtos.TerminalDto;
import com.byborgenterprises.dtos.TraceRouteDto;
import com.byborgenterprises.embeddables.Terminal;
import com.byborgenterprises.entities.TraceRoute;
import com.byborgenterprises.mappers.TerminalMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraceRouteMapperAssemblerTest {

  @Mock
  private TerminalMapper terminalMapper;

  @InjectMocks
  private TraceRouteMapperAssembler traceRouteMapperAssembler;

  @Test
  void testToDto_success() {
    //given
    String host = "google.com";
    Terminal terminal = Mockito.mock(Terminal.class);
    TraceRoute traceRoute = new TraceRoute(host, terminal);
    boolean success = true;
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    //when
    Mockito.when(terminalMapper.toDto(terminal)).thenReturn(terminalDto);
    traceRoute.setSuccess(success);
    TraceRouteDto traceRouteDto = traceRouteMapperAssembler.toDto(traceRoute);
    //then
    Assertions.assertNotNull(traceRouteDto);
    Assertions.assertEquals(host, traceRouteDto.host());
    Assertions.assertEquals(terminalDto, traceRouteDto.terminal());
    Assertions.assertEquals(success, traceRouteDto.success());
  }

  @Test
  void testToDto_nullInput() {
    //given
    //when
    Executable executable = () -> traceRouteMapperAssembler.toDto(null);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testToDto_nullTerminal() {
    //given
    TraceRoute traceRoute = Mockito.mock(TraceRoute.class);
    //when
    Mockito.when(traceRoute.getTerminal()).thenReturn(null);
    Executable executable = () -> traceRouteMapperAssembler.toDto(traceRoute);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }
}