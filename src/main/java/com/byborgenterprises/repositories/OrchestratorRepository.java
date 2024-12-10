package com.byborgenterprises.repositories;

import com.byborgenterprises.annotations.Id;
import com.byborgenterprises.exceptions.OrchestratorRepositoryException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Synchronized;

/**
 * The OrchestratorRepository class provides a base implementation for managing entities
 * in a repository pattern inspired by JPA. It supports generic CRUD operations
 * such as saving, updating, and retrieving entities, with an emphasis on using
 * an in-memory data store for simplicity.
 *
 * <p>This class works in conjunction with classes typically ending in "Repository",
 * acting as a backbone for repository interactions. It identifies entities using
 * fields annotated with {@link Id} and ensures
 * the correct management of identifiers.
 *
 * <p>Designed to mimic the functionality of Spring Data JPA repositories,
 * this implementation leverages reflection to handle entity metadata.
 *
 * @param <K> the type of the key used to uniquely identify entities.
 * @param <E> the type of the entities managed by this repository.
 */
@Getter(AccessLevel.PRIVATE)
public abstract class OrchestratorRepository<K, E> {

  /**
   * The in-memory database represented as a concurrent map.
   *
   * <p>This map stores entities, using their identifiers as keys for quick access.
   */
  private final Map<K, E> dataBase = new ConcurrentHashMap<>();

  /**
   * Retrieves an entity by its unique identifier.
   *
   * @param id the unique identifier of the entity to retrieve.
   * @return an {@link Optional} containing the entity if found, or empty if not.
   */
  public Optional<E> findById(K id) {
    return Optional.ofNullable(this.getDataBase().getOrDefault(id, null));
  }

  /**
   * Saves or updates an entity in the repository.
   *
   * <p>If the entity already exists in the repository (based on its identifier),
   * it will be updated. Otherwise, it will be added as a new entry.
   *
   * @param entity the entity to save or update.
   * @return the saved or updated entity.
   * @throws OrchestratorRepositoryException if the entity does not have a field annotated with {@link Id}.
   */
  @NonNull
  @Synchronized
  public E save(@NonNull E entity) {
    Field idField = this.findIdField(entity.getClass()).orElseThrow(
        () -> new OrchestratorRepositoryException("No field annotated with @Id found in class: " + entity.getClass().getName(), new IllegalStateException("Entity Id not found")));

    K id = this.extractId(entity, idField);
    if (Objects.isNull(id)) {
      throw new OrchestratorRepositoryException("The @Id field in entity " + entity + " cannot be null", new IllegalStateException("Entity Id can not be null"));
    } else {
      this.getDataBase().put(id, entity);
      return this.getDataBase().getOrDefault(id, entity);
    }
  }

  /**
   * Finds the field annotated with {@link Id} in the specified class.
   *
   * <p>This method uses reflection to inspect the declared fields of the given class,
   * searching for a field annotated with {@link Id}.
   *
   * @param clazz the class to inspect for an {@link Id}-annotated field.
   * @return an {@link Optional} containing the field if found, or empty if not.
   */
  @NonNull
  private Optional<Field> findIdField(@NonNull Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Id.class)).findFirst();
  }

  /**
   * Extracts the identifier value from an entity using the specified field.
   *
   * @param entity the entity from which to extract the identifier.
   * @param idField the field annotated with {@link Id} containing the identifier.
   * @return the extracted identifier.
   * @throws OrchestratorRepositoryException if the identifier cannot be accessed.
   */
  @NonNull
  @SuppressWarnings("unchecked")
  private K extractId(@NonNull E entity, @NonNull Field idField) {
    try {
      MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(entity.getClass(), MethodHandles.lookup());
      VarHandle varHandle = lookup.findVarHandle(entity.getClass(), idField.getName(), idField.getType());
      return (K) varHandle.get(entity);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      throw new OrchestratorRepositoryException("Cannot access the @Id field in entity: " + entity, e);
    }
  }
}
