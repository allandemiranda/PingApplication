package com.byborgenterprises.services;

import com.byborgenterprises.dtos.ReportDto;
import lombok.NonNull;

/**
 * The ReportService interface defines operations for managing the submission of reports
 * to an external API. It includes a method to send reports encapsulated in a {@link ReportDto}.
 */
public interface ReportService {

  /**
   * Sends a report represented to the external API by the specified {@link ReportDto}.
   *
   * @param reportDto the report data to be submitted.
   * @return an integer indicating the result of the report submission,
   *         such as a status code or the number of reports successfully sent.
   */
  int sendReport(@NonNull ReportDto reportDto);

}
