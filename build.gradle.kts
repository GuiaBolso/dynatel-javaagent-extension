plugins {
    id("java")
    `maven-publish`
    signing
}

group = "br.com.guiabolso"
version = System.getenv("RELEASE_VERSION") ?: "local"

apply(plugin = "maven-publish")
apply(plugin = "signing")

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure:1.28.0")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.4.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

val javadoc = tasks.named("javadoc")
val javadocsJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles java doc to jar"
    archiveClassifier.set("javadoc")
    from(javadoc)
}

publishing {

    repositories {
        maven {
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("OSSRH_USERNAME")
                password = System.getenv("OSSRH_PASSWORD")
            }
        }
    }

    publications.register("mavenJava", MavenPublication::class) {
        from(components["java"])
        artifact(javadocsJar)
        artifact(sourcesJar.get())
        artifactId = project.name

        pom {
            name.set("Dynatel")
            description.set("Dynatel - OpenTelemetry Javaagent Extension")
            url.set("https://github.com/GuiaBolso/dynatel-javaagent-extension")

            scm {
                url.set("https://github.com/GuiaBolso/dynatel-javaagent-extension")
                connection.set("scm:git:https://github.com/GuiaBolso/dynatel-javaagent-extension")
            }

            licenses {
                license {
                    name.set("Apache-2.0")
                    url.set("https://opensource.org/licenses/Apache-2.0")
                }
            }

            developers {
                developer {
                    id.set("Picpay")
                    name.set("Picpay")
                }
            }
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project

    useGpgCmd()
    if (signingKey != null && signingPassword != null) {
        useInMemoryPgpKeys(signingKey, signingPassword)
    }

    sign((extensions.getByName("publishing") as PublishingExtension).publications)
}
