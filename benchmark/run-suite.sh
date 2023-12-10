#!/bin/bash

#tests='simple-readonly complex-readonly'
tests='complex-writeable'
backends='mysql h2 holodb-low holodb holodb-fine'
repeats=10

date '+%Y-%m-%d %H:%M:%S.%N'
echo ''

# pre
./run.sh 'holodb-fine' > /dev/null

for test in $tests; do
    echo "# ${test}:"
    for backend in $backends; do
        for i in $( seq 1 $repeats ); do
            while echo "$( cat /proc/loadavg | cut -d' ' -f1 )" 1 | awk '{exit !( $1 > $2)}'; do
                sleep 1
            done
            sleep 5
            ./run.sh "${backend}" "${test}"
        done
    done
    echo ''
done

date '+%Y-%m-%d %H:%M:%S.%N'
