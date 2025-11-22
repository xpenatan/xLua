plugins {
    id("java-library")
}

val moduleName = "lua-ext"


dependencies {
    implementation(project(":lua:lua-core"))
    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")
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