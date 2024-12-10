package com.byborgenterprises.repositories;

import com.byborgenterprises.entities.TraceRoute;

/**
 * The TraceRouteRepository class is a specialized implementation of
 * {@link OrchestratorRepository} for managing {@link TraceRoute} entities.
 *
 * <p>This repository uses a {@link String} key to uniquely identify
 * instances of {@link TraceRoute} and provides CRUD operations inherited
 * from the base class.
 */
public class TraceRouteRepository extends OrchestratorRepository<String, TraceRoute> {

}
