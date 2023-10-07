#!/bin/bash

mkdir -p build
rm -Rf build/*

cp -R ./src/. ./build

cd build

pdflatex --shell-escape index.tex
bibtex index.tex
makeindex -s nomencl.ist -t index.nlg -o index.nls index.nlo
pdflatex --shell-escape index.tex
pdflatex --shell-escape index.tex

cd ..

mkdir -p out
cp -f build/index.pdf out/dolgozat.pdf
rm -f ./*.out ./*.dvi
