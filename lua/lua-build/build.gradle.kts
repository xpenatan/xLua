import de.undercouch.gradle.tasks.download.Download
import org.gradle.kotlin.dsl.support.unzipTo

plugins {
    id("java")
    id("de.undercouch.download") version("5.5.0")
}

val mainClassName = "Build"

dependencies {
    implementation(project(":lua:lua-base"))
    implementation("com.github.xpenatan.jParser:jParser-core:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:jParser-build:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:jParser-build-tool:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:jParser-teavm:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:jParser-cpp:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:jParser-idl:${LibExt.jParserVersion}")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.java11Target)
    targetCompatibility = JavaVersion.toVersion(LibExt.java11Target)
}

val buildDir = layout.buildDirectory.get().asFile
val zippedPath = "$buildDir/lua-source.zip"
val sourcePath = "$buildDir/lua-source"
val sourceDestination = "$buildDir/lua/"

tasks.register<Download>("lua_download_source") {
    group = "lua"
    description = "Download lua source"
    src("https://github.com/lua/lua/archive/refs/tags/v5.4.6.zip")
    dest(File(zippedPath))
    doLast {
        unzipTo(File(sourcePath), dest)
        copy{
            from("$sourcePath/lua-5.4.6")
            into(sourceDestination)
        }
        delete(sourcePath)
        delete(zippedPath)
    }
}

tasks.register<JavaExec>("lua_build_project") {
    group = "lua"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf() // Just generate classes
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("lua_build_project_teavm") {
    group = "lua"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf("teavm")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("lua_build_project_windows64") {
    group = "lua"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf("windows64")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("lua_build_project_linux64") {
    group = "lua"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf("linux64")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("lua_build_project_mac64") {
    group = "lua"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf("mac64")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("lua_build_project_macArm") {
    group = "lua"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf("macArm")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("lua_build_project_android") {
    group = "lua"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf("android")
    classpath = sourceSets["main"].runtimeClasspath
}