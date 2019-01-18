import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.11"
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = ("https://dl.bintray.com/openrndr/openrndr/"))
    maven(url = ("https://jitpack.io"))
}

val openrndrVersion = "0.3.31"
val orxVersion = "v0.0.16"
val slf4jVersion = "1.7.25"
// windows platform specific!

dependencies {
    compile(kotlin("stdlib-jdk8"))
    runtime("org.openrndr:openrndr-gl3:$openrndrVersion")
    runtime("org.openrndr:openrndr-gl3-natives-windows:$openrndrVersion")
    compile("org.openrndr:openrndr-core:$openrndrVersion")
    compile("org.openrndr:openrndr-svg:$openrndrVersion")
    compile("org.openrndr:openrndr-animatable:$openrndrVersion")
    compile("org.openrndr:openrndr-extensions:$openrndrVersion")
    compile("org.openrndr:openrndr-event:$openrndrVersion")
    compile("com.github.openrndr.orx:orx-no-clear:$orxVersion")
    compile("org.slf4j:slf4j-nop:$slf4jVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    //??? options.compilerArgs += ['-Xuse-experimental=kotlin.Experimental']
}
