#!/bin/bash

inputFileName='index.tex'
outputFileName='dolgozat.pdf'

baseName="$( echo "${inputFileName}" | sed 's/\.[^.]*$//' )"

mkdir -p build
rm -Rf build/*

cp -R ./src/. ./build

cd build/diagram

ls ./ --color=never | egrep '^.*\.drawio$' | while IFS=' ' read -r diagramFilename; do
    diagramPngFilename="$( echo "${diagramFilename}" | sed -E 's/.drawio$/.png/' )"
    drawio -x -f png -o "${diagramPngFilename}" "${diagramFilename}"
    
    diagramSvgFilename="$( echo "${diagramFilename}" | sed -E 's/.drawio$/.svg/' )"
    drawio -x -f svg -o "${diagramSvgFilename}" "${diagramFilename}"
    inkscape "${diagramSvgFilename}" --export-text-to-path --export-overwrite
done

cd ..

command="pdflatex --shell-escape -interaction=nonstopmode '${inputFileName}'"

eval "${command}"
bibtex "${baseName}"
eval "${command}"
eval "${command}"

cd ..

mkdir -p out
cp -f "build/${baseName}.pdf" "out/${outputFileName}"
rm -f ./*.out ./*.dvi
