package plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies

/**
 * Feature模块 插件
 * 负责将Feature模块中加入标准组件内容
 * */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("final.android.library")
            }

            dependencies {
                // 如果是feature特性模块，我们引用network模块
                add("implementation", project(":library_network"))
            }
        }
    }
}