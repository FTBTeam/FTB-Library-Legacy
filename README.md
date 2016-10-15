# FTBLib

### Building the mod

- Make sure you have Java JDK 1.8+ installed
- Downloading it as zip or clone with Git
- Open console / Command Prompt in downloaded folder's location
- Run "gradlew build"

### Adding as dependency

```
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compile 'com.github.LatvianModder:FTBLib:3.1.1' //Or use 1.9-SNAPSHOT as version, if you want latest
}
```

You probably also want to add "required-after:ftbl" or "after:ftbl" in your @Mod's dependencies.
Most of the APIs don't need hard dependency, but things like GUIs, Notifications etc. won't work without FTBLib loaded