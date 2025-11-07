group = "fr.iban"
version = "1.0"
description = "CustomItems"
java.sourceCompatibility = JavaVersion.VERSION_21

plugins {
    `java-library`
    `maven-publish`
    id("io.github.goooler.shadow") version "8.1.7"
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }

    maven {
        url = uri("https://jitpack.io")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("io.github.revxrsal:lamp.common:4.0.0-rc.13")
    implementation("io.github.revxrsal:lamp.bukkit:4.0.0-rc.13")
    implementation("com.tcoded:FoliaLib:0.5.1")

    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")

    compileOnly(libs.com.github.ibanetchep.mscore.core.survival)
    compileOnly("com.github.Zrips:Jobs:v5.2.6.3") {
        isTransitive = false
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

tasks.shadowJar {
    archiveClassifier.set("")

    relocate("com.tcoded.folialib", "fr.iban.customitems.libs.folialib")
    relocate("revxrsal.commands", "fr.iban.customitems.libs.revxrsal")
}
