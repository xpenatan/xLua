plugins {
    id("java")
    id("org.gretty") version("4.1.10")
}

project.extra["webAppDir"] = File(projectDir, "build/dist/webapp")
gretty {
    contextPath = "/"
}

dependencies {
    implementation(project(":examples:basic:core"))
    implementation(project(":lua:lua-teavm"))

    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")
    implementation("com.github.xpenatan.gdx-teavm:backend-teavm:${LibExt.gdxTeaVMVersion}")
//    implementation("com.github.xpenatan.xImGui:imgui-ext-teavm:${LibExt.gdxImGuiVersion}")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.java11Target)
    targetCompatibility = JavaVersion.toVersion(LibExt.java11Target)
}

val mainClassName = "lua.example.basic.Build"

tasks.register<JavaExec>("lua_basic_build_teavm") {
    group = "example-teavm"
    description = "Build basic example"
    mainClass.set(mainClassName)
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register("lua_basic_run_teavm") {
    group = "example-teavm"
    description = "Run teavm app"
    val list = listOf("lua_basic_build_teavm", "jettyRun")
    dependsOn(list)

    tasks.findByName("jettyRun")?.mustRunAfter("lua_basic_build_teavm")
}