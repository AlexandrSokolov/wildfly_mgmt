package com.savdev.demo.wf.rest;

import com.savdev.demo.wf.AsyncWrapper;
import com.savdev.demo.wf.service.LongRunningService;
import com.savdev.demo.wf.service.LongRunningServiceWrongTry;
import org.jboss.ejb3.annotation.TransactionTimeout;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.TimeUnit;

@Stateless
@Path(RestService.PATH)
@Produces({MediaType.APPLICATION_JSON})
public class RestService {

  public static final String PATH = "/service";

  @Inject
  LongRunningServiceWrongTry serviceFailedTry;

  @Inject
  LongRunningService service;

  @Inject
  AsyncWrapper asyncWrapper;

  /**
   * Global WF timeout in `standalone-full.xml` is set to 5 seconds (`<coordinator-environment ... default-timeout="5"/>`)
   *
   */
  @GET
  @Path("/fails")
  public String fails(){
    try {
      TimeUnit.SECONDS.sleep(10);
      return "Success!";
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Global WF timeout in `standalone-full.xml` is set to 5 seconds (`<coordinator-environment ... default-timeout="5"/>`)
   * The configuration in `src/main/webapp/WEB-INF/jboss-ejb3.xml` extend it to 10 seconds only for this method
   *
   */
  @GET
  @Path("/xml")
  public String xmlConfigured(){
    try {
      TimeUnit.SECONDS.sleep(10);
      return "Success! Timeout is controlled via xml configuration in `jboss-ejb3.xml`";
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Global WF timeout in `standalone-full.xml` is set to 5 seconds (`<coordinator-environment ... default-timeout="5"/>`)
   * The configuration in `src/main/webapp/WEB-INF/jboss-ejb3.xml` extend it to 10 seconds only for this method
   *
   */
  @GET
  @Path("/annotation")
  @TransactionTimeout(value = 1, unit = TimeUnit.MINUTES)
  public String annotationBased(){
    try {
      TimeUnit.SECONDS.sleep(10);
      return "Success! Timeout is controlled via `@TransactionTimeout` annotation";
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * @TransactionTimeout applied to `LongRunningServiceWrongTry#longRunning` is ignored
   * @return
   */
  @GET
  @Path("/wrong/try")
  public String failedTry(){
    return serviceFailedTry.longRunning();
  }

  @GET
  @Path("/long")
  @TransactionTimeout(value = 1, unit = TimeUnit.MINUTES)
  public String successTry(){
    return service.longRunning()
      + " JEE timeout is configured via `@TransactionTimeout` applied to the rest endpoint, but not to the service itself";
  }

  @GET
  @Path("/run/and/forget")
  public String submit(){
    asyncWrapper.runAsynchronously(service::longRunning);
    return "Async task successfully submitted";
  }
}
