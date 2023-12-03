#!/bin/sh

rootDirectory="$( realpath "$( dirname "$0" )" )"
startDirectory="$( realpath "$( pwd )" )"

cd "${rootDirectory}/micronaut-db-benchmark"

./gradlew run -q --console=plain &
backendPid=$!
echo "${backendPid}"

while [ ! -f 'RUNNING.lock' ]; do
    echo 'Waiting for lock...'
    sleep 1;
done

cd "${rootDirectory}/micronaut-db-benchmark-client"

./gradlew run -q --console=plain

kill "${backendPid}"

cd "${startDirectory}"
