FROM jboss/wildfly:10.1.0.Final

USER jboss
COPY --chown=jboss [ "files", "/opt/jboss/wildfly/" ]
ADD target/jee-timeout-demo-1.0.0.war /opt/jboss/wildfly/standalone/deployments/

# http port:
EXPOSE 8080
# debugging port:
EXPOSE 8787

#to run without starting wildfly for investigation only:
#CMD ["tail", "-f", "/dev/null" ]
#overwrite with "standalone-full.xml":
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-c", "standalone-full.xml", "-b", "0.0.0.0"]