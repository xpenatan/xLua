// Core
include(":lua:lua-base")
include(":lua:lua-build")
include(":lua:lua-core")
include(":lua:lua-desktop")
include(":lua:lua-teavm")

// Utils
include(":extensions:reg-utils")

// Examples
include(":examples:basic:core")
include(":examples:basic:desktop")
include(":examples:basic:teavm")

// #### Use include build to use other project source directly. Just update the source path ####

//includeBuild("E:\\Dev\\Projects\\java\\gdx-teavm") {
//    dependencySubstitution {
//        substitute(module("com.github.xpenatan.gdx-teavm:backend-teavm")).using(project(":backends:backend-teavm"))
//    }
//}

//includeBuild("E:\\Dev\\Projects\\java\\jParser") {
//    dependencySubstitution {
//        substitute(module("com.github.xpenatan.jParser:jParser-base")).using(project(":jParser:jParser-base"))
//        substitute(module("com.github.xpenatan.jParser:jParser-build")).using(project(":jParser:jParser-build"))
//        substitute(module("com.github.xpenatan.jParser:jParser-build-tool")).using(project(":jParser:jParser-build-tool"))
//        substitute(module("com.github.xpenatan.jParser:jParser-core")).using(project(":jParser:jParser-core"))
//        substitute(module("com.github.xpenatan.jParser:jParser-cpp")).using(project(":jParser:jParser-cpp"))
//        substitute(module("com.github.xpenatan.jParser:jParser-idl")).using(project(":jParser:jParser-idl"))
//        substitute(module("com.github.xpenatan.jParser:jParser-teavm")).using(project(":jParser:jParser-teavm"))
//        substitute(module("com.github.xpenatan.jParser:idl-core")).using(project(":idl:idl-core"))
//        substitute(module("com.github.xpenatan.jParser:idl-teavm")).using(project(":idl:idl-teavm"))
//        substitute(module("com.github.xpenatan.jParser:loader-core")).using(project(":loader:loader-core"))
//        substitute(module("com.github.xpenatan.jParser:loader-teavm")).using(project(":loader:loader-teavm"))
//    }
//}

//includeBuild("E:\\Dev\\Projects\\java\\xImGui") {
//    dependencySubstitution {
//        substitute(module("com.github.xpenatan.xImGui:imgui-core")).using(project(":imgui:imgui-core"))
//        substitute(module("com.github.xpenatan.xImGui:imgui-desktop")).using(project(":imgui:imgui-desktop"))
//        substitute(module("com.github.xpenatan.xImGui:imgui-teavm")).using(project(":imgui:imgui-teavm"))
//        substitute(module("com.github.xpenatan.xImGui:gdx-shared-impl")).using(project(":gdx:gdx-shared-impl"))
//        substitute(module("com.github.xpenatan.xImGui:gdx-gl-impl")).using(project(":gdx:gdx-gl-impl"))
//        substitute(module("com.github.xpenatan.xImGui:gdx-wgpu-impl")).using(project(":gdx:gdx-wgpu-impl"))
//        substitute(module("com.github.xpenatan.xImGui:imgui-ext-core")).using(project(":imgui-ext:ext-core"))
//        substitute(module("com.github.xpenatan.xImGui:imgui-ext-desktop")).using(project(":imgui-ext:ext-desktop"))
//        substitute(module("com.github.xpenatan.xImGui:imgui-ext-teavm")).using(project(":imgui-ext:ext-teavm"))
//    }
//}