package io.johnsonlee.gradle.publish

import org.gradle.api.Project
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KProperty

class ProjectVariableDelegate private constructor(
        private val project: Project
) {

    companion object {
        fun of(project: Project) = ProjectVariableDelegate(project)
    }

    private val cache: MutableMap<String, String?> by lazy {
        ConcurrentHashMap<String, String?>().withDefault { null }
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String? = cache.getOrPutIfNotNull(property.name) {
        project.findProperty(property.name)?.toString()?.takeIf(String::isNotBlank)
                ?: System.getenv(property.name)?.takeIf(String::isNotBlank)
    }

}

private inline fun <K, V> MutableMap<K, V>.getOrPutIfNotNull(key: K, defaultValue: () -> V): V {
    val value = get(key)
    return if (value == null) {
        val answer = defaultValue()
        if (answer != null) {
            put(key, answer)
        }
        answer
    } else {
        value
    }
}
