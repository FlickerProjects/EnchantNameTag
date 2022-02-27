plugins {
    `java-library`
    id("io.izzel.taboolib") version "1.34"
    id("org.jetbrains.kotlin.jvm") version "1.5.10"
}

taboolib {
    install("common")
    install("common-5")
    install("module-chat")
    install("module-configuration")
    install("module-database")
    install("module-lang")
    install("module-nms-util")
    install("module-nms")
    install("module-ui")
    install("platform-bukkit")
    install("expansion-player-database")
    classifier = null
    version = "6.0.7-35"
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("ink.ptms.core:v11604:11604")
    compileOnly("ink.ptms.core:v11800:11800:universal")
    compileOnly("ink.ptms.core:v11800:11800:mapped")
    compileOnly("ink.ptms:nms-all:1.0.0")

    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}