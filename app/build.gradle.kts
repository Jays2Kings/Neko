import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

plugins {
    id("com.android.application")
    id("com.google.android.gms.oss-licenses-plugin")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("org.jmailen.kotlinter") version "2.3.1"
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.firebase.crashlytics")
    id("com.google.gms.google-services") //apply false
}

fun getBuildTime() = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now(ZoneOffset.UTC))
fun getCommitCount() = runCommand("git rev-list --count HEAD")
fun getGitSha() = runCommand("git rev-parse --short HEAD")

fun runCommand(command: String): String {
    val byteOut = ByteArrayOutputStream()
    project.exec {
        commandLine = command.split(" ")
        standardOutput = byteOut
    }
    return String(byteOut.toByteArray()).trim()
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.3")

    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
        applicationId = "tachiyomi.mangadex"
        versionCode = 66
        versionName = "2.0.13"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
        setProperty("archivesBaseName", "Neko")
        buildConfigField("String", "COMMIT_COUNT", "\"${getCommitCount()}\"")
        buildConfigField("String", "COMMIT_SHA", "\"${getGitSha()}\"")
        buildConfigField("String", "BUILD_TIME", "\"${getBuildTime()}\"")
        buildConfigField("Boolean", "INCLUDE_UPDATER", "false")

        ndk {
            abiFilters("armeabi-v7a", "arm64-v8a", "x86")
        }
    }
    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
    }

    flavorDimensions("default")

    productFlavors {
        create("standard") {
            buildConfigField("Boolean", "INCLUDE_UPDATER", "true")
        }
        create("dev") {
            resConfig("en")
        }
    }

    lintOptions {
        isAbortOnError = false
        isCheckReleaseBuilds = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
androidExtensions {
    isExperimental = true
}

dependencies {
    // Modified dependencies
    implementation("com.github.inorichi:subsampling-scale-image-view:ac0dae7")
// Android support library
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.google.android.material:material:1.1.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.preference:preference:1.1.1")
    implementation("androidx.annotation:annotation:1.1.0")
    implementation("androidx.browser:browser:1.2.0")
    implementation("androidx.biometric:biometric:1.0.1")
    implementation("androidx.palette:palette:1.0.0")


    implementation("androidx.constraintlayout:constraintlayout:1.1.3")

    implementation("androidx.multidex:multidex:2.0.1")

    implementation("com.google.firebase:firebase-core:17.4.0")
    implementation("com.google.firebase:firebase-analytics:17.4.0")
    implementation("com.google.firebase:firebase-crashlytics:17.0.0")

    val lifecycleVersion = "2.2.0"
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")

    // ReactiveX
    implementation("io.reactivex:rxandroid:1.2.1")
    implementation("io.reactivex:rxjava:1.3.8")
    implementation("com.jakewharton.rxrelay:rxrelay:1.2.0")
    implementation("com.f2prateek.rx.preferences:rx-preferences:1.0.2")
    implementation("com.github.pwittchen:reactivenetwork:0.13.0")

    // Coroutines
    implementation("com.github.tfcporciuncula:flow-preferences:1.1.1")

// Network client
    val okhttpVersion = "4.5.0"
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")
    implementation("com.squareup.okio:okio:2.6.0")

    //Chucker
    val chuckerVersion = "3.2.0"
    debugImplementation("com.github.ChuckerTeam.Chucker:library:$chuckerVersion")
    releaseImplementation("com.github.ChuckerTeam.Chucker:library-no-op:$chuckerVersion")

    //hyperion
    debugImplementation("com.willowtreeapps.hyperion:hyperion-core:0.9.27")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-timber:0.9.27")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-core:0.9.27")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-attr:0.9.27")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-build-config:0.9.27")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-crash:0.9.27")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-disk:0.9.27")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-geiger-counter:0.9.27")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-measurement:0.9.27")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-phoenix:0.9.27")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-recorder:0.9.27")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-shared-preferences:0.9.27")

// REST
    val retrofitVersion = "2.7.2"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.5.0")

// JSON
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.github.salomonbrys.kotson:kotson:2.5.0")

// Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:0.20.0")

// Disk
    implementation("com.jakewharton:disklrucache:2.0.2")
    implementation("com.github.inorichi:unifile:e9ee588")

// HTML parser
    implementation("org.jsoup:jsoup:1.13.1")

//Icons
    implementation("com.mikepenz:iconics-core:5.0.3")
    implementation("com.mikepenz:iconics-views:5.0.3")
    implementation("com.mikepenz:community-material-typeface:5.0.45.1-kotlin@aar")
    implementation("com.mikepenz:material-design-icons-dx-typeface:5.0.1.0-kotlin@aar")

// Job scheduling
    val workManagerVersion = "2.3.3"
    implementation("android.arch.work:work-runtime:$workManagerVersion")
    implementation("android.arch.work:work-runtime-ktx:$workManagerVersion")
    implementation("com.google.android.gms:play-services-gcm:17.0.0")

// Changelog
    implementation("com.github.gabrielemariotti.changeloglib:changelog:2.1.0")

// Database
    implementation("androidx.sqlite:sqlite:2.1.0")
    implementation("com.github.inorichi.storio:storio-common:8be19de@aar")
    implementation("com.github.inorichi.storio:storio-sqlite:8be19de@aar")
    implementation("io.requery:sqlite-android:3.31.0")

// Model View Presenter
    val nucleusVersion = "3.0.0"
    implementation("info.android15.nucleus:nucleus:$nucleusVersion")
    implementation("info.android15.nucleus:nucleus-support-v7:$nucleusVersion")

// Dependency injection
    implementation("com.github.inorichi.injekt:injekt-core:65b0440")

    // Image library
    val coilVersion = "0.11.0"
    implementation("io.coil-kt:coil:$coilVersion")
    implementation("io.coil-kt:coil-gif:$coilVersion")
    implementation("io.coil-kt:coil-svg:$coilVersion")

// Logging
    implementation("com.jakewharton.timber:timber:4.7.1")

// UI
    implementation("com.dmitrymalkovich.android:material-design-dimens:1.4")
    implementation("com.github.dmytrodanylyk.android-process-button:library:1.0.4")
    val latestFastAdapterRelease = "5.0.0"
    implementation("com.mikepenz:fastadapter:${latestFastAdapterRelease}")
    implementation("com.mikepenz:fastadapter-extensions-binding:${latestFastAdapterRelease}")
    implementation("eu.davidea:flexible-adapter:5.1.0")
    implementation("eu.davidea:flexible-adapter-ui:1.0.0")
    implementation("com.nononsenseapps:filepicker:2.5.2")
    implementation("com.afollestad.material-dialogs:core:3.1.1")
    implementation("com.afollestad.material-dialogs:input:3.1.1")
    implementation("me.zhanghai.android.systemuihelper:library:1.0.0")
    implementation("com.nightlynexus.viewstatepageradapter:viewstatepageradapter:1.1.0")

    implementation("com.github.kizitonwose:AndroidTagGroup:1.6.0")
    implementation("com.github.chrisbanes:PhotoView:2.3.0")
    implementation("com.github.carlosesco:DirectionalViewPager:a844dbca0a")
    implementation("com.github.florent37:viewtooltip:1.2.2")
    implementation("com.getkeepsafe.taptargetview:taptargetview:1.13.0")

// Conductor
    implementation("com.bluelinelabs:conductor:2.1.5")
    implementation("com.bluelinelabs:conductor-support:2.1.5") {
        exclude("group", "com.android.support")
    }
    implementation("com.github.inorichi:conductor-support-preference:a32c357")

// RxBindings
    val rxbindingsVersion = "1.0.1"
    implementation("com.jakewharton.rxbinding:rxbinding-kotlin:$rxbindingsVersion")
    implementation("com.jakewharton.rxbinding:rxbinding-appcompat-v7-kotlin:$rxbindingsVersion")
    implementation("com.jakewharton.rxbinding:rxbinding-support-v4-kotlin:$rxbindingsVersion")
    implementation("com.jakewharton.rxbinding:rxbinding-recyclerview-v7-kotlin:$rxbindingsVersion")

// Tests
    testImplementation("junit:junit:4.13")
    testImplementation("org.assertj:assertj-core:3.12.2")
    testImplementation("org.mockito:mockito-core:1.10.19")

    val robolectricVersion = "3.1.4"
    testImplementation("org.robolectric:robolectric:$robolectricVersion")
    testImplementation("org.robolectric:shadows-multidex:$robolectricVersion")
    testImplementation("org.robolectric:shadows-play-services:$robolectricVersion")


    implementation(kotlin("stdlib", org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION))

    val coroutinesVersion = "1.3.5"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    // Text distance
    implementation("info.debatty:java-string-similarity:1.2.1")

    //  version comparison
    implementation("com.g00fy2:versioncompare:1.3.4")

    implementation("com.google.android.gms:play-services-oss-licenses:17.0.0")
}

/*tasks.preBuild {
    dependsOn(tasks.lintKotlin)
}*/
tasks.lintKotlin {
    dependsOn(tasks.formatKotlin)
}
