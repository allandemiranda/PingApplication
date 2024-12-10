package com.byborgenterprises.exceptions;

import com.byborgenterprises.factories.BatchJobsFactory;
import lombok.experimental.StandardException;

/**
 * The BatchJobsException class represents a custom runtime exception used
 * for handling errors specific to the execution and management of batch jobs
 * in the {@link BatchJobsFactory}.
 *
 * <p>This exception is typically thrown in scenarios such as:
 * <ul>
 *   <li>Validation of responses during job execution (e.g., ICMP or TCP/IP pings).</li>
 *   <li>Errors encountered while generating reports for failed operations.</li>
 *   <li>Issues arising in the lifecycle of scheduled jobs.</li>
 * </ul>
 *
 * <p>It extends {@link RuntimeException} and is annotated with
 * {@link StandardException} to provide standard constructors.
 */
@StandardException
public class BatchJobsException extends RuntimeException {

}
