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

rootProject.name = "WIRE"

include(":app")
include(":core")

include(":feature:auth")
include(":feature:chat")
include(":feature:wallet")
include(":feature:payments")
include(":feature:profile")
include(":feature:contacts")
include(":feature:notifications")
include(":feature:settings")