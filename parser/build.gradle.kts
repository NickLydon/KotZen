plugins {
    kotlin("jvm")
    id("io.bkbn.sourdough.library.jvm")
    id("io.gitlab.arturbosch.detekt")
    id("com.adarshr.test-logger")
    id("org.jetbrains.dokka")
    id("maven-publish")
    id("java-library")
    id("signing")
}

sourdough {
    libraryName.set("KotZen Parser")
    libraryDescription.set("KotZen Parser Library")
}

testing {
    suites {
        named("test", JvmTestSuite::class) {
            useJUnitJupiter()
        }
    }
}

sourdough {
    githubOrg.set("")
    githubRepo.set("kompendium")
    licenseName.set("MIT License")
    licenseUrl.set("https://mit-license.org")
    developerId.set("unredundant")
    developerName.set("Ryan Brink")
    developerEmail.set("admin@bkbn.io")
}