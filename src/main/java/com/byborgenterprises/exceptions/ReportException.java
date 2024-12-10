package com.byborgenterprises.exceptions;

import com.byborgenterprises.providers.ReportProvider;
import lombok.experimental.StandardException;

/**
 * The ReportException class represents a custom runtime exception used
 * for handling errors specific to report generation and management in the application.
 *
 * <p>This exception is typically thrown in the {@link ReportProvider}
 * class during scenarios such as:
 * <ul>
 *   <li>Failures to fetch or process report data from external sources.</li>
 *   <li>Errors encountered during the creation or submission of reports.</li>
 *   <li>Issues arising from invalid or incomplete report configurations.</li>
 * </ul>
 *
 * <p>It extends {@link RuntimeException} and is annotated with
 * {@link StandardException} to provide standard constructors.
 */
@StandardException
public class ReportException extends RuntimeException {

}
