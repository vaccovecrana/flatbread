plugins { id("io.vacco.oss.gitflow") version "0.9.8" }

group = "io.vacco.flatbread"
version = "0.1.4"

configure<io.vacco.oss.gitflow.GsPluginProfileExtension> {
  addJ8Spec()
  addClasspathHell()
  sharedLibrary(true, false)
}

dependencies {
  testImplementation("io.vacco.jsonbeans:jsonbeans:1.0.0")
}
