plugins {
    id("java-library")
    id("maven-publish")
}

group = "com.moocrest"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-core:2.16.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.16.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.1")
    implementation("de.exlll:configlib-core:4.5.0")
    implementation("de.exlll:configlib-yaml:4.5.0")
    
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.mockito:mockito-core:5.8.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(17)
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            
            pom {
                name.set("Crest Webhooks")
                description.set("A powerful and lightweight webhook library")
                url.set("https://github.com/xLevitate/crest-webhooks")
                
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                
                developers {
                    developer {
                        id.set("xLevitate")
                        name.set("xLevitate")
                    }
                }
                
                scm {
                    connection.set("scm:git:git://github.com/xLevitate/crest-webhooks.git")
                    developerConnection.set("scm:git:ssh://github.com:xLevitate/crest-webhooks.git")
                    url.set("https://github.com/xLevitate/crest-webhooks/tree/main")
                }
            }
        }
    }
}