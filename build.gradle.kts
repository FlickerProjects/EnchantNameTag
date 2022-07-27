plugins {
    `java-library`
    id("io.izzel.taboolib") version "1.40"
    id("org.jetbrains.kotlin.jvm") version "1.5.10"
}

taboolib {
    description {
        dependencies {
            name("TAB").optional(true)
            name("TAB-Bridge").optional(true)
            name("ItemsAdder").optional(true)
        }
    }
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
    install("expansion-command-helper")
    classifier = null
    version = "6.0.9-40"
}

repositories {
    mavenCentral()
    maven("https://repo.kryptonmc.org/releases")
}

dependencies {
    compileOnly("ink.ptms.core:v11604:11604")
    compileOnly("ink.ptms.core:v11900:11900:mapped")
    compileOnly("ink.ptms.core:v11900:11900:universal")
    compileOnly("ink.ptms:nms-all:1.0.0")

    compileOnly("me.neznamy:tab-api:3.1.2") { isTransitive = false }

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