package com.byborgenterprises.providers;

import com.byborgenterprises.dtos.ReportDto;
import com.byborgenterprises.enums.HttpMethod;
import com.byborgenterprises.exceptions.ReportException;
import com.byborgenterprises.utils.NetworkTools;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportProviderTest {

  @InjectMocks
  private ReportProvider reportProvider;

  @BeforeEach
  void setUp() {
    String reportUrl = "http://localhost:3000/report";
    reportProvider = new ReportProvider(reportUrl);
  }

  @Test
  void testSendReport_Success() {
    //given
    ReportDto reportDto = new ReportDto("host", null, null, null);
    URI uri = Mockito.mock(URI.class);
    HttpResponse<String> httpResponse = Mockito.mock(HttpResponse.class);
    try (MockedStatic<NetworkTools> networkTools = Mockito.mockStatic(NetworkTools.class)) {
      //when
      networkTools.when(() -> NetworkTools.getUri(Mockito.anyString(), Mockito.eq(Map.of()))).thenReturn(uri);
      networkTools.when(
              () -> NetworkTools.httpRequest(Mockito.eq(uri), Mockito.eq(Map.of("Content-Type", "application/json")), Mockito.eq(HttpMethod.POST), Mockito.anyString(), Mockito.eq(10000L)))
          .thenReturn(httpResponse);
      Mockito.when(httpResponse.statusCode()).thenReturn(200);
      Executable executable = () -> reportProvider.sendReport(reportDto);
      //then
      Assertions.assertDoesNotThrow(executable);
    }
  }

  @Test
  void testSendReport_ReportException() {
    //given
    ReportDto reportDto = new ReportDto("host", null, null, null);
    URI uri = Mockito.mock(URI.class);
    try (MockedStatic<NetworkTools> networkTools = Mockito.mockStatic(NetworkTools.class)) {
      //when
      networkTools.when(() -> NetworkTools.getUri(Mockito.anyString(), Mockito.eq(Map.of()))).thenReturn(uri);
      networkTools.when(
              () -> NetworkTools.httpRequest(Mockito.eq(uri), Mockito.eq(Map.of("Content-Type", "application/json")), Mockito.eq(HttpMethod.POST), Mockito.anyString(), Mockito.eq(10000L)))
          .thenThrow(IOException.class);
      Executable executable = () -> reportProvider.sendReport(reportDto);
      //then
      Assertions.assertThrows(ReportException.class, executable);
    }
  }

  @Test
  void testSendReport_NullReportDto() {
    //given
    Executable executable = () -> reportProvider.sendReport(null);
    //then
    Assertions.assertThrows(NullPointerException.class, executable);

  }
}
