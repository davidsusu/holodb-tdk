#!/bin/sh

set -e

name="$( cat 'name.txt' )"

docker build -t "$name" "$( dirname "$0" )"
