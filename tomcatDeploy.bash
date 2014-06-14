#!/bin/bash
#
# This script requires:
# 1. set the variable of CATALINA_HOME
# 2. set path to CATALIHA bin.
#
git pull /git/prmSB.git master

gradle -b build.war.gradle clean build
shutdown.sh
sleep 5s
rm -rf ${CATALINA_HOME}/webapps/prm*
cp build/libs/prm*.war ${CATALINA_HOME}/webapps/prm.war
startup.sh
