package com.byborgenterprises.dtos;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.NonNull;

/**
 * The TerminalDto record encapsulates the details of a terminal command execution.
 * It includes information about the command, its output, and the time it was executed.
 *
 * <p>This record is immutable and implements {@link Serializable} for data transfer.
 *
 * @param command the command executed in the terminal.
 * @param exitCode the exit code returned by the terminal after execution.
 * @param result the output or result of the terminal command.
 * @param time the {@link LocalDateTime} when the command was executed.
 */
public record TerminalDto(@NonNull String command, int exitCode, @NonNull String result, @NonNull LocalDateTime time) implements Serializable {

  @Serial
  private static final long serialVersionUID = 6161605489375734389L;
}
