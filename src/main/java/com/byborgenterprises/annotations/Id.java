package com.byborgenterprises.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code @Id} annotation is used to mark fields as identifiers within a class.
 *
 * <p>It is retained at runtime, allowing reflection-based tools to access the annotated fields.
 * The annotation can only be applied to fields.
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * public class Entity {
 *     @Id
 *     private Long id;
 * }
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {

}
