/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

import com.android.build.api.dsl.ManagedVirtualDevice
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import java.io.FileInputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hiltAndroidPlugin)
    alias(libs.plugins.kotlinxKover)
    alias(libs.plugins.devtoolsKsp)
    alias(libs.plugins.gradleKtlint)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.goodluck3301.giphy"
    compileSdk = libs.versions.compileSdk.get().toInt()

    signingConfigs {
        create("release") {
            val isRunningOnCI = System.getenv("BITRISE") == "true"
            val keystorePropertiesFile = file("../../keystore.properties")

            if (isRunningOnCI) {
                println("Signing Config: using environment variables")
                keyAlias = System.getenv("BITRISEIO_ANDROID_KEYSTORE_ALIAS")
                keyPassword = System.getenv("BITRISEIO_ANDROID_KEYSTORE_PRIVATE_KEY_PASSWORD")
                storeFile = file(System.getenv("KEYSTORE_LOCATION"))
                storePassword = System.getenv("BITRISEIO_ANDROID_KEYSTORE_PASSWORD")
            } else if (keystorePropertiesFile.exists()) {
                println("Signing Config: using keystore properties")
                val properties = Properties()
                InputStreamReader(
                    FileInputStream(keystorePropertiesFile),
                    Charsets.UTF_8,
                ).use { reader ->
                    properties.load(reader)
                }

                keyAlias = properties.getProperty("alias")
                keyPassword = properties.getProperty("pass")
                storeFile = file(properties.getProperty("store"))
                storePassword = properties.getProperty("storePass")
            } else {
                println("Signing Config: skipping signing")
            }
        }
    }

    defaultConfig {
        applicationId = "com.goodluck3301.giphy"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        androidResources {
            localeFilters.add("en")
        }

        testInstrumentationRunner = "com.goodluck3301.giphy.ui.test.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Bundle output filename
        val timestamp = SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())
        setProperty("archivesBaseName", "giphy-$versionName-$timestamp")

        // Configurable values - We are able to set different values for each build
        buildConfigField("String", "GIPHY_ENDPOINT", "\"https://api.giphy.com/\"")
        buildConfigField("String", "DATABASE_NAME", "\"trending.db\"")
        buildConfigField("String", "API_MAX_ENTRIES", "\"100\"")
        buildConfigField("String", "API_MIN_ENTRIES", "\"25\"")

        val isRunningOnCI = System.getenv("BITRISE") == "false"
        val keystorePropertiesFile = file("../keystore.properties")
        println("Keystore file path: ${keystorePropertiesFile.absolutePath}") // Print actual path
        println("Keystore file exists: ${keystorePropertiesFile.exists()}")
        if (isRunningOnCI) {
            println("Importing Giphy API Key from environment variable")
            defaultConfig.buildConfigField(
                type = "String",
                name = "GIPHY_API_KEY",
                value = System.getenv("GIPHYAPIKEY"),
            )
        } else if (keystorePropertiesFile.exists()) {
            println("Importing Giphy API Key from keystore")
            val properties = Properties()
            InputStreamReader(
                FileInputStream(keystorePropertiesFile),
                Charsets.UTF_8,
            ).use { reader ->
                properties.load(reader)
            }

            defaultConfig.buildConfigField(
                type = "String",
                name = "GIPHY_API_KEY",
                value = properties.getProperty("giphyApiKey") ?: "\"\"",
            )
        } else {
            println("Giphy API key not found.")
            defaultConfig.buildConfigField(
                "String",
                "GIPHY_API_KEY",
                "\"\"",
            )
        }
    }

    buildTypes {
        fun setOutputFileName() {
            applicationVariants.all {
                val variant = this
                variant.outputs
                    .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
                    .forEach { output ->
                        val timestamp = SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())
                        val outputFileName =
                            "giphy-${variant.versionName}-$timestamp-${variant.name}.apk"
                        output.outputFileName = outputFileName
                    }
            }
        }

        getByName("debug") {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            setOutputFileName()
        }

        create("benchmark") {
            initWith(getByName("release"))
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true

            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks.add("release")
        }

        getByName("release") {
            isShrinkResources = true
            isMinifyEnabled = true
            isDebuggable = false
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro",
                ),
            )

            signingConfigs.getByName("release").keyAlias?.let {
                signingConfig = signingConfigs.getByName("release")
            }

            setOutputFileName()
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += listOf(
                "META-INF/AL2.0",
                "META-INF/LGPL2.1",
                "META-INF/licenses/ASM",
                "META-INF/LICENSE.md",
                "META-INF/LICENSE*.md",
            )
            pickFirsts += listOf(
                "win32-x86-64/attach_hotspot_windows.dll",
                "win32-x86/attach_hotspot_windows.dll",
            )
        }
    }

    sourceSets {
        named("test") {
            java.srcDirs("src/testFixtures/java")
        }
    }

    testOptions {
        animationsDisabled = true

        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }

        managedDevices {
            devices {
                create<ManagedVirtualDevice>("pixel2Api34") {
                    device = "Pixel 2"
                    apiLevel = 34
                    systemImageSource = "aosp-atd"
                }
            }
        }
    }

    /**
     * Source sets can no longer contain shared roots as this is impossible to represent in the IDE.
     * In order to share sources between test and androidTest we should be able to use test fixtures.
     */
    testFixtures {
        enable = true
        androidResources = true
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    "baselineProfile"(project(":baselineprofile"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.windowsizeclass)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.material3.adaptive.android)
    implementation(libs.androidx.profileinstaller)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.leakcanary.android)

    // Dagger-Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    kspAndroidTest(libs.hilt.android.compiler)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.coil)
    implementation(libs.coil.gif)

    // Ktor as replacement of Retrofit
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.datetime)

    // Room 2
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.extensions)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.timber)

    // testing
    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.core.ktx)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.mockk.android)
    testImplementation(libs.kotest.assertions.core)

    // For instrumented tests - with Kotlin
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.uiautomator)
    androidTestImplementation(libs.kotest.assertions.core)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.mockk.android)
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    android.set(true)
    ignoreFailures.set(true)
    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.SARIF)
    }
}

tasks.named("preBuild") {
    dependsOn(tasks.named("ktlintFormat"))
}

kover {
    reports {
        // common filters for all reports of all variants
        filters {
            // exclusions for reports
            excludes {
                // excludes class by fully-qualified JVM class name, wildcards '*' and '?' are available
                classes(
                    listOf(
                        "com.goodluck3301.giphy.GiphyApplication",
                        "com.goodluck3301.giphy.*.*MembersInjector",
                        "com.goodluck3301.giphy.*.*Factory",
                        "com.goodluck3301.giphy.*.*HiltModules*",
                        "com.goodluck3301.giphy.data.source.local.*_Impl*",
                        "com.goodluck3301.giphy.data.source.local.*Impl_Factory",
                        "com.goodluck3301.giphy.BR",
                        "com.goodluck3301.giphy.BuildConfig",
                        "com.goodluck3301.giphy.Hilt*",
                        "com.goodluck3301.giphy.*.Hilt_*",
                        "com.goodluck3301.giphy.ComposableSingletons*",
                        "*Fragment",
                        "*Fragment\$*",
                        "*Activity",
                        "*Activity\$*",
                        "*.BuildConfig",
                        "*.DebugUtil",
                    ),
                )
                // excludes all classes located in specified package and it subpackages, wildcards '*' and '?' are available
                packages(
                    listOf(
                        "com.goodluck3301.giphy.di",
                        "com.goodluck3301.giphy.ui.components",
                        "com.goodluck3301.giphy.ui.destinations",
                        "com.goodluck3301.giphy.ui.navigation",
                        "com.goodluck3301.giphy.ui.previewparameter",
                        "com.goodluck3301.giphy.ui.theme",
                        "com.goodluck3301.giphy.ui.utils",
                        "androidx",
                        "dagger.hilt.internal.aggregatedroot.codegen",
                        "hilt_aggregated_deps",
                    ),
                )
            }
        }
    }
}
