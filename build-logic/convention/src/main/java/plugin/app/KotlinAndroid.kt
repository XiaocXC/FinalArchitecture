package plugin.app

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import plugin.config.Versions

/**
 * Android基本配置
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *>,
) {
    commonExtension.apply {
        // compileSDK
        compileSdk = Versions.COMPILE_SDK
        // compileTools
        buildToolsVersion = Versions.BUILD_TOOLS

        defaultConfig {
            // minSDK
            minSdk = Versions.MIN_SDK
        }

        // Java11
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        // 给release和debug都配置上BuildConfig.VERSION_CODE和BuildConfig.VERSION_NAME
        buildTypes.asMap["release"]!!.apply {
            buildConfigField("long", "VERSION_CODE", "${Versions.VERSION_CODE}")
            buildConfigField("String", "VERSION_NAME", "\"${Versions.VERSION_NAME}\"")
        }
        buildTypes.asMap["debug"]!!.apply {
            buildConfigField("long", "VERSION_CODE", "${Versions.VERSION_CODE}")
            buildConfigField("String", "VERSION_NAME", "\"${Versions.VERSION_NAME}\"")
        }

        kotlinOptions {
            // Java11
            jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }
}

fun CommonExtension<*, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}