pluginManagement {
    repositories {// Le decimos a Gradle que busque plugins en estos repositorios
        google()
        mavenCentral()
        gradlePluginPortal() // Repositorio oficial de plugins de Gradle
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // Le decimos a Gradle que busque dependencias (librerías) en estos repositorios
        google()
        mavenCentral()
    }
}

rootProject.name = "HuertoHogarApp"
include(":app")

