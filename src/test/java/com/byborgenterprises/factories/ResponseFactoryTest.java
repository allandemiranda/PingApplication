package com.byborgenterprises.factories;

import com.byborgenterprises.enums.ResponseStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ResponseFactoryTest {

  @Test
  void testResponseFactoryBuilder_OK() {
    //given
    String responseData = "Sample Response";
    ResponseStatus status = ResponseStatus.OK;
    //when
    ResponseFactory<String> responseFactory = ResponseFactory.<String>builder().response(responseData).status(status).build();
    //then
    Assertions.assertNotNull(responseFactory);
    Assertions.assertEquals(responseData, responseFactory.getResponse());
    Assertions.assertEquals(status, responseFactory.getStatus());
    Assertions.assertNull(responseFactory.getException());
    Assertions.assertNull(responseFactory.getMessage());
  }

  @Test
  void testResponseFactoryBuilder_SERVICE_UNAVAILABLE() {
    //given
    ResponseStatus status = ResponseStatus.SERVICE_UNAVAILABLE;
    String message = "An error occurred.";
    //when
    ResponseFactory<String> responseFactory = ResponseFactory.<String>builder().status(status).message(message).build();
    //then
    Assertions.assertNotNull(responseFactory);
    Assertions.assertNull(responseFactory.getResponse());
    Assertions.assertEquals(status, responseFactory.getStatus());
    Assertions.assertNull(responseFactory.getException());
    Assertions.assertEquals(message, responseFactory.getMessage());
  }

  @Test
  void testResponseFactoryBuilder_INTERNAL_SERVER_ERROR() {
    //given
    ResponseStatus status = ResponseStatus.INTERNAL_SERVER_ERROR;
    Exception exception = new IllegalArgumentException("Invalid input");
    //when
    ResponseFactory<String> responseFactory = ResponseFactory.<String>builder().status(status).exception(exception).build();
    //then
    Assertions.assertNotNull(responseFactory);
    Assertions.assertNull(responseFactory.getResponse());
    Assertions.assertEquals(status, responseFactory.getStatus());
    Assertions.assertNull(responseFactory.getMessage());
    Assertions.assertEquals(exception, responseFactory.getException());
  }
}