// IGNORE_BACKEND: JVM_IR
// TARGET_BACKEND: JVM
// IGNORE_LIGHT_ANALYSIS
// WITH_RUNTIME
// KOTLIN_CONFIGURATION_FLAGS: +JVM.INHERIT_MULTIFILE_PARTS
// FILE: box.kt

import a.*

fun box(): String = ok {}

// FILE: part1.kt
@file:[JvmName("MultifileClass") JvmMultifileClass]
package a

val O = run { "O" }
const val K = "K"

inline fun ok(block: () -> Unit): String {
    block()
    return O + K
}
