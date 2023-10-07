#!/bin/sh

name="$( cat 'name.txt' )"

cid="$( docker ps -a -q --filter ancestor="$name" --format='{{.ID}}')"

if [ -z "$cid" ]; then
    "No container found"
    exit 2
fi

if docker rm -f "$cid"; then
    echo "Container ${cid} successfully stopped"
else
    echo "Failed to stop container ${cid}"
    exit 1
fi
