#!/bin/bash
mvn source:jar javadoc:jar compile
mvn -Dgwt.compiler.skip=true package install
