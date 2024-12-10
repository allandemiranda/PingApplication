package com.byborgenterprises.assemblers;

import com.byborgenterprises.dtos.PingIcmpDto;
import com.byborgenterprises.dtos.TerminalDto;
import com.byborgenterprises.embeddables.Terminal;
import com.byborgenterprises.entities.PingIcmp;
import com.byborgenterprises.mappers.PingIcmpMapper;
import com.byborgenterprises.mappers.TerminalMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * The PingIcmpMapperAssembler class implements {@link PingIcmpMapper} to provide
 * mapping logic between {@link PingIcmp} entities and {@link PingIcmpDto} data transfer objects.
 *
 * <p>This class leverages the {@link TerminalMapper} to map the {@link Terminal}
 * field to its corresponding {@link TerminalDto} representation.
 *
 * <p>Designed to separate mapping logic from core business functionality, this assembler ensures
 * consistency and reusability in mapping operations.
 */
@RequiredArgsConstructor
@Getter(AccessLevel.PRIVATE)
public class PingIcmpMapperAssembler implements PingIcmpMapper {

  private final TerminalMapper terminalMapper;

  @Override
  public @NonNull PingIcmpDto toDto(@NonNull PingIcmp pingIcmp) {
    TerminalDto terminalDto = this.getTerminalMapper().toDto(pingIcmp.getTerminal());
    return new PingIcmpDto(pingIcmp.getHost(), terminalDto, pingIcmp.isSuccess());
  }
}
