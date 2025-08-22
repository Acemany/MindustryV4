![Logo](https://i.imgur.com/LLlfrzb.png)

This version is going to improve the [mindustry](https://github.com/Anuken/Mindustry) of [V4](https://github.com/Anuken/Mindustry/releases/tag/v63), while not changing the style of the game itself much.

> [!IMPORTANT]
>
> Project [migrated](https://codeberg.org/Acemany/MindustryV4) to Codeberg, there will be no more updates to this repository!

## Building

If you'd rather compile on your own, follow these instructions.
First, make sure you have Java 8 and JDK 8 installed. Open a terminal in the root directory, and run the following commands:

### Windows

_Running:_ `gradlew desktop:run`  
_Building:_ `gradlew desktop:dist`

### Linux

_Running:_ `./gradlew desktop:run`  
_Building:_ `./gradlew desktop:dist`

### For Server Builds...

Server builds are bundled with each released build (in Releases). If you'd rather compile on your own, replace 'desktop' with 'server' i.e. `gradlew server:dist`.

---

Gradle may take up to several minutes to download files. Be patient.

After building, the output .JAR file should be in `/desktop/build/libs/desktop-release.jar` for desktop builds, and in `/server/build/libs/server-release.jar` for server builds.
