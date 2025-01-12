#!/bin/bash

tests='simple-readonly complex-readonly complex-writeable benchmarks'
#backends='mysql h2 sqlite holodb-low holodb holodb-fine holoserver'
#backends='mysql h2 holodb-low holodb holodb-fine'
backends='h2'
repeats=1

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

date '+%Y-%m-%d %H:%M:%S.%N'
printf '\n'


printf 'pre . . . '
./run.sh 'holodb-fine' > /dev/null
printf 'OK\n'


for test in $tests; do
    printf '# %s:\n' "$test"
    for backend in $backends; do
        for i in $( seq 1 $repeats ); do
            #while printf '%s\n' "$( cat /proc/loadavg | cut -d' ' -f1 )" 1 | awk '{exit !( $1 > $2)}'; do
            #    sleep 1
            #done
            #sleep 5
            if ! ./run.sh "${backend}" "${test}"; then
                exit 1
            fi
        done
    done
    printf '\n'
done

date '+%Y-%m-%d %H:%M:%S.%N'
