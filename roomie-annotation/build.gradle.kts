plugins {
    id("kotlin")
    id("maven-publish")
}

group = "com.github.ezechuka"
version = "1.0.0-beta02"

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}