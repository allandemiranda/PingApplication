package com.byborgenterprises.exceptions;

import com.byborgenterprises.utils.OperatingSystemTools;
import lombok.experimental.StandardException;

/**
 * The OperatingSystemNotFoundException class represents a custom runtime exception used
 * for handling errors when the operating system cannot be identified.
 *
 * <p>This exception is typically thrown in the {@link OperatingSystemTools}
 * class during scenarios such as:
 * <ul>
 *   <li>Failure to match the current operating system against predefined options.</li>
 *   <li>Issues encountered while attempting to retrieve the "os.name" system property.</li>
 * </ul>
 *
 * <p>It extends {@link RuntimeException} and is annotated with
 * {@link StandardException} to provide standard constructors.
 */
@StandardException
public class OperatingSystemNotFoundException extends RuntimeException {

}
