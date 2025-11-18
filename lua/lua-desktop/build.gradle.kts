plugins {
    id("java")
}

val moduleName = "lua-desktop"

val windowsFile = "$projectDir/../lua-build/build/c++/libs/windows/vc/lua64.dll"

tasks.jar {
    from(windowsFile)
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.java8Target)
    targetCompatibility = JavaVersion.toVersion(LibExt.java8Target)
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = moduleName
            group = LibExt.groupId
            version = LibExt.libVersion
            from(components["java"])
        }
    }
}