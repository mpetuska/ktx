#!/usr/bin/env bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
version=local
destination="$HOME/.local/share/ktx"

cd "$SCRIPT_DIR/../" || exit 1
./gradlew :cli:installDist

echo "Installing ktx@$version to $destination"
mkdir -p "$HOME/.local/share"
rm -rf "$destination"

ln -s "$SCRIPT_DIR/../cli/build/install/ktx" "$destination"
rm -rf "$HOME/.local/bin/ktx"
ln -s "$destination/bin/ktx" "$HOME/.local/bin/ktx"

