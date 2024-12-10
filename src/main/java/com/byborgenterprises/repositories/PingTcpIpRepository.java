package com.byborgenterprises.repositories;

import com.byborgenterprises.entities.PingTcpIp;
import java.net.URI;

/**
 * The PingTcpIpRepository class is a specialized implementation of
 * {@link OrchestratorRepository} for managing {@link PingTcpIp} entities.
 *
 * <p>This repository uses a {@link URI} key to uniquely identify
 * instances of {@link PingTcpIp} and provides CRUD operations inherited
 * from the base class.
 */
public class PingTcpIpRepository extends OrchestratorRepository<URI, PingTcpIp> {

}
