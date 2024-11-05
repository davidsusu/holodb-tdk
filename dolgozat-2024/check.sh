#!/bin/bash

latexFile='src/index.tex'
plainOutFile="/tmp/latex-check-plain-$( uuidgen )"
wordOutFile="/tmp/latex-check-words-$( uuidgen )"

hunChars='í|Í|ó|Ó|ö|Ö|ő|Ő|ú|Ú|ü|Ü|ű|Ű'

cat "${latexFile}" |
    grep -vE '\\(logo|includegraphics|includesvg|documentlang)\b' |
    sed -E '/\\begin\{(minted)\}/,/\\end\{minted\}/d' |
    sed -E 's/\$\-([a-zA-Z]|'"${hunChars}"'){1,7}\b/$/gi' |
    detex -l |
    sed -E 's/\b(ld|stb|pl)\.//gi' |
    sed -E 's/\-([a-zA-Z]|'"${hunChars}"'){1,7}\b//gi' |
    sed -E 's/\[//' |
    sed -E 's/[-.?!:,;]/ /gi' > "${plainOutFile}" \
;
hunspell -t -l -i 'UTF-8' -d 'hu_HU,en_US' "${plainOutFile}" |
    sort -u |
    grep -vE '[0-9]|.[A-Z]' \
    > "${wordOutFile}" \
;

cat "${wordOutFile}"

rm -f "${plainOutFile}"
rm -f "${wordOutFile}"
