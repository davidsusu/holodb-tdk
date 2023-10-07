#!/bin/sh

set -e

docker build -t miniconnect/holodb-demo "$( dirname "$0" )"
