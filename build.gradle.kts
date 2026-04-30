import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val archivesBaseName: String by project
val mavenGroup: String by project
val modVersion: String by project

val javaVersion = JavaVersion.VERSION_25

plugins {
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

base {
    archivesName.set(archivesBaseName)
}

group = mavenGroup
version = modVersion

repositories {
    maven("https://api.modrinth.com/maven") {
        content {
            includeGroup("maven.modrinth")
        }
    }
    maven("https://maven.terraformersmc.com/") {
        name = "TerraformersMC"
    }
    maven("https://maven.blamejared.com/") {
        name = "Jared's maven"
    }
}

loom {
    accessWidenerPath.set(File("src/main/resources/enchantment-conflict.accesswidener"))
}

dependencies {
    minecraft(libs.minecraft)

    implementation(libs.fabric.api)
    implementation(libs.fabric.loader)
    implementation(libs.fabric.language.kotlin)

    compileOnlyApi(libs.jei.fabric.api)
    runtimeOnly(libs.jei.fabric)

    embed(libs.modmenu.badges)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion.toString()))
    }
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    withSourcesJar()
}

tasks {
    jar {
        from("LICENSE")
    }

    processResources {
        filesMatching("fabric.mod.json") {
            expand(
                mutableMapOf(
                    "modVersion" to modVersion,
                    "loaderVersion" to libs.fabric.loader.get().version,
                    "minecraftVersion" to libs.minecraft.get().version,
                )
            )
        }
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
        options.release.set(javaVersion.toString().toInt())
    }

    withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(javaVersion.toString()))
        }
    }
}

fun DependencyHandlerScope.embed(projectDependency: Provider<*>) {
    implementation(projectDependency)
    include(projectDependency)
}