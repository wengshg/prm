#!/bin/bash
#
# This script requires:
# 1. set the variable of CATALINA_HOME
# 2. set path to CATALIHA bin.
#
if [[ "$1" == "up" ]];then
   git pull origin master
fi

if [[ "$1" != "nocompile" ]];then
	gradle -b build.war.gradle clean build
fi
shutdown.sh
sleep 5s
rm -rf ${CATALINA_HOME}/logs/*
rm -rf ${CATALINA_HOME}/webapps/prm*
cp build/libs/prm*.war ${CATALINA_HOME}/webapps/prm.war
startup.sh
sleep 10s
tail -f ${CATALINA_HOME}/logs/catalina.out
