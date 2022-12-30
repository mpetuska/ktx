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
  curl "https://github.com/mpetuska/ktx/releases/download/$tag/ktx.zip" -Lo "$outfile"
else
  echo "Installing from $src"
  cp "$src" "$outfile"
fi
unzip "$outfile" -d "$destination"
rm -rf "$workdir"
unzip -j "$destination/ktx/lib/cli.jar" ".ktxrc" -d "$destination/ktx"

echo "Setting up ktx environment"
bindir="$HOME/.local/bin"
mkdir -p "$bindir"
target="$bindir/ktx"
rm -rf "$target"
ln -s "$destination/ktx/bin/ktx" "$target"

function append-source() {
  local sourcestr="source \"$destination/ktx/.ktxrc\""

  if [[ -f "$1" && -z $(grep "$sourcestr" "$1") ]]; then
    echo "source \"$destination/ktx/.ktxrc\"" >>"$1"
  fi
}

append-source "$HOME/.profile"
append-source "$HOME/.bashrc"
append-source "$HOME/.zshrc"

echo "ktx was installed! Either start a new terminal session or source $destination/ktx/.ktxrc"
