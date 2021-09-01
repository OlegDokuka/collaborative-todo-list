pluginManagement {
	repositories {
		mavenCentral()
		maven { url = uri("https://repo.spring.io/milestone") }
		maven { url = uri("https://repo.spring.io/snapshot") }
		gradlePluginPortal()
		google()
	}
}

include(":androidApp")
include(":backendApp")
include(":browserApp")
include(":desktopApp")
include(":shared")

rootProject.name = "collaborative-todo-list"
