package com.savdev.demo.wf;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.Set;

@ApplicationPath(JaxRsWsConfiguration.APPLICATION_PATH)
public class JaxRsWsConfiguration extends Application {

  public static final String APPLICATION_PATH = "/rest";

  @Override
  public Set<Class<?>> getClasses() {
    return Collections.singleton(RestService.class);
  }
}
