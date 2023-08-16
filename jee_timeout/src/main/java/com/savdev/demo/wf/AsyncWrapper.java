package com.savdev.demo.wf;

import org.jboss.ejb3.annotation.TransactionTimeout;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import java.util.concurrent.TimeUnit;

@Stateless
public class AsyncWrapper {

  @Asynchronous
  @TransactionTimeout(value = 1, unit = TimeUnit.MINUTES)
  public void runAsynchronously(Executor executor) {
    executor.execute();
  }
}
