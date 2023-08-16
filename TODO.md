### Logging log4j2 and commands to configure custom handler

### MDB and JEE transaction

[See `MDB`](jee_timeout/TODO.md#mdb)

### Add in docker project

$ docker run -p 8080:8080 -p 8187:8187 -e "JAVA_OPTS=-Ddebug -Xmx128m -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8187"   -e BM_SYSTEM_EXTERNAL_URL="https://vw-test.brandmaker.com"   -e BM_SYSTEM_ID="194-378-153"   cs-vw-assets-approval-report:1.0.21

-e "JAVA_OPTS=-Ddebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8787"  \