#!/bin/bash

inputFileName='index.tex'
outputFileName='dolgozat.pdf'
jobName="$( uuidgen )"

mkdir -p build
rm -Rf build/*

cp -R ./src/. ./build

cd build

ls diagram/ --color=never | egrep '^.*\.drawio$' | while IFS=' ' read -r diagramFilename; do
    diagramSvgFilename="$( echo "${diagramFilename}" | sed -E 's/.drawio$/.svg/' )"
    drawio -x -f svg -o "diagram/${diagramSvgFilename}" "diagram/${diagramFilename}"
    inkscape "diagram/${diagramSvgFilename}" --export-text-to-path --export-overwrite
done

command="pdflatex --shell-escape -interaction=nonstopmode -jobname='${jobName}' '${inputFileName}'"

eval "$command" | exit 0
bibtex "$inputFileName"
makeindex -s nomencl.ist -t index.nlg -o index.nls index.nlo # FIXME
eval "$command"
eval "$command"

cd ..

mkdir -p out
cp -f "build/${jobName}.pdf" "out/${outputFileName}"
rm -f ./*.out ./*.dvi
