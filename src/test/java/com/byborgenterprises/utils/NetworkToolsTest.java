package com.byborgenterprises.utils;

import com.byborgenterprises.exceptions.NetworkToolsException;
import java.net.URI;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NetworkToolsTest {

  @Test
  void testGetUri_WithParameters() {
    // given
    String protocol = "https";
    String host = "example.com";
    Map<String, String> parameters = Map.of("key1", "value1", "key2", "value2");
    // when
    URI uri = NetworkTools.getUri(protocol, host, parameters);
    // then
    Assertions.assertNotNull(uri);
    Assertions.assertTrue(uri.toString().contains("key2=value2"));
    Assertions.assertTrue(uri.toString().contains("key1=value1"));
  }

  @Test
  void testGetUri_WithoutParameters() {
    // given
    String protocol = "https";
    String host = "example.com";
    Map<String, String> parameters = Map.of();
    // when
    URI uri = NetworkTools.getUri(protocol, host, parameters);
    // then
    Assertions.assertNotNull(uri);
    Assertions.assertEquals("https://example.com", uri.toString());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "a a"})
  void testGetUri_InvalidHost(String host) {
    // given
    String protocol = "https";
    Map<String, String> parameters = Map.of();
    // when
    Executable executable = () -> NetworkTools.getUri(protocol, host, parameters);
    // then
    Assertions.assertThrows(NetworkToolsException.class, executable);
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "a a"})
  void testGetUri_InvalidProtocol(String protocol) {
    // given
    String host = "example.com";
    Map<String, String> parameters = Map.of();
    // when
    Executable executable = () -> NetworkTools.getUri(protocol, host, parameters);
    // then
    Assertions.assertThrows(NetworkToolsException.class, executable);
  }

  @Test
  void testGetUri_NullProtocol() {
    // given
    String protocol = null;
    String host = "example.com";
    Map<String, String> parameters = Map.of();
    // when
    Executable executable = () -> NetworkTools.getUri(protocol, host, parameters);
    // then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testGetUri_NullHost() {
    // given
    String protocol = "https";
    String host = null;
    Map<String, String> parameters = Map.of();
    // when
    Executable executable = () -> NetworkTools.getUri(protocol, host, parameters);
    // then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testGetUri_NullParameters() {
    // given
    String protocol = "https";
    String host = "example.com";
    Map<String, String> parameters = null;
    // when
    Executable executable = () -> NetworkTools.getUri(protocol, host, parameters);
    // then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testGetUri_EmptyParameters() {
    // given
    String protocol = "https";
    String host = "example.com";
    Map<String, String> parameters = Map.of();
    // when
    URI uri = NetworkTools.getUri(protocol, host, parameters);
    // then
    Assertions.assertNotNull(uri);
    Assertions.assertEquals("https://example.com", uri.toString());
  }
}
