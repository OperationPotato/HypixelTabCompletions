import com.operationpotato.catalogs.CatalogExtensions.library
import com.operationpotato.catalogs.CatalogExtensions.version
import com.operationpotato.catalogs.CatalogExtensions.versionedLibs
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.kotlin)
}

version = providers.gradleProperty("mod_version").get() + "+" + sc.current.version
group = providers.gradleProperty("maven_group").get()
base.archivesName = rootProject.name

repositories {
    maven("https://maven.operationpotato.com/mirror")

    maven("https://maven.azureaaron.net/releases") {
        content {
            includeGroup("net.azureaaron")
        }
    }

    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1") {
        content {
            includeGroup("me.djtheredstoner")
        }
    }
}

dependencies {
    minecraft(versionedLibs.library("minecraft"))
    implementation(libs.fabric.loader)

    implementation(versionedLibs.library("fabric.api"))
    implementation(libs.fabric.language.kotlin)

    includeImplementation(versionedLibs.library("hm_api"))
    runtimeOnly(libs.dev.auth)
}

fun DependencyHandlerScope.includeImplementation(dependencyNotation: Provider<*>) {
    this.include(dependencyNotation)
    this.implementation(dependencyNotation)
}

loom {
    runConfigs["client"].apply {
        runDirectory = project.file("../../run")
        val modsDir = rootProject.projectDir.resolve("run/mods/${sc.current.project}/")
        modsDir.mkdirs()
        jvmArguments.add("-Dfabric.modsFolder=$modsDir")
    }
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("loader_version", libs.versions.fabric.loader)
    inputs.property("mc_range", versionedLibs.version("mcRange"))
    inputs.property("fabric_kotlin_version", libs.versions.fabric.language.kotlin)

    filesMatching("fabric.mod.json") {
        val props = mapOf(
            "version" to inputs.properties["version"],
            "loader_version" to inputs.properties["loader_version"],
            "minecraft_version" to inputs.properties["mc_range"],
            "fabric_kotlin_version" to inputs.properties["fabric_kotlin_version"],
            "java_version" to 25
        )
        expand(props)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_25
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

tasks.named("assemble") {
    doLast {
        val fileName = "${base.archivesName.get()}-$version.jar"
        val sourceFile = project.projectDir.resolve("build/libs/${fileName}")
        val targetFile = rootProject.projectDir.resolve("build/libs/${fileName}")
        targetFile.parentFile.mkdirs()
        targetFile.writeBytes(sourceFile.readBytes())
    }
}

tasks.named("clean") {
    doLast {
        val libsFolder = rootProject.projectDir.resolve("build/libs")
        if (!libsFolder.exists()) return@doLast
        delete(libsFolder)
    }
}
