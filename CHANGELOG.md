# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [Unreleased]

### Added

- [sdkman](https://sdkman.io) distribution
- New `migrate` command for more future flexibility between version changes
- Version awareness and new `version` command for self diagnostics
- `JRE 11+` requirement
- Script execution with `jvmTarget=11` (temporary, configuration options for this will be added in the future)
- `kts-samples` with sample scripts that are semi-useful to try ktx out with

### Changed

- `ktx` start script directory now gets added to `PATH` directly
- Replace bash starter scripts with symbolic links to kts scripts and ktx shebang
- Improve ktx install scripts to better propagate arguments to target script
- Replaced all `java.io.File` usages to `okio` APIs for quicker testing

### Removed

- `ktx` start script no longer being symlinked to `~/.local/bin/ktx`

---

## [0.0.1] - 2022-12-30

Initial developer preview release

### Added

- Base CLI infrastructure
- Kotlin script support (both, local and remote)
- Ability to execute scripts
- Ability to install scripts
- ktx installer scripts for easy setup

### Changed

### Removed

---

[Unreleased]: https://github.com/mpetuska/ktx/compare/0.0.2...HEAD

[0.0.2]: https://github.com/mpetuska/ktx/compare/0.0.1...0.0.2

[0.0.1]: https://github.com/mpetuska/ktx/releases/tag/0.0.1