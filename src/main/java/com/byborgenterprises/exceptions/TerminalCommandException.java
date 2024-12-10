package com.byborgenterprises.exceptions;

import lombok.experimental.StandardException;

/**
 * The TerminalCommandException class represents a custom runtime exception used
 * for handling errors related to terminal command execution.
 *
 * <p>This exception is typically thrown in the {@link com.byborgenterprises.utils.TerminalTools}
 * class during scenarios such as:
 * <ul>
 *   <li>Failures to capture the output of a terminal command.</li>
 *   <li>Errors encountered while waiting for a terminal process to complete.</li>
 *   <li>Handling unexpected I/O issues during terminal command execution.</li>
 * </ul>
 *
 * <p>It extends {@link RuntimeException} and is annotated with
 * {@link lombok.experimental.StandardException} to provide standard constructors.
 */
@StandardException
public class TerminalCommandException extends RuntimeException {

}
