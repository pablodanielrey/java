#!/bin/bash
mvn -Dgwt.compiler.skip=true source:jar javadoc:jar compile
mvn -Dgwt.compiler.skip=true package install
