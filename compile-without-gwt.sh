#!/bin/bash
#mvn -Dgwt.compiler.skip=true compile source:jar javadoc:jar package install
mvn -Dgwt.compiler.skip=true compile source:jar package install
