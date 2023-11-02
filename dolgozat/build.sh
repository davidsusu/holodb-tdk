#!/bin/bash

inputFileName='index.tex'
outputFileName='dolgozat.pdf'
jobName="$( uuidgen )"

mkdir -p build
rm -Rf build/*

cp -R ./src/. ./build

cd build/diagram

ls ./ --color=never | egrep '^.*\.drawio$' | while IFS=' ' read -r diagramFilename; do
    diagramSvgFilename="$( echo "${diagramFilename}" | sed -E 's/.drawio$/.svg/' )"
    drawio -x -f svg -o "${diagramSvgFilename}" "${diagramFilename}"
    inkscape "${diagramSvgFilename}" --export-text-to-path --export-overwrite
done

#ls ./ --color=never | egrep '^.*\.m$' | while IFS=' ' read -r diagramFilename; do
#    diagramNameBase="$( echo "${diagramFilename}" | sed -E 's/.m$//' )"
#    sed -iE "s/\boutputfilename\b/${diagramNameBase}/" "${diagramFilename}"
#    octave --no-gui --no-window-system "${diagramFilename}"
#done

cd ..

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
