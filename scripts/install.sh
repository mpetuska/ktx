#!/usr/bin/env bash

function info() {
  echo "$(tput setaf 4)[INFO] $*$(tput sgr0)"
}

version=latest
destination="$HOME/.local/share"
local_path="$1"
src="${local_path:=https://github.com/mpetuska/ktx/releases/latest}"

info "Installing ktx@$version to $destination/ktx"
mkdir -p "$destination"
rm -rf "$destination/ktx"

workdir="/tmp/ktx"
mkdir -p "$workdir"
outfile="$workdir/ktx.zip"
if [[ -z "$1" ]]; then
  info "Downloading $version release"
  tag="$(curl -Ls -o /dev/null -w %\{url_effective\} "$src")"
  tag="${tag##*/}"
  curl "https://github.com/mpetuska/ktx/releases/download/$tag/ktx-$tag.zip" -Lo "$outfile"
else
  info "Installing from $src"
  cp "$src" "$outfile"
fi
unzip "$outfile" -d "$destination"
mv "$destination"/ktx* "$destination/ktx"
rm -rf "$workdir"

info "Setting up ktx environment"
"$destination/ktx/bin/ktx" migrate
"$destination/ktx/bin/ktx" version

info "ktx was installed! Either start a new terminal session or source $destination/ktx/.ktxrc"
