# ktx

npx for kotlin and the JVM!
Install and execute Maven Central packages or kotlin scripts just like any other command line utility.

## Installation

### (Recommended) [SDKMAN](sdkman.io)

```shell
sdk install ktx
```

### [Install Script](./scripts/install.sh)

```shell
curl https://raw.githubusercontent.com/mpetuska/ktx/master/scripts/install.sh | bash
```

## Uninstallation

### (Recommended) [SDKMAN](sdkman.io)

```shell
# Check which versions are installed
sdk list ktx
# Uninstall desired version(s)
sdk uninstall ktx <version>
```

### [Install Script](./scripts/uninstall.sh)

```shell
curl https://raw.githubusercontent.com/mpetuska/ktx/master/scripts/uninstall.sh | bash
```

## Usage

After installation a new `ktx` command will appear in your terminal. The cli is self-documented so to explore its
features simply use `-h` argument on any command. e.g. `ktx -h`.

Main ones to know, however, are `ktx run` and `ktx install` which allow you
to execute or install any maven package or kotlin script from the web or your local file system.

```shell
ktx run https://raw.githubusercontent.com/mpetuska/ktx/master/kts-samples/gw.main.kts -- clean build -Pversion="0.0.0"

ktx install https://raw.githubusercontent.com/mpetuska/ktx/master/kts-samples/gw.main.kts
# Installed script usage
gw clean build -Pversion="0.0.0"

ktx clean --scripts
ktx install https://raw.githubusercontent.com/mpetuska/ktx/master/kts-samples/gw.main.kts --alias=super-wrapper
super-wrapper clean build -Pversion="0.0.0"

ktx install org.jetbrains.kotlin:kotlin-compiler:1.8.0
kotlin-compiler -- -version
```

For sample scripts to play with you can have a look at [kts-samples](./kts-samples)

## Known Issues

- Flag options are not propagated to scripts. To work around it use `--` separator to explicitly denote the start of
  script (not ktx interpreter) arguments. e.g.: `script -- -version arg1 -flag`