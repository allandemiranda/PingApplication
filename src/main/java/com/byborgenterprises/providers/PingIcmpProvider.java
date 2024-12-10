package com.byborgenterprises.providers;

import com.byborgenterprises.dtos.PingIcmpDto;
import com.byborgenterprises.dtos.TerminalDto;
import com.byborgenterprises.embeddables.Terminal;
import com.byborgenterprises.entities.PingIcmp;
import com.byborgenterprises.enums.OperatingSystem;
import com.byborgenterprises.mappers.PingIcmpMapper;
import com.byborgenterprises.mappers.TerminalMapper;
import com.byborgenterprises.repositories.PingIcmpRepository;
import com.byborgenterprises.services.PingIcmpService;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

/**
 * The PingIcmpProvider class implements {@link PingIcmpService} to provide functionality
 * for managing ICMP ping operations. It interacts with repositories and mappers to handle
 * the creation, update, and retrieval of ICMP ping data.
 *
 * <p>This class encapsulates the business logic for processing ICMP ping operations,
 * including determining the appropriate command based on the operating system,
 * evaluating ping success, and transforming data between entity and DTO representations.
 */
@RequiredArgsConstructor
@Getter(AccessLevel.PRIVATE)
public class PingIcmpProvider implements PingIcmpService {

  private static final String HOST_TARGET = "HOST";
  private static final String WINDOWS_RESULT_TARGET = " = 0 (0% ";
  private static final String PORT_TARGET = ":";
  private final String pingCommandWindows;
  private final String pingCommandLinux;
  private final PingIcmpRepository pingIcmpRepository;
  private final PingIcmpMapper pingIcmpMapper;
  private final TerminalMapper terminalMapper;

  /**
   * Creates a {@link PingIcmp} entity with the given details.
   *
   * @param host the hostname or IP address.
   * @param terminal the terminal execution details.
   * @param success whether the ping operation was successful.
   * @return a {@link PingIcmp} entity.
   */
  @NonNull
  private static PingIcmp getPingIcmp(@NonNull final String host, @NonNull final Terminal terminal, final boolean success) {
    PingIcmp pingIcmp = new PingIcmp(host, terminal);
    pingIcmp.setSuccess(success);
    return pingIcmp;
  }

  @Override
  public @NonNull String getTerminalCommand(@NonNull String host, @NonNull OperatingSystem operatingSystem) {
    String hostFiltrated = host.contains(PORT_TARGET) ? host.substring(0, host.indexOf(PORT_TARGET)) : host;
    return switch (operatingSystem) {
      case LINUX -> this.getPingCommandLinux().replace(HOST_TARGET, hostFiltrated);
      case WINDOWS -> this.getPingCommandWindows().replace(HOST_TARGET, hostFiltrated);
    };
  }

  @Override
  @Synchronized
  public @NonNull PingIcmpDto createOrUpdatePing(@NonNull String host, @NonNull TerminalDto terminalDto, @NonNull OperatingSystem os) {
    Terminal terminal = this.getTerminalMapper().toEntity(terminalDto);
    boolean success = switch (os) {
      case WINDOWS -> terminalDto.exitCode() == 0 && terminalDto.result().contains(WINDOWS_RESULT_TARGET);
      case LINUX -> terminalDto.exitCode() == 0;
    };

    PingIcmp pingIcmp = getPingIcmp(host, terminal, success);

    PingIcmp save = this.getPingIcmpRepository().save(pingIcmp);
    return this.getPingIcmpMapper().toDto(save);
  }

  @Override
  public @NonNull Optional<PingIcmpDto> getPing(@NonNull String host) {
    return this.getPingIcmpRepository().findById(host).map(pingIcmp -> this.getPingIcmpMapper().toDto(pingIcmp));
  }
}
