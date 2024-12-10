package com.byborgenterprises.assemblers;

import com.byborgenterprises.dtos.PingIcmpDto;
import com.byborgenterprises.dtos.TerminalDto;
import com.byborgenterprises.embeddables.Terminal;
import com.byborgenterprises.entities.PingIcmp;
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
class PingIcmpMapperAssemblerTest {

  @Mock
  private TerminalMapper terminalMapper;

  @InjectMocks
  private PingIcmpMapperAssembler pingIcmpMapperAssembler;

  @Test
  void testToDto_success() {
    //given
    String host = "google.com";
    Terminal terminal = Mockito.mock(Terminal.class);
    PingIcmp pingIcmp = new PingIcmp(host, terminal);
    boolean success = true;
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    //when
    Mockito.when(terminalMapper.toDto(terminal)).thenReturn(terminalDto);
    pingIcmp.setSuccess(success);
    PingIcmpDto pingIcmpDto = pingIcmpMapperAssembler.toDto(pingIcmp);
    //then
    Assertions.assertNotNull(pingIcmpDto);
    Assertions.assertEquals(host, pingIcmpDto.host());
    Assertions.assertEquals(terminalDto, pingIcmpDto.terminal());
    Assertions.assertEquals(success, pingIcmpDto.success());
  }

  @Test
  void testToDto_nullInput() {
    //given
    //when
    Executable executable = () -> pingIcmpMapperAssembler.toDto(null);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testToDto_nullTerminal() {
    //given
    PingIcmp pingIcmp = Mockito.mock(PingIcmp.class);
    //when
    Mockito.when(pingIcmp.getTerminal()).thenReturn(null);
    Executable executable = () -> pingIcmpMapperAssembler.toDto(pingIcmp);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }
}