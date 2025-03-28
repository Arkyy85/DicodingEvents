// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    //noinspection GradleDependency
    id("com.google.devtools.ksp") version "2.0.21-1.0.26" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.5.0" apply false
    id("org.owasp.dependencycheck") version "8.4.0"
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.dynamic.feature) apply false
}

dependencyCheck {
    autoUpdate = true
    cveValidForHours = 24
    format = "ALL"
    failBuildOnCVSS = 7f
}