package com.byborgenterprises.exceptions;

import com.byborgenterprises.utils.NetworkTools;
import lombok.experimental.StandardException;

/**
 * The NetworkToolsException class represents a custom runtime exception used
 * for handling errors related to network operations within the application.
 *
 * <p>This exception is typically thrown in the {@link NetworkTools}
 * class during scenarios such as:
 * <ul>
 *   <li>Failures in URI generation (e.g., invalid protocol or malformed URL).</li>
 *   <li>Errors during HTTP request creation or execution.</li>
 *   <li>Issues encountered while processing terminal command outputs.</li>
 * </ul>
 *
 * <p>It extends {@link RuntimeException} and is annotated with
 * {@link StandardException} to provide standard constructors.
 */
@StandardException
public class NetworkToolsException extends RuntimeException {

}
