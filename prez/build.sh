#!/bin/bash

inputFileName='index.tex'
outputFileName='presentation.pdf'

mode="$1"
if [ -z "$mode" ]; then
    mode='final'
fi

pngScale="$( echo 'draft:1 quick:4 final:4' | tr ' ' '\n' | egrep "^${mode}:" | sed -E 's/[a-z]+://' )"

baseName="$( echo "${inputFileName}" | sed 's/\.[^.]*$//' )"

mkdir -p build
rm -Rf build/*

cp -R ./src/. ./build

cd build/diagram

if ! [ "${mode}" = 'draft' ]; then
    ls ./ --color=never | egrep '^.*\.drawio$' | while IFS=' ' read -r diagramFilename; do
        diagramFilenameBase="$( echo "${diagramFilename}" | sed -E 's/.drawio$//' )"
        
        if ! [ "${mode}" = 'final' ]; then
            if ! fgrep --quiet "${diagramFilenameBase}" "../${inputFileName}"; then
                echo "${diagramFilenameBase} not found in input"
                continue
            fi
        fi
        
        diagramPngFilename="${diagramFilenameBase}.png"
        drawio --export --format png --scale "${pngScale}" --output "${diagramPngFilename}" "${diagramFilename}"
        if [ "${mode}" = 'final' ]; then
            optipng -o7 -zm1-9 "${diagramPngFilename}"
        elif [ "${mode}" = 'final' ]; then
            optipng -o7 -zm1-9 "${diagramPngFilename}"
        fi
        
        diagramPngFilename="${diagramFilenameBase}.svg"
        drawio --export --format svg --output "${diagramSvgFilename}" "${diagramFilename}"
        if [ "${mode}" = 'final' ]; then
            inkscape "${diagramSvgFilename}" --export-text-to-path --export-overwrite
        fi
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
