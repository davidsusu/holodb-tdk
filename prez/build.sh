#!/bin/bash

mkdir -p build
rm -Rf build/*

cp src/* build/

cd build

#latex prez.tex && \
#  dvips -Ppdf prez.dvi && \
#  ps2pdf prez.ps;
pdflatex prez.tex;
pdflatex prez.tex;

cd ..

mkdir -p out
cp -f build/prez.pdf out/prez.pdf
rm -f ./*.out ./*.dvi
