#!/bin/sh

numberOfRuns=20

./gradlew run -q --console=plain | grep -E '^Total size:'

printf '%s' ','
for j in $(seq 0 "$numberOfRuns"); do
    printf '%s' '_'
done
printf ',\n '
total=0
for j in $(seq 0 "$numberOfRuns"); do
    output="$( ./gradlew run -q --console=plain )"
    printf '%s' 'O'
    startTimestamp="$( printf '%s\n' "$output" | grep -E '^Start timestamp:' | sed -E 's/^.*\(//; s/\)$//' )"
    readyTimestamp="$( printf '%s\n' "$output" | grep -E '^Ready timestamp:' | sed -E 's/^.*\(//; s/\)$//' )"
    diff=$(( readyTimestamp - startTimestamp ))
    total=$(( total + diff ))
done
printf '\n '

avg=$(( total / numberOfRuns ))

printf 'Total: %s\n' "$total"
printf 'Number: %s\n' "$numberOfRuns"
printf 'AVG: %s\n' "$avg"
