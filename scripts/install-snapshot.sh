#!/usr/bin/env bash

function info() {
  echo "$(tput setaf 4)[INFO] $*$(tput sgr0)"
}

start="$(pwd)"
workdir=/tmp/ktx-snapshot
rm -rf $workdir || exit 1
mkdir -p $workdir || exit 1
cd $workdir || exit 1

info "Cloning ktx into $(pwd)"
git clone https://github.com/mpetuska/ktx.git && cd ktx || exit 1

info "Building and installing ktx-SNAPSHOT"
./scripts/install-local.sh || exit 1

info "Preparing uninstall script"
mkdir -p "$KTX_DIR"
cp scripts/uninstall.sh "$KTX_DIR/uninstall.sh" || exit 1
chmod +x "$KTX_DIR/uninstall.sh" || exit 1

info "Cleaning up after install"
cd "$start" && rm -rf $workdir || exit 1

info "If you'd like to uninstall ktx-SNAPSHOT execute $KTX_DIR/uninstall.sh"
