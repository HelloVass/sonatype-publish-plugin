package io.johnsonlee.gradle.publish

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.maven
import java.net.URI

fun RepositoryHandler.configureNexus(project: Project, repository: String = "/content/repositories/public") {
    val NEXUS_URL: String? by project.vars
    val NEXUS_USERNAME: String? by project.vars
    val NEXUS_PASSWORD: String? by project.vars

    if (NEXUS_URL != null && NEXUS_USERNAME != null && NEXUS_PASSWORD != null) {
        maven {
            url = URI(NEXUS_URL).resolve(repository)
            credentials {
                username = NEXUS_USERNAME
                password = NEXUS_PASSWORD
            }
        }
    }
}

fun RepositoryHandler.configureSonatype(project: Project) {
    maven("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
}
