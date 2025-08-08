plugins {
    id("java")
}


group = "org.example"
version = "1.0-SNAPSHOT"

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.apache.commons:commons-csv:1.14.1")
    implementation("com.github.javafaker:javafaker:1.0.2")
}





tasks.test {
    useJUnitPlatform()
}