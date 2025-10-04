plugins {
    java
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.apache.commons:commons-csv:1.14.1")
    implementation("com.github.javafaker:javafaker:1.0.2")
    implementation("org.xerial:sqlite-jdbc:3.42.0.0")
    implementation("org.springframework:spring-context:6.0.11")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
}

application {
    mainClass.set("org.example.Main")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}