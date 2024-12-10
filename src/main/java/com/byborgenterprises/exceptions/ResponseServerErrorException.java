package com.byborgenterprises.exceptions;

import lombok.experimental.StandardException;

/**
 * The ResponseServerErrorException class represents a custom runtime exception used
 * for handling server-related errors encountered during batch job execution.
 *
 * <p>This exception is typically thrown in the {@link com.byborgenterprises.factories.BatchJobsFactory}
 * class during scenarios such as:
 * <ul>
 *   <li>Receiving a 5xx HTTP status code from an external API.</li>
 *   <li>Failures to process server responses during job execution.</li>
 *   <li>Handling unexpected server errors that disrupt batch job workflows.</li>
 * </ul>
 *
 * <p>It extends {@link RuntimeException} and is annotated with
 * {@link lombok.experimental.StandardException} to provide standard constructors.
 */
@StandardException
public class ResponseServerErrorException extends RuntimeException {

}
