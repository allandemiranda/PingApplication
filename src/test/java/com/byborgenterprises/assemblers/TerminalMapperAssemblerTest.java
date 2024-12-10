package com.byborgenterprises.assemblers;

import com.byborgenterprises.dtos.TerminalDto;
import com.byborgenterprises.embeddables.Terminal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TerminalMapperAssemblerTest {

  @InjectMocks
  private TerminalMapperAssembler terminalMapperAssembler;

  @Test
  void testToDto_success() {
    //given
    String command = "command";
    String result = "result";
    LocalDateTime localDateTime = Mockito.mock(LocalDateTime.class);
    Terminal terminal = new Terminal(command, result, localDateTime);
    int exitCode = 200;
    //when
    terminal.setExitCode(exitCode);
    TerminalDto terminalDto = terminalMapperAssembler.toDto(terminal);
    //then
    Assertions.assertNotNull(terminalDto);
    Assertions.assertEquals(command, terminalDto.command());
    Assertions.assertEquals(result, terminalDto.result());
    Assertions.assertEquals(localDateTime, terminalDto.time());
    Assertions.assertEquals(exitCode, terminalDto.exitCode());
  }

  @Test
  void testToDto_nullInput() {
    //given
    //when
    Executable executable = () -> terminalMapperAssembler.toDto(null);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testToEntity_success() {
    //given
    String command = "command";
    int exitCode = 200;
    String result = "result";
    LocalDateTime localDateTime = Mockito.mock(LocalDateTime.class);
    TerminalDto terminalDto = new TerminalDto(command, exitCode, result, localDateTime);
    //when
    Terminal terminal = terminalMapperAssembler.toEntity(terminalDto);
    //then
    Assertions.assertNotNull(terminal);
    Assertions.assertEquals(command, terminal.getCommand());
    Assertions.assertEquals(result, terminal.getResult());
    Assertions.assertEquals(localDateTime, terminal.getTime());
    Assertions.assertEquals(exitCode, terminal.getExitCode());
  }

  @Test
  void testToEntity_nullInput() {
    //given
    //when
    Executable executable = () -> terminalMapperAssembler.toEntity(null);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }
}