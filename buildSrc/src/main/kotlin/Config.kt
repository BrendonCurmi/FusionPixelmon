import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

/**
 * Applies the default configuration to the Kotlin build scripts.
 */
fun Project.applyConfiguration() {
    /*configurations {
        create("shade")
    }*/

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
