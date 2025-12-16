plugins {
    id("soffa.lombok")
    id("soffa.maven-publish")
    id("soffa.test.junit5")
    id("soffa.qa.coverage.l1")
}

dependencies {
    api(project(":foundation-api"))

    api("commons-beanutils:commons-beanutils:1.11.0")
    // api("io.github.openfeign:feign-core:11.8")
    api("joda-time:joda-time:2.12.7")
    api("com.github.michaelgantman:MgntUtils:1.7.0.3")
    api("com.joestelmach:natty:0.13")
    api("org.json:json:20240303")
    api("com.jayway.jsonpath:json-path:2.10.0")
    api("jakarta.servlet:jakarta.servlet-api:6.0.0")
    api("commons-io:commons-io:2.16.1")
    api("com.squareup.okhttp3:okhttp:4.12.0")
    api("com.konghq:unirest-java:3.14.5")
    api("com.google.guava:guava:33.1.0-jre")
    @Suppress("GradlePackageUpdate")
    implementation("commons-codec:commons-codec:1.16.1")
    implementation("io.pebbletemplates:pebble:4.0.0")
    api("com.auth0:java-jwt:4.4.0")
    api("com.nimbusds:nimbus-jose-jwt:10.6")
    api("org.apache.commons:commons-text:1.12.0")
    implementation("com.jsoniter:jsoniter:0.9.23")  {
        exclude(group = "com.fasterxml.jackson.core")
        exclude(group = "com.google.code.gson")
    }
    implementation("com.aventrix.jnanoid:jnanoid:2.0.0")
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.17.0")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.0")
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.17.0")

}
repositories {
    mavenCentral()
}

