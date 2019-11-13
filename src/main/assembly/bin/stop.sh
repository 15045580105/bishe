#!/bin/bash
set -e;
cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
PID_FILE=${DEPLOY_DIR}/pid;
if [[ ! -f ${PID_FILE} ]];then
    echo "$PID_FILE can not find, please check service is running !";
    rm -f ${PID_FILE}
    exit 0;
fi
CONF_DIR=${DEPLOY_DIR}/conf

SERVER_NAME=qianlima-reptile-statistics-service

if [[ -z "$SERVER_NAME" ]]; then
    SERVER_NAME=`hostname`
fi
if [[ "$1" != "skip" ]]; then
    ${BIN_DIR}/dump.sh
fi

echo -e "Stopping the $SERVER_NAME ...\c"

PID=$(cat ${PID_FILE});
cat ${PID_FILE} | xargs kill

COUNT=0
while [[ ${COUNT} -lt 1 ]]; do
    echo -e ".\c"
    sleep 1
    COUNT=1
    PID_EXIST=$(ps -ef |grep ${PID} | grep -v java)
    if [[ -n "${PID_EXIST}" ]]; then
        COUNT=0
        break
    fi
    done
pwd
rm -f ${PID_FILE}
echo "OK!"
echo "PID: $PID"
