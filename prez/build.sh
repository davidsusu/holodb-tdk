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
    drawio --export --format png --scale 2.5 --output "${diagramPngFilename}" "${diagramFilename}"
    optipng -o7 -zm1-9 "${diagramPngFilename}"
    
    diagramSvgFilename="$( echo "${diagramFilename}" | sed -E 's/.drawio$/.svg/' )"
    drawio --export --format svg --output "${diagramSvgFilename}" "${diagramFilename}"
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
