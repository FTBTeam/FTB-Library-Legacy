# FTBLib

### Building the mod

- Make sure you have Java JDK 1.8+ installed
- Downloading it as zip or clone with Git
- Open console / Command Prompt in downloaded folder's location
- Run "gradlew build"
- Find PermissionAPI-x.jar, and copy it's contents in the built jar (use deobf version for deobf jar)

### Adding as dependency

Currently it doesn't have a maven repository, but you can still drop it in your 'libs' folder and add this in your build.gradle file:

```
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compile 'com.github.LatvianModder:FTBLib:3.0.2'
    compile 'com.github.LatvianModder:PermissionAPI:1.0.0' //Needed until Forge PermissionAPI PR is merged
}
```

You probably also want to add "required-after:ftbl" or "after:ftbl" in your @Mod's dependencies. Most of the APIs don't need hard dependency, but things like GUIs, Notifications etc. won't work without FTBLib loaded