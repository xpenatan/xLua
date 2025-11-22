plugins {
    id("java")
}

dependencies {
    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")

    if(LibExt.useRepoLibs) {
        implementation("com.github.xpenatan.xLua:lua-core:-SNAPSHOT")
    }
    else {
        implementation(project(":lua:lua-core"))
    }
    implementation(project(":extensions:lua-ext"))

//    implementation("com.github.xpenatan.xImGui:imgui-ext-core:${LibExt.gdxImGuiVersion}")
//    implementation("com.github.xpenatan.xImGui:gdx-gl-impl:${LibExt.gdxImGuiVersion}")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.java8Target)
    targetCompatibility = JavaVersion.toVersion(LibExt.java8Target)
}