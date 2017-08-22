
apply { plugin("kotlin") }

jvmTarget = "1.6"

dependencies {
    compile(project(":compiler:util"))
    compile(project(":compiler:frontend"))
    compile(project(":kotlin-reflect"))
    compile(preloadedDeps("kotlinx-coroutines-core"))
}

sourceSets {
    "main" { projectDefault() }
    "test" {}
}

