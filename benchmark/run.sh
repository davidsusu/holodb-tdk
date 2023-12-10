#!/bin/sh

# prepare environment

dbType="$1"
if [ -z "${dbType}" ]; then
    dbType='holodb'
fi

benchmarkTestType="$2"
if [ -z "${dbType}" ]; then
    dbType='simple-readonly'
fi

rootDirectory="$( realpath "$( dirname "$0" )" )"
startDirectory="$( realpath "$( pwd )" )"

cd "${rootDirectory}"

. "env/${dbType}.env.sh"

if [ "${dbType}" = 'mysql' ]; then
    ./db/mysql-reset.py
elif [ "${dbType}" = 'sqlite' ]; then
    rm -f ./db/db.sqlite
    cp ./db/db-start.sqlite ./db/db.sqlite
fi


# prepare projects

cd "${rootDirectory}/micronaut-db-benchmark"
./gradlew build --console=plain >/dev/null
rm -f './log/out.log'
cd "${rootDirectory}/micronaut-db-benchmark-client"
./gradlew build --console=plain >/dev/null


# run tests

echo -n "${dbType} ${benchmarkTestType} "

cd "${rootDirectory}/micronaut-db-benchmark"

rm -f RUNNING.lock

./gradlew run -q --console=plain >/dev/null 2>&1 &
backendPid=$!

while [ ! -f 'RUNNING.lock' ]; do
    sleep 1;
done

cd "${rootDirectory}/micronaut-db-benchmark-client"

startTime="$( date +%s.%N )"

./gradlew run -q --console=plain --args="\"${benchmarkTestType}\"" >/dev/null 2>&1

endTime="$( date +%s.%N )"

kill "${backendPid}"

cd "${startDirectory}"


# summarize

fullTestSeconds="$( echo "$endTime - $startTime" | bc -l )"
fullSqlPreparationSeconds="$( egrep -o '[0-9]+ nanoseconds spent preparing [0-9]+ JDBC statements;' "${rootDirectory}/micronaut-db-benchmark/log/out.log" | awk '{s+=$1} END {print s / 1000000000}' )"
fullSqlExecutionSeconds="$( egrep -o '[0-9]+ nanoseconds spent executing [0-9]+ JDBC statements;' "${rootDirectory}/micronaut-db-benchmark/log/out.log" | awk '{s+=$1} END {print s / 1000000000}' )"
fullSqlExecutionCount="$( egrep -o '[0-9]+ nanoseconds spent executing [0-9]+ JDBC statements;' "${rootDirectory}/micronaut-db-benchmark/log/out.log" | awk '{s+=$5} END {print s}' )"

echo "${fullTestSeconds} ${fullSqlExecutionCount} ${fullSqlPreparationSeconds} ${fullSqlExecutionSeconds}"
