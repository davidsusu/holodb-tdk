#!/bin/bash

inputFileName='index.tex'
outputFileName='output.pdf'

baseName="$( echo "${inputFileName}" | sed 's/\.[^.]*$//' )"

mkdir -p build
rm -Rf build/*

cp -R ./src/. ./build

cd build

command="pdflatex --shell-escape -interaction=nonstopmode --synctex=1 '${inputFileName}'"

eval "${command}"
bibtex "${baseName}"
eval "${command}"
eval "${command}"

cd ..

rm -f ./*.out ./*.dvi

mkdir -p out
cp -f "build/${baseName}.pdf" "out/${outputFileName}"
synctexOutputFileName="$( printf '%s' "$outputFileName" | sed -E 's/\.pdf$//' ).synctex.gz"
gzip -dc "build/${baseName}.synctex.gz" | sed -E 's/\/build\/.\/(\w+).(cls|tex)$/\/src\/\1.\2/' | gzip > "out/${synctexOutputFileName}"
