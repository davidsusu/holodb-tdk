#!/bin/bash

# $1: mode: [draft|quick|edit|final] (default: final)


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
        diagramPngScale='1'
        if [ "${mode}" = 'final' ]; then
            diagramPngScale='2.5'
        fi
        
        drawio --export --format png --scale "${diagramPngScale}" --output "${diagramPngFilename}" "${diagramFilename}"
        
        if [ "${mode}" = 'final' ]; then
            optipng -o7 -zm1-9 "${diagramPngFilename}"
        fi
        
        diagramSvgFilename="$( echo "${diagramFilename}" | sed -E 's/.drawio$/.svg/' )"
        drawio --export --format svg --output "${diagramSvgFilename}" "${diagramFilename}"
    done
    #if [ "${mode}" = 'final' ]; then
        ls ./ --color=never | egrep '^.*\.svg$' | while IFS=' ' read -r svgFilename; do
            inkscape "${svgFilename}" --export-text-to-path --export-overwrite
        done
    #fi
fi

cd ..

if [ "${mode}" = 'draft' ]; then
    command="pdflatex --shell-escape -interaction=nonstopmode '\makeatletter\def\@classoptionslist{draft}\makeatother\input{${inputFileName}}'"
elif [ "${mode}" = 'edit' ]; then
    command="pdflatex --shell-escape -interaction=nonstopmode '${inputFileName}' --synctex=1"
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
