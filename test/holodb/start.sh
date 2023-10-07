#!/bin/sh

innerPort=3430
hostPort="$innerPort"
if [ -n "$1" ]; then
    hostPort="$1"
fi

name="$( cat 'name.txt' )"

if docker run -p "$hostPort":"$innerPort" -d "$name"; then
   echo "HoloDB is listening on port $hostPort"
else
   echo "HoloDB startup failed"
   exit 1
fi
