package com.byborgenterprises.requests;

import com.byborgenterprises.dtos.ReportDto;
import com.byborgenterprises.enums.ResponseStatus;
import com.byborgenterprises.exceptions.ReportException;
import com.byborgenterprises.factories.ResponseFactory;
import com.byborgenterprises.services.ReportService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportRequestTest {

  @Mock
  private ReportService reportService;

  @InjectMocks
  private ReportRequest reportRequest;

  @ParameterizedTest
  @ValueSource(ints = {200, 201})
  void testPostReport_Success(int code) {
    // given
    ReportDto reportDto = new ReportDto("host", null,null, null);
    // when
    Mockito.when(reportService.sendReport(reportDto)).thenReturn(code);
    ResponseFactory<Void> response = reportRequest.postReport(reportDto);
    // then
    Assertions.assertNotNull(response);
    Assertions.assertEquals(ResponseStatus.OK, response.getStatus());
  }

  @ParameterizedTest
  @ValueSource(ints = {400, 404, 500, 503})
  void testPostReport_FailureOtherCode(int code) {
    // given
    ReportDto reportDto = new ReportDto("host", null,null, null);
    // when
    Mockito.when(reportService.sendReport(reportDto)).thenReturn(code);
    ResponseFactory<Void> response = reportRequest.postReport(reportDto);
    // then
    Assertions.assertNotNull(response);
    Assertions.assertEquals(ResponseStatus.SERVICE_UNAVAILABLE, response.getStatus());
  }

  @Test
  void testPostReport_ReportException() {
    // given
    ReportDto reportDto = new ReportDto("host", null,null, null);
    Mockito.when(reportService.sendReport(reportDto)).thenThrow(new ReportException("Test Exception"));
    // when
    ResponseFactory<Void> response = reportRequest.postReport(reportDto);
    // then
    Assertions.assertNotNull(response);
    Assertions.assertEquals(ResponseStatus.SERVICE_UNAVAILABLE, response.getStatus());
    Assertions.assertEquals("Test Exception", response.getException().getMessage());
  }

  @Test
  void testPostReport_GeneralException() {
    // given
    ReportDto reportDto = new ReportDto("host", null,null, null);
    Mockito.when(reportService.sendReport(reportDto)).thenThrow(new RuntimeException("Unexpected Error"));
    // when
    ResponseFactory<Void> response = reportRequest.postReport(reportDto);
    // then
    Assertions.assertNotNull(response);
    Assertions.assertEquals(ResponseStatus.INTERNAL_SERVER_ERROR, response.getStatus());
    Assertions.assertEquals("Unexpected Error", response.getException().getMessage());
  }

  @Test
  void testPostReport_NullReportDto() {
    // given
    ReportDto reportDto = null;
    // when
    Executable executable = () -> reportRequest.postReport(reportDto);
    // then
    Assertions.assertThrows(NullPointerException.class, executable);
  }
}
