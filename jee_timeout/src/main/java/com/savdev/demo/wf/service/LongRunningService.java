package com.savdev.demo.wf.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.TimeUnit;

public class LongRunningService {

  private static final Log logger = LogFactory.getLog(LongRunningService.class);

  public String longRunning() {
    try {
      TimeUnit.SECONDS.sleep(10);
      logger.info("LongRunningService#longRunning completed!");
      return "Success!";
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }
  }
}
