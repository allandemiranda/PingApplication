package com.byborgenterprises.utils;

import com.byborgenterprises.enums.HttpMethod;
import com.byborgenterprises.exceptions.NetworkToolsException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * The NetworkTools class provides utility methods for network operations,
 * such as generating URIs, creating HTTP requests, and handling URL parameters.
 * This is a final utility class and cannot be instantiated.
 */
@UtilityClass
public final class NetworkTools {

  /**
   * Generates a URI based on the provided protocol, host, and parameters.
   *
   * @param protocol   the protocol to use (e.g., "http" or "https").
   * @param host       the host name or IP address.
   * @param parameters a map of query parameters to include in the URI.
   * @return a {@link URI} constructed from the given inputs.
   * @throws NetworkToolsException if the URI cannot be generated.
   */
  @NonNull
  public static URI getUri(@NonNull final String protocol, @NonNull final String host, @NonNull final Map<@NonNull String, @NonNull String> parameters) {
    try {
      return new URI(protocol + "://" + host + getUrlParameters(parameters));
    } catch (URISyntaxException e) {
      throw new NetworkToolsException("Not possible generate a URL from: '" + protocol + "://" + host + "'", e);
    }
  }

  /**
   * Generates a URI based on the provided URL and query parameters.
   *
   * @param url        the base URL.
   * @param parameters a map of query parameters to append to the URL.
   * @return a {@link URI} constructed from the given inputs.
   * @throws NetworkToolsException if the URI cannot be generated.
   */
  @NonNull
  public static URI getUri(@NonNull final String url, @NonNull final Map<@NonNull String, @NonNull String> parameters) {
    String urlParameters = getUrlParameters(parameters);
    String fullUrl = url + urlParameters;
    try {
      return new URI(fullUrl);
    } catch (URISyntaxException e) {
      throw new NetworkToolsException("Not possible generate a URI from: '" + fullUrl + "'", e);
    }
  }

  /**
   * Sends an HTTP request and returns the response.
   *
   * @param uri      the target URI.
   * @param method   the HTTP method to use (e.g., GET, POST).
   * @param timeout  the timeout duration for the request.
   * @return the {@link HttpResponse} received.
   * @throws IOException              if an I/O error occurs.
   * @throws NetworkToolsException    if the request cannot be created or sent.
   */
  @NonNull
  public static HttpResponse<String> httpRequest(@NonNull final URI uri, @NonNull final Map<String, String> header, @NonNull final HttpMethod method, @NonNull final String body,
      final long timeout) throws IOException {
    HttpResponse<String> response;
    try {
      HttpClient httpClient = HttpClient.newHttpClient();
      HttpRequest httpRequest = getHttpRequest(uri, header, method, body, timeout);
      response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new NetworkToolsException("HTTP client can't set a " + method.getMethodValue() + " request to url " + uri, e);
    }
    return response;
  }

  /**
   * Creates an HTTP request based on the provided URI, method, and timeout.
   *
   * @param uri      the target URI.
   * @param method   the HTTP method to use.
   * @param timeout  the timeout duration for the request.
   * @return the {@link HttpRequest} constructed.
   * @throws NetworkToolsException if the request cannot be created.
   */
  @NonNull
  private static HttpRequest getHttpRequest(@NonNull final URI uri, final @NonNull Map<@NonNull String, @NonNull String> header, final @NonNull HttpMethod method, @NonNull final String body,
      final long timeout) {
    final Builder builder = HttpRequest.newBuilder();
    // URI
    builder.uri(uri);
    // Header
    header.forEach(builder::header);
    // Body
    builder.method(method.getMethodValue(), HttpRequest.BodyPublishers.ofString(body));
    // Timeout
    builder.timeout(Duration.ofMillis(timeout));
    // Request
    return builder.build();
  }

  /**
   * Converts a map of parameters into a URL query string.
   *
   * @param parameters a map of query parameters.
   * @return a string representing the query parameters, starting with "?" if the map is not empty.
   */
  private static @NonNull String getUrlParameters(@NonNull Map<@NonNull String, @NonNull String> parameters) {
    if (!parameters.isEmpty()) {
      String parametersJoin = parameters.entrySet().stream().map(stringStringEntry -> stringStringEntry.getKey() + "=" + stringStringEntry.getValue()).collect(Collectors.joining("&"));
      return "?" + parametersJoin;
    } else {
      return "";
    }
  }
}
