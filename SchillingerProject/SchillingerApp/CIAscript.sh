#! /bin/sh
cd ~/NetBeansProject/SchillingerApp/
lilypond TestResults.ly
rm TestResults.ps
evince TestResults.pdf

