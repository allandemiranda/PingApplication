package com.byborgenterprises.mappers;

import com.byborgenterprises.dtos.TerminalDto;
import com.byborgenterprises.embeddables.Terminal;
import lombok.NonNull;

/**
 * The TerminalMapper interface provides methods for mapping Terminal entities
 * to their corresponding Data Transfer Object (DTO) representations and vice versa.
 */
public interface TerminalMapper {

  /**
   * Converts a {@link Terminal} entity to a {@link TerminalDto}.
   *
   * @param terminal the Terminal entity to be converted.
   * @return the corresponding {@link TerminalDto} representation of the entity.
   */
  @NonNull
  TerminalDto toDto(@NonNull final Terminal terminal);

  /**
   * Converts a {@link TerminalDto} to a {@link Terminal} entity.
   *
   * @param terminalDto the Terminal Data Transfer Object to be converted.
   * @return the corresponding {@link Terminal} entity representation of the DTO.
   */
  @NonNull
  Terminal toEntity(@NonNull final TerminalDto terminalDto);

}
