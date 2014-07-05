#!/bin/bash
#
# This script requires:
# 1. set the variable of CATALINA_HOME
# 2. set path to CATALIHA bin.
#
if [[ "$1" != "restart" ];then
   if [[ "$1" == "up" ]];then
      git pull origin master
   fi

   if [[ "$1" != "nocompile" ]];then
	   gradle -b build.war.gradle clean build
   fi
fi

shutdown.sh
sleep 5s

if [[ "$1" != "restart" ];then
   rm -rf ${CATALINA_HOME}/logs/*
   rm -rf ${CATALINA_HOME}/webapps/prm*
   cp build/libs/prm*.war ${CATALINA_HOME}/webapps/prm.war
fi
startup.sh
if [[ "$1" != "restart" ];then
   sleep 10s
fi
tail -f ${CATALINA_HOME}/logs/catalina.out
