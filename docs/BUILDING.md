# Building / بیلد

Requirements: JDK 17, Gradle wrapper or Gradle 8+.

```bash
git clone https://github.com/JustRohams/FallBack.git
cd FallBack
gradle build        # or ./gradlew build if you add the wrapper
```

The plugin is created at `build/libs/Fallback.jar`.

The build depends on `dev.waterdog.waterdogpe:waterdog:2.0.4-SNAPSHOT`. If your proxy fork is not published in a
repository, run `./gradlew publishToMavenLocal` inside the WaterdogPE source first and then build the plugin.

## Project layout
```
src/main/java/RohaM/fallback/
  Fallback.java         main class, registers the handler and listeners
  FallbackHandler.java  reconnect handler, server selection, offline tracking
  FallbackConfig.java   validated snapshot of config.yml
src/main/resources/
  plugin.yml
  config.yml
```
