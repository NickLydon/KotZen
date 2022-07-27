rootProject.name = "kotzen"

include("parser")

// Set Project Gradle Names
run {
    rootProject.children.forEach { it.name = "${rootProject.name}-${it.name}" }
}