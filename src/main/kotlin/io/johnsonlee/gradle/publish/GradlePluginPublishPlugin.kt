package io.johnsonlee.gradle.publish

import org.gradle.api.Project
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType

class GradlePluginPublishPlugin : AbstractLibraryPublishPlugin() {

    override fun Project.createMavenPublications(
            publications: PublicationContainer,
            config: MavenPublication.() -> Unit
    ) {
        publications.run {
            val sourceSets = the<SourceSetContainer>()
            val javadocJar = tasks.register("packageJavadocFor${name.capitalize()}", Jar::class.java) {
                dependsOn("dokkaHtml")
                archiveClassifier.set("javadoc")
                from(tasks["dokkaHtml"])
            }
            val sourcesJar = tasks.register("packageSourcesFor${name.capitalize()}", Jar::class.java) {
                archiveClassifier.set("sources")
                from(sourceSets["main"].allSource)
            }

            withType<MavenPublication>().configureEach {
                groupId = groupString
                version = versionString
                artifact(javadocJar)
                artifact(sourcesJar)
                config.invoke(this)
            }
        }

    }

}