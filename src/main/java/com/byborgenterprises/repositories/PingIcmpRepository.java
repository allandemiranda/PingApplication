package com.byborgenterprises.repositories;

import com.byborgenterprises.entities.PingIcmp;

/**
 * The PingIcmpRepository class is a specialized implementation of
 * {@link OrchestratorRepository} for managing {@link PingIcmp} entities.
 *
 * <p>This repository uses a {@link String} key to uniquely identify
 * instances of {@link PingIcmp} and provides CRUD operations inherited
 * from the base class.
 */
public class PingIcmpRepository extends OrchestratorRepository<String, PingIcmp> {

}
