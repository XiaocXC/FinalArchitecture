plugins {
    `kotlin-dsl`
}

group = "com.zjl.final.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "final.android.application"
            implementationClass = "plugin.AndroidApplicationConventionPlugin"
        }
        register("finalApplication") {
            id = "final.app.application"
            implementationClass = "plugin.FinalApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "final.android.library"
            implementationClass = "plugin.AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = "final.android.feature"
            implementationClass = "plugin.AndroidFeatureConventionPlugin"
        }
        register("androidHilt") {
            id = "final.android.hilt"
            implementationClass = "plugin.AndroidHiltConventionPlugin"
        }
    }
}