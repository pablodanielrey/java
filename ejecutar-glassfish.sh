#!/bin/bash
#MAVEN_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000" mvn embedded-glassfish:deploy embedded-glassfish:run
#MAVEN_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000" mvn package install
mvn package install
