#!/bin/bash
mvn -Dgwt.compiler.skip=true compile source:jar javadoc:jar package install
