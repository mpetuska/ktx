# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [SNAPSHOT]

### Added

- Short options for all flags
- SNAPSHOT installation script for the anxious folk that installs latest and greatest ktx from
  source
- New `link` command that allows linking a given script instead of copying it into ktx cache. Useful when you need to
  modify the script often as all the changes to the original are immediately reflected on the linked path entry.

### Changed

- Internal `execute` command now properly propagates all arguments. This allows installed script
  usage without the need of `--` bash argument separator.

### Removed

---

## [0.1.2]

### Added

### Changed

### Removed

- Remove automatic migrations. `ktx migrate` should now be invoked explicitly after each upgrade.

---

## [0.1.1]

Improve zsh compatibility

### Added

### Changed

- `.ktxrc` script now properly handles self-path on zsh shell too

### Removed

---

## [0.1.0]

Maven packages support has landed!

### Added

- [sdkman](https://sdkman.io) distribution
- New `migrate` command for more future flexibility between version changes
- Version awareness and new `version` command for self diagnostics
- `JRE 11+` requirement
- Script execution with `jvmTarget=11` (temporary, configuration options for this will be added in
  the future)
- `kts-samples` with sample scripts that are semi-useful to try ktx out with
- Maven package execution and installation support

### Changed

- `ktx` start script directory now gets added to `PATH` directly
- Replace bash starter scripts with symbolic links to kts scripts and ktx shebang
- Improve ktx install scripts to better propagate arguments to target script
- Replaced most `java.io.File` usages to `okio` APIs for quicker testing

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

[SNAPSHOT]: https://github.com/mpetuska/ktx/compare/0.1.2...HEAD

[0.1.2]: https://github.com/mpetuska/ktx/compare/0.1.1...0.1.2

[0.1.1]: https://github.com/mpetuska/ktx/compare/0.1.0...0.1.1

[0.1.0]: https://github.com/mpetuska/ktx/compare/0.0.1...0.1.0

[0.0.1]: https://github.com/mpetuska/ktx/releases/tag/0.0.1