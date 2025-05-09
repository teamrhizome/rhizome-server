plugins {
    java
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "7.0.3"
}

group = "org.rhizome"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

spotless {
    java {
        palantirJavaFormat()

        importOrder("java", "javax", "org", "com")
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }
}

tasks.compileJava {
    dependsOn("spotlessApply")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<Copy>("installGitHooks") {
    description = "공유된 git hooks를 .git/hooks/로 복사"
    group = "git"

    from("$rootDir/.githooks/")
    into("$rootDir/.git/hooks/")

    filePermissions {
        user {
            execute = true
            read = true
            write = true
        }
        group.execute = true
        other.execute = true
    }
}
