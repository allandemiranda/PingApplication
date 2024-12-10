package com.byborgenterprises.utils;

import com.byborgenterprises.dtos.TerminalDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class TerminalToolsTest {

  @Test
  void testExecuteCommand_Echo() {
    //given
    String command = "echo Hello";
    //when
    TerminalDto result = TerminalTools.executeCommand(command);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals("Hello", result.result().trim());
    Assertions.assertEquals(0, result.exitCode());
    Assertions.assertNotNull(result.time());
  }

  @Test
  void testExecuteCommand_NullCommand() {
    // given
    String command = null;
    // when
    Executable executable = () -> TerminalTools.executeCommand(command);
    // then
    Assertions.assertThrows(NullPointerException.class, executable);
  }
}
