#!/bin/bash

inputFileName='index.tex'
outputFileName='dolgozat.pdf'

mode="$1"
if [ -z "$mode" ]; then
    mode='final'
fi

baseName="$( echo "${inputFileName}" | sed 's/\.[^.]*$//' )"

mkdir -p build
rm -Rf build/*

cp -R ./src/. ./build

cd build/diagram

if ! [ "${mode}" = 'draft' ]; then
    ls ./ --color=never | egrep '^.*\.drawio$' | while IFS=' ' read -r diagramFilename; do
        diagramPngFilename="$( echo "${diagramFilename}" | sed -E 's/.drawio$/.png/' )"
        drawio --export --format png --scale 2.5 --output "${diagramPngFilename}" "${diagramFilename}"
        optipng -o7 -zm1-9 "${diagramPngFilename}"
        
        diagramSvgFilename="$( echo "${diagramFilename}" | sed -E 's/.drawio$/.svg/' )"
        drawio --export --format svg --output "${diagramSvgFilename}" "${diagramFilename}"
        inkscape "${diagramSvgFilename}" --export-text-to-path --export-overwrite
    done
fi

cd ..

if [ "${mode}" = 'draft' ]; then
    command="pdflatex --shell-escape -interaction=nonstopmode '\makeatletter\def\@classoptionslist{draft}\makeatother\input{${inputFileName}}'"
else
    command="pdflatex --shell-escape -interaction=nonstopmode '${inputFileName}'"
fi

eval "${command}"
bibtex "${baseName}"
eval "${command}"
eval "${command}"

cd ..

mkdir -p out
cp -f "build/${baseName}.pdf" "out/${outputFileName}"
rm -f ./*.out ./*.dvi
