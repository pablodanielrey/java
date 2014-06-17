#!/bin/bash
cd /java/github/java/
mvn eclipse:clean eclipse:eclipse

cd gwt

cd auth
mvn eclipse:clean eclipse:eclipse

cd ..; cd assistance
mvn eclipse:clean eclipse:eclipse

cd ..; cd person
mvn eclipse:clean eclipse:eclipse

