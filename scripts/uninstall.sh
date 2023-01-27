#!/usr/bin/env bash

function info() {
  echo "$(tput setaf 4)[INFO] $*$(tput sgr0)"
}

function remove() {
  info "Removing $1"
  rm -rf "$1" || exit 1
}
remove ~/.ktx/
remove ~/.local/share/ktx/
remove ~/.local/bin/ktx

function remove-source() {
  if [[ -f "$1" ]]; then
    info "Cleaning up $1"
    sed -i.bak "/^source \".*\\/.ktxrc\"/d" "$1" || exit 1
  fi
}
remove-source ~/.profile
remove-source ~/.bashrc
remove-source ~/.zshrc

info "ktx was uninstalled successfully!"
