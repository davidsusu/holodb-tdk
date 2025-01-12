#!/bin/sh

startDir="$( realpath "$( pwd )" )"

rm -rf tmp

. "./env/holodb-fine.env.sh"

csvPath="$( realpath "./db/csv" )"
mkdir -p "$csvPath"

cd micronaut-db-benchmark
./gradlew run --args="db-export-job -d ${csvPath}"
cd "$startDir"

. "./env/mysql.env.sh"
python3 db/mysql-init.py

mkdir -p tmp/git
cd tmp/git
git clone 'https://github.com/miniconnect/general-docs' general-docs
cd ..
cp -R git/general-docs/examples/holodb-standalone holodb-docker
cd holodb-docker
rm -rf .git
rm -f config.yaml
cp ../../micronaut-db-benchmark/src/main/resources/holodb-fine.yaml config.yaml
printf '%s\n' 'holodb-benchmark-sample-server' > name.txt
./build.sh
cd ..
rm -rf git
cd ..

cd "$startDir"
