package com.byborgenterprises;

import com.byborgenterprises.configs.BatchJobsConfig;
import com.byborgenterprises.factories.BatchJobsFactory;

public class SimplePingApp {

  /**
   * The configuration for batch jobs, initialized using the {@link BatchJobsFactory}.
   */
  private static final BatchJobsConfig batchJobsConfig = new BatchJobsFactory();

  public static void main(String[] args) {
    batchJobsConfig.startWorkflow();
  }
}

