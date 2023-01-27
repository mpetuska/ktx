#!/usr/bin/env bash

SCRIPT_DIR=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" &>/dev/null && pwd)

cd "$SCRIPT_DIR/../" || exit 1
rm -f "$PWD/cli/build/distributions/"ktx*.zip
./gradlew :cli:distZip -Pversion="SNAPSHOT" || exit 1
./scripts/install.sh "$PWD/cli/build/distributions/"ktx*.zip
source "$HOME/.local/share/ktx/.ktxrc"
