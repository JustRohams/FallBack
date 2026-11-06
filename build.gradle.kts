plugins {
    java
}

group = "RohaM"
version = "1.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.opencollab.dev/maven-releases/")
    maven("https://repo.opencollab.dev/maven-snapshots/")
    maven("https://repo.waterdog.dev/main")
    maven("https://repo.waterdog.dev/snapshots")
}

dependencies {
    compileOnly("dev.waterdog.waterdogpe:waterdog:2.0.4-SNAPSHOT")
    compileOnly("org.apache.logging.log4j:log4j-api:2.25.4")
}

tasks.jar {
    archiveBaseName.set("Fallback")
    archiveVersion.set("")
}
