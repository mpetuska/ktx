#!/usr/bin/env bash

printf "Which ktx version to install [default: latest]? "
read -r user_version
version=${user_version:=latest}

destination="$HOME/.local/share/ktx"
echo "Installing ktx@$version to $destination"
