package com.byborgenterprises.providers;

import com.byborgenterprises.dtos.ReportDto;
import com.byborgenterprises.enums.HttpMethod;
import com.byborgenterprises.exceptions.ReportException;
import com.byborgenterprises.services.ReportService;
import com.byborgenterprises.utils.NetworkTools;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * The ReportProvider class implements {@link ReportService} to handle the
 * generation and submission of reports to an external API.
 *
 * <p>This class serializes {@link ReportDto} objects into JSON using the Jackson library
 * and utilizes {@link NetworkTools} to perform HTTP requests for sending reports.
 */
@Log4j2
@RequiredArgsConstructor
@Getter(AccessLevel.PRIVATE)
public class ReportProvider implements ReportService {

  private final String reportUrl;

  /**
   * Serializes a {@link ReportDto} into a JSON string.
   *
   * @param reportDto the report data to serialize.
   * @return a JSON string representation of the report.
   * @throws ReportException if the serialization process fails.
   */
  @NonNull
  private static String getBody(@NonNull final ReportDto reportDto) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.registerModule(new JavaTimeModule());
      objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      return objectMapper.writeValueAsString(reportDto);
    } catch (JsonProcessingException e) {
      throw new ReportException("Unable to serialize Report information " + reportDto + " to JSON", e);
    }
  }

  @Override
  public int sendReport(@NonNull ReportDto reportDto) {
    String body = getBody(reportDto);
    log.warn("Report JSON: {}", body);

    Map<String, String> header = Map.of("Content-Type", "application/json");
    HttpMethod method = HttpMethod.POST;
    URI uri = NetworkTools.getUri(this.getReportUrl(), Map.of());

    try {
      HttpResponse<String> httpResponse = NetworkTools.httpRequest(uri, header, method, body, 10000L);
      return httpResponse.statusCode();
    } catch (IOException e) {
      throw new ReportException("Unable to send report " + reportDto + " to the API", e);
    }

  }
}
