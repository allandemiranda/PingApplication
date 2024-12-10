package com.byborgenterprises.embeddables;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NonNull;

/**
 * The Terminal class represents the details of a command executed in the terminal.
 * It includes information about the command, its output, and the time it was executed.
 *
 * <p>This class is serializable and intended to encapsulate terminal operation results.
 */
@Data
public class Terminal implements Serializable {

  @Serial
  private static final long serialVersionUID = 5332465895807914458L;

  /**
   * The command executed in the terminal.
   */
  @NonNull
  private String command;

  /**
   * The exit code returned by the terminal after execution.
   */
  private int exitCode;

  /**
   * The output or result of the terminal command.
   */
  @NonNull
  private String result;

  /**
   * The {@link LocalDateTime} when the command was executed.
   */
  @NonNull
  private LocalDateTime time;

}
