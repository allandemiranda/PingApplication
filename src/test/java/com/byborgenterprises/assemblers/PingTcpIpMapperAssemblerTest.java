package com.byborgenterprises.assemblers;

import com.byborgenterprises.dtos.PingTcpIpDto;
import com.byborgenterprises.entities.PingTcpIp;
import java.net.URI;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PingTcpIpMapperAssemblerTest {

  @InjectMocks
  private PingTcpIpMapperAssembler pingTcpIpMapperAssembler;

  @Test
  void testToDto_success() {
    //given
    URI uri = Mockito.mock(URI.class);
    LocalDateTime localDateTime = Mockito.mock(LocalDateTime.class);
    PingTcpIp pingTcpIp = new PingTcpIp(uri, localDateTime);
    int responseCode = 200;
    long responseTime = 1000L;
    boolean success = true;
    //when
    pingTcpIp.setResponseCode(responseCode);
    pingTcpIp.setResponseTime(responseTime);
    pingTcpIp.setSuccess(success);
    PingTcpIpDto pingTcpIpDto = pingTcpIpMapperAssembler.toDto(pingTcpIp);
    //then
    Assertions.assertNotNull(pingTcpIpDto);
    Assertions.assertEquals(uri, pingTcpIpDto.url());
    Assertions.assertEquals(localDateTime, pingTcpIpDto.time());
    Assertions.assertEquals(responseCode, pingTcpIpDto.responseCode());
    Assertions.assertEquals(responseTime, pingTcpIpDto.responseTime());
    Assertions.assertEquals(success, pingTcpIpDto.success());

  }

  @Test
  void testToDto_nullInput() {
    //given
    //when
    Executable executable = () -> pingTcpIpMapperAssembler.toDto(null);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }
}