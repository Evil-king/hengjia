#!/bin/sh
#变量设置
SERVICE_NAME=hengjia-gateway
mkdir -p /logs/$SERVICE_NAME
CONFIG_DIR=/apps/$SERVICE_NAME/config
LIB_DIR=/apps/$SERVICE_NAME/lib
LIB_JARS=`ls $LIB_DIR | grep .jar | awk '{print "'$LIB_DIR'/"$0}' | tr "\n" ":"`
MAIN_CLASS="com.baibei.hengjia.gateway.GatewayApplication"
mkdir -p  /logs/$SERVICE_NAME/
# 设置classpath
JAVA_OPT="-server -Xms4096M -Xmx4096M -Xmn1536M -Xss512k  -XX:+UseBiasedLocking -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+UseParNewGC  -XX:+CMSParallelRemarkEnabled -XX:LargePageSizeInBytes=128m  -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -Djava.awt.headless=true -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$OOMLOG_DIR"
nohup java $JAVA_OPT -Dapp.id=hengjia-gateway -Denv=pro -Dapollo.cluster=default -Dapollo.meta=http://192.168.11.231:8080 -classpath $CONFIG_DIR:$LIB_JARS $MAIN_CLASS >> /logs/$SERVICE_NAME/std-out.log 2>&1 &