#!/bin/bash

apt-get install \
    texlive-latex-base texlive-latex-recommended texlive-lang-european \
    texlive-latex-extra texlive-bibtex-extra texlive-fonts-extra \
    texlive-luatex texlive-pictures texlive-pstricks texlive-science \
    hunspell hunspell-tools hunspell-hu \
    inkscape imagemagick optipng \
    uuid-runtime \
;

if ! command -v drawio &> /dev/null; then
    downloadUrl='https://github.com/jgraph/drawio-desktop/releases/download/v22.1.2/drawio-amd64-22.1.2.deb'
    outputFile="/tmp/$( uuidgen ).deb"
    curl --location "${downloadUrl}" --output "${outputFile}"
    dpkg -i "${outputFile}"
    rm -f "${outputFile}"
else
    echo 'drawio is already installed'
fi
