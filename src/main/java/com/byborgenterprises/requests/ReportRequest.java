package com.byborgenterprises.requests;

import com.byborgenterprises.controllers.ReportController;
import com.byborgenterprises.dtos.ReportDto;
import com.byborgenterprises.enums.ResponseStatus;
import com.byborgenterprises.exceptions.ReportException;
import com.byborgenterprises.factories.ResponseFactory;
import com.byborgenterprises.services.ReportService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * The ReportRequest class implements {@link ReportController} to handle the
 * submission of reports via the {@link ReportService}.
 *
 * <p>This class encapsulates the logic for posting reports to an external API,
 * managing exceptions, and constructing standardized responses using
 * {@link ResponseFactory} and {@link ResponseStatus}.
 */
@RequiredArgsConstructor
@Getter(AccessLevel.PRIVATE)
public class ReportRequest implements ReportController {

  private final ReportService reportService;

  @Override
  public @NonNull ResponseFactory<Void> postReport(@NonNull ReportDto reportDto) {
    try {
      int code = this.getReportService().sendReport(reportDto);
      if (code == 200 || code == 201) {
        return ResponseFactory.<Void>builder().status(ResponseStatus.OK).build();
      } else {
        throw new ReportException("Error to send report for API, HTTP code: " + code);
      }
    } catch (ReportException e) {
      return ResponseFactory.<Void>builder().status(ResponseStatus.SERVICE_UNAVAILABLE).exception(e).build();
    } catch (Exception e) {
      return ResponseFactory.<Void>builder().status(ResponseStatus.INTERNAL_SERVER_ERROR).exception(e).build();
    }
  }
}
