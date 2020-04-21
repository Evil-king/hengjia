#!/bin/sh
PROJECT_DIR=$(cd "$(dirname "$0")"; pwd)
PROJECT_DIR=$PROJECT_DIR/..
CONFIG_DIR=$PROJECT_DIR/config

pid=`ps ax | grep -i "$CONFIG_DIR" |grep java | grep -v grep | awk '{print $1}'`
if [ -z "$pid" ] ; then
        echo "No process is running."
        exit -1;
fi

kill ${pid}
echo "The process ${pid} have been killed" 