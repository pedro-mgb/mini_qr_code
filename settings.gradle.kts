pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "QR CodeCompose CameraX"
include(":app")
include(":qrcodecomposelib")
include(":qrcodecomposelibmlkit")
include(":baselineprofile")
