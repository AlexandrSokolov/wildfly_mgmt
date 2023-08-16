package com.savdev.demo.wf.service;

import org.jboss.ejb3.annotation.TransactionTimeout;

import javax.ejb.Stateless;
import java.util.concurrent.TimeUnit;

/**
 * If transaction has already started, you cannot overwrite timeout.
 * As a result, you must set transaction not on the method that takes long, but from a point, where it is invoked
 *  and this point can be: rest service/jms message/async method.
 *
 * In this case `LongRunningServiceWrongTry#longRunning` is invoked from `RestService#failedTry()`
 *  and `@TransactionTimeout`, configured for `longRunning()` is just ignored and not applied.
 *
 *  You'll get transaction exceptions.
 *
 */
@Stateless
public class LongRunningServiceWrongTry {

  @TransactionTimeout(value = 1, unit = TimeUnit.MINUTES)
  public String longRunning() {
    try {
      TimeUnit.SECONDS.sleep(10);
      return "Success!";
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }
  }
}
