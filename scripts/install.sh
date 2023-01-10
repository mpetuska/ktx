#!/usr/bin/env bash

version=latest
destination="$HOME/.local/share"
local_path="$1"
src="${local_path:=https://github.com/mpetuska/ktx/releases/latest}"

echo "Installing ktx@$version to $destination/ktx"
mkdir -p "$destination"
rm -rf "$destination/ktx"

workdir="/tmp/ktx"
mkdir -p "$workdir"
outfile="$workdir/ktx.zip"
if [[ -z "$1" ]]; then
  echo "Downloading $version release"
  tag="$(curl -Ls -o /dev/null -w %\{url_effective\} "$src")"
  tag="${tag##*/}"
  curl "https://github.com/mpetuska/ktx/releases/download/$tag/ktx-$tag.zip" -Lo "$outfile"
else
  echo "Installing from $src"
  cp "$src" "$outfile"
fi
unzip "$outfile" -d "$destination"
mv "$destination"/ktx* "$destination/ktx"
rm -rf "$workdir"

echo "Setting up ktx environment"
"$destination/ktx/bin/ktx" migrate
"$destination/ktx/bin/ktx" version

echo "ktx was installed! Either start a new terminal session or source $destination/ktx/.ktxrc"
