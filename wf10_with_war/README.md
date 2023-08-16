### Content:

* [Project requirements](#project-requirements)
* [Build and run](#build-and-run)
* [Send GET request](#send-request)
* [Custom docker image creation](#custom-docker-image-creation)

#### Project requirements:

* Apache Maven 3.6.3
* OpenJDK 1.8
* Docker
* `jboss/wildfly:10.1.0.Final` docker image (`docker pull jboss/wildfly:10.1.0.Final`)

#### Build and run

Run `./start.sh` file. 

The script:
* Builds application with Maven
* Build Docker image and
  * Overwrites certain configuration files, first of all `standalone-full.xml` and `standalone.conf`
  * Deploys the war file into WildFly (via runtime deployment into the `deployments` folder)
  * Runs WildFly in `standalone-full` mode
  * Runs WildFly with debugging option enabled (`-agentlib:jdwp=transport=dt_socket,address=8787,server=y,suspend=n`)
* Runs the container with the http (`8080`) and debugging ports (`8787`) exposed

#### Send request

```bash
curl -i -X GET -w "\n" http://localhost:8080/demo/rest/service
```

#### Custom docker image creation

1. Specify the parent `FROM jboss/wildfly:10.1.0.Final`
2. Overwrite the required configuration files
3. Highlight, which ports can/should be exposed
4. Run WildFly in `standalone-full` mode, with `standalone-full.xml` configuration
5. Enable debugging in `bin/standalone.conf` via `-agentlib:jdwp=transport=dt_socket,address=8787,server=y,suspend=n`