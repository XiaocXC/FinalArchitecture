package plugin

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import plugin.config.Versions

/**
 * Final的Android应用 插件
 * 负责加入 Android 标准应用的plugin，targetSDK等内容
 * 并加入 Final APP 的定制化内容
 * */
class FinalApplicationConventionPlugin : AndroidApplicationConventionPlugin() {
    override fun apply(target: Project) {
        super.apply(target)
        with(target) {
            extensions.configure<ApplicationExtension> {
                defaultConfig.applicationId = Versions.ApplicationId
                defaultConfig.versionCode = Versions.VERSION_CODE
                defaultConfig.versionName = Versions.VERSION_NAME
            }
        }
    }

}