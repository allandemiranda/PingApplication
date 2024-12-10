package com.byborgenterprises.exceptions;

import com.byborgenterprises.annotations.Id;
import com.byborgenterprises.repositories.OrchestratorRepository;
import lombok.experimental.StandardException;

/**
 * The OrchestratorRepositoryException class represents a custom runtime exception used
 * for handling errors specific to repository operations in the {@link OrchestratorRepository}.
 *
 * <p>This exception is typically thrown in scenarios such as:
 * <ul>
 *   <li>Failures to access or process fields annotated with {@link Id}.</li>
 *   <li>Errors encountered during save or update operations in repositories.</li>
 *   <li>Issues arising from reflection-based access to entity metadata.</li>
 * </ul>
 *
 * <p>It extends {@link RuntimeException} and is annotated with
 * {@link StandardException} to provide standard constructors.
 */
@StandardException
public class OrchestratorRepositoryException extends RuntimeException {

}
