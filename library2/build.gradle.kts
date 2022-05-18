
enum class Strategy {
    DirectProject, // depend on api(":library"). WORKS
    DirectMaven, // depend on maven artifact. WORKS
    Substitution // depend on maven artifact, replace with project. DOESN'T WORK
}

val strategy = Strategy.Substitution

if (strategy == Strategy.Substitution) {
    configurations.configureEach {
        resolutionStrategy {
            dependencySubstitution {
                val subs = listOf(
                    "${project.group}:library:${project.version}",
                    "${project.group}:library-iosarm64:${project.version}"
                )
                subs.forEach {
                    substitute(module(it)).with(project(":library"))
                }
            }
        }
    }
}

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                when (strategy) {
                    Strategy.DirectProject -> api(project(":library"))
                    Strategy.DirectMaven -> api("${project.group}:library:${project.version}")
                    Strategy.Substitution -> api("${project.group}:library:${project.version}")
                }
            }
        }
    }
}
