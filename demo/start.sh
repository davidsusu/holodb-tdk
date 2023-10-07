#!/bin/sh

innerPort=3430
hostPort="$innerPort"
if [ -n "$1" ]; then
    hostPort="$1"
fi

if docker run -p "$hostPort":"$innerPort" -d miniconnect/holodb-demo:latest; then
   echo "HoloDB is listening on port $hostPort"
else
   echo "HoloDB startup failed"
fi
