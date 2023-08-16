#!/usr/bin/env bash

mvn clean package \
  && docker build --tag=savdev/wf-demo:1.0.0 . \
  && docker run -p 8080:8080 -p 8787:8787 savdev/wf-demo:1.0.0