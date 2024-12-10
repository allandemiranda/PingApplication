package com.byborgenterprises.assemblers;

import com.byborgenterprises.dtos.TerminalDto;
import com.byborgenterprises.embeddables.Terminal;
import com.byborgenterprises.mappers.TerminalMapper;
import lombok.NonNull;

/**
 * The TerminalMapperAssembler class implements {@link TerminalMapper} to provide
 * mapping logic between {@link Terminal} entities and {@link TerminalDto} data transfer objects.
 *
 * <p>This class facilitates the conversion between the entity and DTO representations
 * of terminal operations, ensuring consistency and reusability across the application.
 *
 * <p>By encapsulating the mapping logic, this assembler separates data transformation
 * concerns from business logic.
 */
public class TerminalMapperAssembler implements TerminalMapper {

  @Override
  public @NonNull TerminalDto toDto(@NonNull Terminal terminal) {
    return new TerminalDto(terminal.getCommand(), terminal.getExitCode(), terminal.getResult(), terminal.getTime());
  }

  @Override
  public @NonNull Terminal toEntity(@NonNull TerminalDto terminalDto) {
    Terminal terminal = new Terminal(terminalDto.command(), terminalDto.result(), terminalDto.time());
    terminal.setExitCode(terminalDto.exitCode());
    return terminal;
  }
}
