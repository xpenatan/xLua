plugins {
    id("java")
}

dependencies {
    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")

    implementation(project(":lua:lua-core"))
    implementation(project(":extensions:lua-extension"))

//    implementation("com.github.xpenatan.xImGui:imgui-ext-core:${LibExt.gdxImGuiVersion}")
//    implementation("com.github.xpenatan.xImGui:gdx-gl-impl:${LibExt.gdxImGuiVersion}")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.java8Target)
    targetCompatibility = JavaVersion.toVersion(LibExt.java8Target)
}