package com.byborgenterprises.providers;

import com.byborgenterprises.dtos.PingTcpIpDto;
import com.byborgenterprises.entities.PingTcpIp;
import com.byborgenterprises.enums.HttpMethod;
import com.byborgenterprises.mappers.PingTcpIpMapper;
import com.byborgenterprises.repositories.PingTcpIpRepository;
import com.byborgenterprises.utils.NetworkTools;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PingTcpIpProviderTest {

  @Mock
  private PingTcpIpRepository pingTcpIpRepository;

  @Mock
  private PingTcpIpMapper pingTcpIpMapper;

  @Mock
  private PingTcpIpProvider pingTcpIpProvider;

  @BeforeEach
  void setUp() {
    pingTcpIpProvider = new PingTcpIpProvider(pingTcpIpRepository, pingTcpIpMapper, 5000L, "http");
  }

  @Test
  void testCreateOrUpdatePing_Success() {
    //given
    String host = "127.0.0.1";
    URI uri = Mockito.mock(URI.class);
    HttpResponse<String> httpResponse = Mockito.mock(HttpResponse.class);
    int statusCode = 200;
    LocalDateTime timestamp = Mockito.mock(LocalDateTime.class);
    PingTcpIp pingTcpIp = Mockito.mock(PingTcpIp.class);
    PingTcpIpDto pingTcpIpDto = new PingTcpIpDto(URI.create("http://localhost"), 0,0, LocalDateTime.now(), false);

    try (MockedStatic<NetworkTools> networkToolsMockedStatic = Mockito.mockStatic(NetworkTools.class); MockedStatic<LocalDateTime> localDateTimeMockedStatic = Mockito.mockStatic(
        LocalDateTime.class)) {
      //when
      localDateTimeMockedStatic.when(LocalDateTime::now).thenReturn(timestamp);
      networkToolsMockedStatic.when(() -> NetworkTools.getUri(Mockito.anyString(), Mockito.eq(host), Mockito.eq(Map.of()))).thenReturn(uri);
      networkToolsMockedStatic.when(
              () -> NetworkTools.httpRequest(Mockito.eq(uri), Mockito.eq(Map.of("Content-Type", "text/plain")), Mockito.eq(HttpMethod.GET), Mockito.eq(""), Mockito.eq(5000L)))
          .thenReturn(httpResponse);
      Mockito.when(httpResponse.statusCode()).thenReturn(statusCode);
      Mockito.when(pingTcpIpRepository.save(Mockito.any(PingTcpIp.class))).thenReturn(pingTcpIp);
      Mockito.when(pingTcpIpMapper.toDto(pingTcpIp)).thenReturn(pingTcpIpDto);
      Executable executable = () -> pingTcpIpProvider.createOrUpdatePing(host);
      //then
      Assertions.assertDoesNotThrow(executable);
    }
  }

  @Test
  void testCreateOrUpdatePing_Timeout() {
    //given
    String host = "127.0.0.1";
    URI uri = Mockito.mock(URI.class);
    LocalDateTime timestamp = Mockito.mock(LocalDateTime.class);
    try (MockedStatic<NetworkTools> networkToolsMockedStatic = Mockito.mockStatic(NetworkTools.class); MockedStatic<LocalDateTime> localDateTimeMockedStatic = Mockito.mockStatic(
        LocalDateTime.class)) {
      //when
      localDateTimeMockedStatic.when(LocalDateTime::now).thenReturn(timestamp);
      networkToolsMockedStatic.when(() -> NetworkTools.getUri(Mockito.anyString(), Mockito.eq(host), Mockito.eq(Map.of()))).thenReturn(uri);
      networkToolsMockedStatic.when(
              () -> NetworkTools.httpRequest(Mockito.eq(uri), Mockito.eq(Map.of("Content-Type", "text/plain")), Mockito.eq(HttpMethod.GET), Mockito.eq(""), Mockito.eq(5000L)))
          .thenThrow(IOException.class);
      Executable executable = () -> pingTcpIpProvider.createOrUpdatePing(host);
      //then
      Assertions.assertDoesNotThrow(executable);
    }
  }

  @Test
  void testCreateOrUpdatePing_HostNull() {
    //given
    //when
    Executable executable = () -> pingTcpIpProvider.createOrUpdatePing(null);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testGetPing_Success() {
    //given
    String host = "127.0.0.1";
    URI uri = Mockito.mock(URI.class);
    PingTcpIp pingTcpIp = Mockito.mock(PingTcpIp.class);
    PingTcpIpDto pingTcpIpDto = new PingTcpIpDto(URI.create("http://localhost"), 0,0, LocalDateTime.now(), false);
    try (MockedStatic<NetworkTools> networkToolsMockedStatic = Mockito.mockStatic(NetworkTools.class)) {
      //when
      networkToolsMockedStatic.when(() -> NetworkTools.getUri(Mockito.anyString(), Mockito.eq(host), Mockito.eq(Map.of()))).thenReturn(uri);
      Mockito.when(pingTcpIpRepository.findById(uri)).thenReturn(Optional.of(pingTcpIp));
      Mockito.when(pingTcpIpMapper.toDto(pingTcpIp)).thenReturn(pingTcpIpDto);
      Optional<PingTcpIpDto> optionalPingTcpIpDto = pingTcpIpProvider.getPing(host);
      //then
      Assertions.assertNotNull(optionalPingTcpIpDto);
      Assertions.assertTrue(optionalPingTcpIpDto.isPresent());
      Assertions.assertEquals(pingTcpIpDto, optionalPingTcpIpDto.get());
    }
  }

  @Test
  void testGetPing_HostNull() {
    //given
    URI uri = Mockito.mock(URI.class);
    try (MockedStatic<NetworkTools> networkToolsMockedStatic = Mockito.mockStatic(NetworkTools.class)) {
      //when
      networkToolsMockedStatic.when(() -> NetworkTools.getUri(Mockito.anyString(), Mockito.isNull(), Mockito.eq(Map.of()))).thenReturn(uri);
      Executable executable = () -> pingTcpIpProvider.getPing(null);
      //then
      Assertions.assertThrows(NullPointerException.class, executable);
    }
  }
}