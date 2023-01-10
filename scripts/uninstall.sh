#!/usr/bin/env bash

function remove() {
  echo "Removing $1"
  rm -rf "$1" || exit 1
}
remove ~/.ktx/
remove ~/.local/share/ktx/
remove ~/.local/bin/ktx

function remove-source() {
  if [[ -f "$1" ]]; then
    echo "Cleaning up $1"
    sed -i.bak "/^source \".*\\/.ktxrc\"/d" "$1" || exit 1
  fi
}
remove-source ~/.profile
remove-source ~/.bashrc
remove-source ~/.zshrc

echo "ktx was uninstalled successfully!"
