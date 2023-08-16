package com.savdev.demo.wf;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(RestService.PATH)
@Produces({MediaType.APPLICATION_JSON})
public class RestService {

  public static final String PATH = "/service";

  @GET
  public String echo(){
    return "Success!";
  }
}
