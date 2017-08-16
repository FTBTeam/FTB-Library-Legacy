# FTBLib [![](http://cf.way2muchnoise.eu/ftblib.svg)](https://minecraft.curseforge.com/projects/ftblib) [![](http://cf.way2muchnoise.eu/versions/ftblib.svg)](https://minecraft.curseforge.com/projects/ftblib)

### Building the mod

- Make sure you have Java JDK 1.8+ installed
- Downloading it as zip or clone with Git
- Open console / Command Prompt in downloaded folder's location
- Run "gradlew build"

### Adding as dependency

```groovy
repositories {
    ivy { name "com.latmod.mods"; artifactPattern "http://mods.latmod.com/[module]/[revision]/[module]-[revision](-[classifier]).[ext]" }
}

dependencies {
    compile 'com.latmod.mods:FTBLib:4.1.0:api'
}
```

You probably also want to add "required-after:ftbl" or "after:ftbl" in your @Mod's dependencies.
Most of the APIs don't need hard dependency, but things like GUIs won't work without FTBLib loaded