#### Project description:

* [Project requirements](#project-requirements)
* [Build and run](#build-and-run)
* [Send GET requests](#send-requests)

[JEE Timeout, theory](#jee-timeout-theory)

#### JEE Timeout options
* [WildFly global configuration](#jee-timeout-management)

#### Project requirements:

* Apache Maven 3.6.3
* OpenJDK 1.8
* Docker

#### Build and run

Run `./start.sh` file. 

The script:
* Builds application with Maven
* Builds Docker image
* Runs the container with the http (`8080`) and debugging ports (`8787`) exposed

#### Send requests

Send request that fails because of timeout:
```bash
curl -i -X GET -w "\n" http://localhost:8080/jee/timeout/rest/service/fails
```

Send successful request. Timeout is configured via `@TransactionTimeout` annotation:
```bash
curl -i -X GET -w "\n" http://localhost:8080/jee/timeout/rest/service/annotation
```

Send successful request. Timeout is configured in `jboss-ejb3.xml`:
```bash
curl -i -X GET -w "\n" http://localhost:8080/jee/timeout/rest/service/xml
```

Send request that fails because of timeout wrong configuration.
Rest `RestService#failedTry()` endpoint is not configured explicitly.
The `@TransactionTimeout` applied to `LongRunningServiceWrongTry#longRunning` is ignored:
```bash
curl -i -X GET -w "\n" http://localhost:8080/jee/timeout/rest/service/wrong/try
```

Send requests that invoke the method of injected beans:
```bash
curl -i -X GET -w "\n" http://localhost:8080/jee/timeout/rest/service/long
curl -i -X GET -w "\n" http://localhost:8080/jee/timeout/rest/service/run/and/forget
```


#### JEE Timeout, theory and requirements

1. JEE (or EJB) timeout management and configuration is applied only to JEE beans, but not to CDI beans. 
As a result it is applied only to beans with: `@Stateless`, `@Statefull`, `@Singleton` or other JEE annotations from `javax.ejb` package.

2. You cannot overwrite timeout, if transaction has already been started. As a result if you configure JEE timeout, apply it:
* In case you use rest services on the rest entry points 
  (methods of rest service, see 
  [`RestService#xmlConfigured` and `RestService#annotationBased`](src/main/java/com/savdev/demo/wf/rest/RestService.java))
* In case you invoke methods asynchronously, configure not the method, that takes long, see 
  [`LongRunningServiceWrongTry#longRunning`](src/main/java/com/savdev/demo/wf/service/LongRunningServiceWrongTry.java), 
  but instead configure the async method that invokes the long running method, see 
  [`AsyncWrapper#runAsynchronously`](src/main/java/com/savdev/demo/wf/AsyncWrapper.java) and its usage.
* If you start a cron job and trigger a long-running process from it, you have 2 options:
  * to use 
    [`AsyncWrapper#runAsynchronously`](src/main/java/com/savdev/demo/wf/AsyncWrapper.java) and see its usage in
    [`ApplicationAsyncTimer`](src/main/java/com/savdev/demo/wf/cron/ApplicationAsyncTimer.java)
  * or to apply `@TransactionTimeout` directly on a method with `@Timeout` annotation, see:
    [`ApplicationSyncTimer`](src/main/java/com/savdev/demo/wf/cron/ApplicationSyncTimer.java)
* If you use MDB TODO

3. If you apply the JEE timeout configuration to method, such method must be public.

#### WildFly JEE timeout annotation-based configuration

See `RestService#annotationBased()` method and timeout configuration via `@TransactionTimeout` annotation.

```pom.xml
    <dependency>
      <groupId>org.jboss.ejb3</groupId>
      <artifactId>jboss-ejb3-ext-api</artifactId>
      <version>2.1.0</version>
      <scope>provided</scope>
    </dependency>
```

#### WildFly JEE timeout xml-based configuration

See `RestService#xmlConfigured()` method and timeout configuration in `src/main/webapp/WEB-INF/jboss-ejb3.xml`


#### WildFly JEE timout global configuration

You can controle JEE timeout per WildFly. Default value is 5 minutes (300 seconds). 
It is configured via `default-timeout` in `coordinator-environment`:
```xml
    <subsystem xmlns="urn:jboss:domain:transactions:3.0">
      ...
      <coordinator-environment statistics-enabled="false" enable-tsm-status="true" default-timeout="5"/>
    </subsystem>
```