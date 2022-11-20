#!/bin/bash

# Shell script to actually test getting the latest version of a sam xml.
# We'll use this raw in docker though, since using this as a script is a pita

cd ../latest/ || return 2
rm ./*.xml
version=$(curl https://www.vas.ehealth.fgov.be/websamcivics/samcivics/download/samv2-full-getLastVersion?xsd=5)
echo "downloading version $version"

wget -O latest.zip "https://www.vas.ehealth.fgov.be/websamcivics/samcivics/download/samv2-download?type=full&version=$version&xsd=5"
echo "downloaded samv2 archive - unzipping"

unzip latest.zip
echo "cleanup downloaded zip"
rm latest.zip