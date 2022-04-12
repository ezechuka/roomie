plugins {
    id("kotlin")
    id("maven-publish")
}

group = "com.github.ezechuka"
version = "1.0.0-beta02"

java {
    withSourcesJar()
}

val kspVersion: String by project

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")

    implementation(project(":roomie-annotation"))

    implementation("com.squareup:kotlinpoet:1.4.4")
    implementation("com.google.code.gson:gson:2.9.0")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}