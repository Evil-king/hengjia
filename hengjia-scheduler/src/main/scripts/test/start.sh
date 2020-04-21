#!/bin/sh
#变量设置
SERVICE_NAME=hengjia-scheduler
mkdir -p /logs/$SERVICE_NAME
CONFIG_DIR=/apps/$SERVICE_NAME/config
LIB_DIR=/apps/$SERVICE_NAME/lib
LIB_JARS=`ls $LIB_DIR | grep .jar | awk '{print "'$LIB_DIR'/"$0}' | tr "\n" ":"`
MAIN_CLASS="com.baibei.hengjia.scheduler.SchedulerApplication"
mkdir -p  /logs/$SERVICE_NAME/
# 设置classpath
nohup java -Dapp.id=hengjia-scheduler -Denv=dev -Dapollo.cluster=default -Dapollo.meta=http://192.168.100.134:8080 -classpath $CONFIG_DIR:$LIB_JARS $MAIN_CLASS >> /logs/$SERVICE_NAME/std-out.log 2>&1 &