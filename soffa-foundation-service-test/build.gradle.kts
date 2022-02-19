plugins {
    id("soffa.java8")
    id("soffa.springboot.library")
    id("soffa.maven-publish")
}

dependencies {
    compileOnly(project(":soffa-foundation-application"))
    // compileOnly(project(":soffa-foundation-service"))
    // api("org.springframework:spring-test")
    api(project(":soffa-foundation-commons"))
    api("com.intuit.karate:karate-junit5:1.2.0.RC4")

    api("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "com.vaadin.external.google")
    }

    compileOnly("org.springframework.boot:spring-boot-starter-web")

    @Suppress("GradlePackageUpdate")
    api("com.h2database:h2:2.1.210")
    api("com.google.guava:guava:31.0.1-jre")
    @Suppress("GradlePackageUpdate")
    api("commons-io:commons-io:2.8.0")
    api("org.awaitility:awaitility:4.1.1")
    api("com.github.javafaker:javafaker:1.0.2")

    // implementation("org.mock-server:mockserver-netty:5.11.2")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
}
