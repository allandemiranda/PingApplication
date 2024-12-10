package com.byborgenterprises.providers;

import com.byborgenterprises.dtos.PingIcmpDto;
import com.byborgenterprises.dtos.TerminalDto;
import com.byborgenterprises.embeddables.Terminal;
import com.byborgenterprises.entities.PingIcmp;
import com.byborgenterprises.enums.OperatingSystem;
import com.byborgenterprises.mappers.PingIcmpMapper;
import com.byborgenterprises.mappers.TerminalMapper;
import com.byborgenterprises.repositories.PingIcmpRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PingIcmpProviderTest {

  @Mock
  private PingIcmpRepository pingIcmpRepository;

  @Mock
  private PingIcmpMapper pingIcmpMapper;

  @Mock
  private TerminalMapper terminalMapper;

  @InjectMocks
  private PingIcmpProvider pingIcmpProvider;

  @BeforeEach
  void setUp() {
    String pingCommandWindows = "ping -n 5 HOST";
    String pingCommandLinux = "ping -c 5 HOST";

    pingIcmpProvider = new PingIcmpProvider(pingCommandWindows, pingCommandLinux, pingIcmpRepository, pingIcmpMapper, terminalMapper);
  }

  @Test
  void testGetTerminalCommand_Linux() {
    //given
    String host = "example.com";
    OperatingSystem os = OperatingSystem.LINUX;
    String expectedCommand = "ping -c 5 example.com";
    //when
    String command = pingIcmpProvider.getTerminalCommand(host, os);
    //then
    Assertions.assertEquals(expectedCommand, command);
  }

  @Test
  void testGetTerminalCommand_Windows() {
    //given
    String host = "example.com";
    OperatingSystem os = OperatingSystem.WINDOWS;
    String expectedCommand = "ping -n 5 example.com";
    //when
    String command = pingIcmpProvider.getTerminalCommand(host, os);
    //then
    Assertions.assertEquals(expectedCommand, command);
  }

  @Test
  void testGetTerminalCommand_HostWithPort() {
    //given
    String host = "example.com:8080";
    OperatingSystem os = OperatingSystem.LINUX;
    String expectedCommand = "ping -c 5 example.com";
    //when
    String command = pingIcmpProvider.getTerminalCommand(host, os);
    //then
    Assertions.assertEquals(expectedCommand, command);
  }

  @Test
  void testGetTerminalCommand_NullOperatingSystem() {
    //given
    String host = "example.com";
    //when
    Executable executable = () -> pingIcmpProvider.getTerminalCommand(host, null);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testGetTerminalCommand_NullHost() {
    //given
    OperatingSystem os = Mockito.mock(OperatingSystem.class);
    //when
    Executable executable = () -> pingIcmpProvider.getTerminalCommand(null, os);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testGetTerminalCommand_NullHostAndOperatingSystem() {
    //given
    //when
    Executable executable = () -> pingIcmpProvider.getTerminalCommand(null, null);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testCreateOrUpdatePing_Success_Linux() {
    //given
    String host = "example.com";
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    Terminal terminal = Mockito.mock(Terminal.class);
    PingIcmp pingIcmp = Mockito.mock(PingIcmp.class);
    PingIcmpDto pingIcmpDto = new PingIcmpDto("", terminalDto, true);
    OperatingSystem os = OperatingSystem.LINUX;
    //when
    Mockito.when(terminalMapper.toEntity(terminalDto)).thenReturn(terminal);
    Mockito.when(pingIcmpRepository.save(Mockito.any(PingIcmp.class))).thenReturn(pingIcmp);
    Mockito.when(pingIcmpMapper.toDto(pingIcmp)).thenReturn(pingIcmpDto);
    PingIcmpDto result = pingIcmpProvider.createOrUpdatePing(host, terminalDto, os);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(pingIcmpDto, result);
  }

  @Test
  void testCreateOrUpdatePing_Success_Windows() {
    //given
    String host = "example.com";
    TerminalDto terminalDto = new TerminalDto("", 0, "** = 0 (0% **", LocalDateTime.now());
    Terminal terminal = Mockito.mock(Terminal.class);
    PingIcmp pingIcmp = Mockito.mock(PingIcmp.class);
    PingIcmpDto pingIcmpDto = new PingIcmpDto("", terminalDto, true);
    OperatingSystem os = OperatingSystem.WINDOWS;
    //when
    Mockito.when(terminalMapper.toEntity(terminalDto)).thenReturn(terminal);
    Mockito.when(pingIcmpRepository.save(Mockito.any(PingIcmp.class))).thenReturn(pingIcmp);
    Mockito.when(pingIcmpMapper.toDto(pingIcmp)).thenReturn(pingIcmpDto);
    PingIcmpDto result = pingIcmpProvider.createOrUpdatePing(host, terminalDto, os);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(pingIcmpDto, result);
  }

  @Test
  void testCreateOrUpdatePing_HostWithPort() {
    //given
    String host = "example.com:8080";
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    Terminal terminal = Mockito.mock(Terminal.class);
    PingIcmp pingIcmp = Mockito.mock(PingIcmp.class);
    PingIcmpDto pingIcmpDto = new PingIcmpDto("", terminalDto, false);
    OperatingSystem os = OperatingSystem.LINUX;
    //when
    Mockito.when(terminalMapper.toEntity(terminalDto)).thenReturn(terminal);
    Mockito.when(pingIcmpRepository.save(Mockito.any(PingIcmp.class))).thenReturn(pingIcmp);
    Mockito.when(pingIcmpMapper.toDto(pingIcmp)).thenReturn(pingIcmpDto);
    PingIcmpDto result = pingIcmpProvider.createOrUpdatePing(host, terminalDto, os);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(pingIcmpDto, result);
  }

  @Test
  void testCreateOrUpdatePing_NullHost() {
    //given
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    OperatingSystem os = OperatingSystem.LINUX;
    //when
    Executable executable = () -> pingIcmpProvider.createOrUpdatePing(null, terminalDto, os);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testCreateOrUpdatePing_NullTerminalDto() {
    //given
    String host = "example.com";
    OperatingSystem os = OperatingSystem.LINUX;
    //when
    Executable executable = () -> pingIcmpProvider.createOrUpdatePing(host, null, os);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testCreateOrUpdatePing_NullOperatingSystem() {
    //given
    String host = "example.com";
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    //when
    Executable executable = () -> pingIcmpProvider.createOrUpdatePing(host, terminalDto, null);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testGetPing_Existing() {
    //given
    String host = "example.com";
    PingIcmp pingIcmp = Mockito.mock(PingIcmp.class);
    TerminalDto terminalDto = new TerminalDto("", 0, "", LocalDateTime.now());
    PingIcmpDto pingIcmpDto = new PingIcmpDto("", terminalDto, true);
    //when
    Mockito.when(pingIcmpRepository.findById(host)).thenReturn(Optional.of(pingIcmp));
    Mockito.when(pingIcmpMapper.toDto(pingIcmp)).thenReturn(pingIcmpDto);
    Optional<PingIcmpDto> result = pingIcmpProvider.getPing(host);
    //then
    Assertions.assertTrue(result.isPresent());
    Assertions.assertEquals(pingIcmpDto, result.get());
  }

  @Test
  void testGetPing_NotFound() {
    //given
    String host = "example.com";
    //when
    Mockito.when(pingIcmpRepository.findById(host)).thenReturn(Optional.empty());
    Optional<PingIcmpDto> result = pingIcmpProvider.getPing(host);
    //then
    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  void testGetPing_NullHost() {
    //given
    //when
    Executable executable = () -> pingIcmpProvider.getPing(null);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }
}