#!/bin/sh


# helpers

checkStartsWith() { # $1: contextString, $2: prefixToCheck
    case "$1" in
        "$2"*) return 0 ;;
        *) return 1 ;;
    esac
}

checkEndsWith() { # $1: contextString, $2: prefixToCheck
    case "$1" in
        *"$2") return 0 ;;
        *) return 1 ;;
    esac
}


# prepare environment

dbType="$1"
if [ -z "${dbType}" ]; then
    dbType='holodb'
fi

benchmarkTestType="$2"
if [ -z "${benchmarkTestType}" ]; then
    benchmarkTestType='simple-readonly'
fi

rootDirectory="$( realpath "$( dirname "$0" )" )"
startDirectory="$( realpath "$( pwd )" )"

cd "${rootDirectory}"

. "env/${dbType}.env.sh"


# prepare services

if [ "${dbType}" = 'mysql' ]; then
    ./db/mysql-reset.py
elif [ "${dbType}" = 'sqlite' ]; then
    rm -f ./db/db.sqlite
    cp ./db/db-start.sqlite ./db/db.sqlite
fi

if checkStartsWith "$dbType" 'holoserver'; then
    cd "${rootDirectory}/tmp/holodb-docker"
    ./kill.sh > /dev/null
    ./start.sh > /dev/null
fi

cd "$rootDirectory"


# prepare project state

previousServerProcessPid="$( ps aux | grep -F java | grep -F 'micronaut-db-benchmark' | grep -F 'GradleWrapperMain' | head -n 1 | tr -s ' ' | cut -d ' ' -f 2 )"
if [ -n "$previousServerProcessPid" ]; then
    kill -9  "$previousServerProcessPid"
fi

resourcesDir="${rootDirectory}/micronaut-db-benchmark/src/main/resources"
if checkStartsWith "$dbType" 'holodb'; then
    mv "${resourcesDir}/${dbType}.yaml" "${resourcesDir}/${dbType}.yaml.bup"
    if checkEndsWith "$benchmarkTestType"; then
        sed -E 's/  writeable: true/  writeable: false/' "${resourcesDir}/${dbType}.yaml.bup" > "${resourcesDir}/${dbType}.yaml"
    else
        sed -E 's/  writeable: false/  writeable: true/' "${resourcesDir}/${dbType}.yaml.bup" > "${resourcesDir}/${dbType}.yaml"
    fi
fi

cd "${rootDirectory}/micronaut-db-benchmark"
./gradlew build --console=plain >/dev/null
rm -f './log/out.log'
./gradlew build --console=plain >/dev/null

cd "$rootDirectory"


# run tests

printf '%s %s ' "$dbType" "$benchmarkTestType"

cd "${rootDirectory}/micronaut-db-benchmark"

rm -f RUNNING.lock

./gradlew run -q --console=plain >/dev/null 2>&1 &
backendPid=$!

while [ ! -f 'RUNNING.lock' ]; do
    sleep 1
done
sleep 1

checkCurlOutput="$( curl 'http://localhost:8080/students/42' -H 'Content-type: application/json' -s )"
checkValue="$( printf '%s\n' "$checkCurlOutput" | jq .id )"
if [ "$checkValue" != '42' ]; then
    printf 'Invalid checkValue: %s (output: %s)' "$checkValue" "$checkCurlOutput"
    exit 1
fi

cd "${rootDirectory}/micronaut-db-benchmark-client"

startTime="$( date +%s.%N )"

benchmarkOutputs=''
if [ "$benchmarkTestType" = 'benchmarks' ]; then
    benchmarkOutputs="$( ./gradlew run -q --console=plain --args="\"${benchmarkTestType}\"" 2>/dev/null )"
else
    ./gradlew run -q --console=plain --args="\"${benchmarkTestType}\"" >/dev/null 2>&1
fi

endTime="$( date +%s.%N )"

kill "$backendPid"


# restore services

if checkStartsWith "$dbType" 'holodb' && [ -f "${resourcesDir}/${dbType}.yaml.bup" ]; then
    rm "${resourcesDir}/${dbType}.yaml"
    mv "${resourcesDir}/${dbType}.yaml.bup" "${resourcesDir}/${dbType}.yaml"
fi
if checkStartsWith "$dbType" 'holoserver'; then
    cd "${rootDirectory}/tmp/holodb-docker"
    ./kill.sh > /dev/null
fi

cd "$rootDirectory"


# restore caller status

cd "${startDirectory}"


# summarize

fullTestSeconds="$( printf '%s - %s\n' "$endTime" "$startTime" | bc -l )"
fullSqlPreparationSeconds="$( grep -Eo '[0-9]+ nanoseconds spent preparing [0-9]+ JDBC statements;' "${rootDirectory}/micronaut-db-benchmark/log/out.log" | awk '{s+=$1} END {print s / 1000000000}' )"
fullSqlExecutionSeconds="$( grep -Eo '[0-9]+ nanoseconds spent executing [0-9]+ JDBC statements;' "${rootDirectory}/micronaut-db-benchmark/log/out.log" | awk '{s+=$1} END {print s / 1000000000}' )"
fullSqlExecutionCount="$( grep -Eo '[0-9]+ nanoseconds spent executing [0-9]+ JDBC statements;' "${rootDirectory}/micronaut-db-benchmark/log/out.log" | awk '{s+=$5} END {print s}' )"

if [ -n "$benchmarkOutputs" ]; then
    printf '%s\n' "$benchmarkOutputs" | jq -r 'map((.n | tostring) + ": " + (.avgNanos | tostring) + " (×" + (.repeats | tostring) + ")") | join("    ")'
else
    printf 'FULL: %s    FULL-SQL: %s    FULL-PREP: %s    FULL-EXE: %s\n' "$fullTestSeconds" "$fullSqlExecutionCount" "$fullSqlPreparationSeconds" "$fullSqlExecutionSeconds"
fi
