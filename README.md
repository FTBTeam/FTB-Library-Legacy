# FTBLib

### Building the mod

- Make sure you have Java JDK 1.8+ installed
- Downloading it as zip or clone with Git
- Open console / Command Prompt in downloaded folder's location
- Run "gradlew build"

### Adding as dependency

Currently it doesn't have a maven repository, but you can still drop it in your 'libs' folder and add this in your build.gradle file:

```
dependencies {
    compile fileTree(dir: 'libs', include: '*.jar')
}
```

You probably also want to add "required-after:ftbl" or "after:ftbl" in your @Mod's dependencies