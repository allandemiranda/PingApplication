package com.byborgenterprises.utils;

import com.byborgenterprises.dtos.TerminalDto;
import com.byborgenterprises.enums.OperatingSystem;
import com.byborgenterprises.exceptions.TerminalCommandException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * The TerminalTools class provides utility methods for interacting with the terminal
 * of the operating system. It allows execution of commands, capturing their output,
 * and handling errors or exceptions during command execution.
 * This is a final utility class and cannot be instantiated.
 */
@UtilityClass
public final class TerminalTools {

  /**
   * Command prefix for Windows terminal commands.
   */
  private static final String WINDOWS_CMD_EXE = "cmd.exe /c ";

  /**
   * Retrieves the exit code of a terminal command after execution.
   *
   * @param command the terminal command that was executed.
   * @param process the {@link Process} object representing the command execution.
   * @return the exit code of the command.
   * @throws TerminalCommandException if the thread is interrupted while waiting for the process to complete.
   */
  private static int getExitCode(@NonNull final String command, @NonNull final Process process) {
    try {
      return process.waitFor();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new TerminalCommandException("Can't wait for exit exitCode on terminal command: " + command, e);
    }
  }

  /**
   * Captures and returns the output of a terminal command.
   *
   * @param command the terminal command that was executed.
   * @param process the {@link Process} object representing the command execution.
   * @return the output of the terminal command as a string.
   * @throws TerminalCommandException if an error occurs while reading the command output.
   */
  @NonNull
  private static String getReturn(@NonNull final String command, @NonNull final Process process) {
    //@formatter:off
    try (InputStream inputStream = process.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader)) {
      //@formatter:on
      return reader.lines().collect(Collectors.joining("\n"));
    } catch (IOException e) {
      throw new TerminalCommandException("Can't get output lines on terminal command: " + command, e);
    }
  }

  /**
   * Creates and starts a terminal process for the specified command.
   *
   * @param command the terminal command to execute.
   * @return a {@link Process} representing the running command.
   * @throws TerminalCommandException if an error occurs while starting the command.
   */
  @NonNull
  private static Process getProcess(@NonNull final String command) {
    try {
      String[] terminalCommand = getTerminalCommand(command);
      ProcessBuilder processBuilder = new ProcessBuilder(terminalCommand);
      return processBuilder.start();
    } catch (IOException e) {
      throw new TerminalCommandException("Can't start the terminal command: " + command, e);
    }
  }

  /**
   * Formats a terminal command based on the operating system.
   *
   * @param command the raw command to execute.
   * @return an array of strings representing the formatted command.
   */
  @NonNull
  private static String @NonNull [] getTerminalCommand(@NonNull final String command) {
    if (OperatingSystemTools.getOperatingSystem().equals(OperatingSystem.WINDOWS)) {
      return WINDOWS_CMD_EXE.concat(command).split(" ");
    } else {
      return command.split(" ");
    }
  }

  /**
   * Executes a terminal command and returns the result as a {@link TerminalDto}.
   *
   * @param command the terminal command to execute.
   * @return a {@link TerminalDto} containing the command, exit code, result, and timestamp.
   */
  @NonNull
  public static TerminalDto executeCommand(@NonNull final String command) {
    LocalDateTime time = LocalDateTime.now();
    Process process = getProcess(command);
    String result = getReturn(command, process);
    int exitCode = getExitCode(command, process);

    return new TerminalDto(command, exitCode, result, time);
  }
}
