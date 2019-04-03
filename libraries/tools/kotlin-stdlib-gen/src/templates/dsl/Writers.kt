/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package templates

import org.xml.sax.InputSource
import java.io.File
import java.io.FileWriter
import java.io.Reader
import javax.xml.xpath.XPathFactory

val COMMON_AUTOGENERATED_WARNING: String = """//
// NOTE: THIS FILE IS AUTO-GENERATED by the GenerateStandardLib.kt
// See: https://github.com/JetBrains/kotlin/tree/master/libraries/stdlib
//"""

lateinit var COPYRIGHT_NOTICE: String

fun readCopyrightNoticeFromProfile(copyrightProfile: File): String = readCopyrightNoticeFromProfile { copyrightProfile.reader() }

fun readCopyrightNoticeFromProfile(getCopyrightReader: () -> Reader): String {
    val template = getCopyrightReader().use { reader ->
        XPathFactory.newInstance().newXPath().evaluate("/component/copyright/option[@name='notice']/@value", InputSource(reader))
    }
    val yearTemplate = "&#36;today.year"
    val year = java.time.LocalDate.now().year.toString()
    assert(yearTemplate in template)

    return template.replace(yearTemplate, year).lines().joinToString("", prefix = "/*\n", postfix = " */\n") { " * $it\n" }
}


data class TargetedSourceFile(
    val target: KotlinTarget,
    val sourceFile: SourceFile
)

@JvmName("groupByFileAndWriteGroups")
fun Sequence<TemplateGroup>.groupByFileAndWrite(
        targetsToGenerate: Collection<KotlinTarget>,
        fileNameBuilder: (TargetedSourceFile) -> File
) {
    flatMap { group ->
        group.invoke()
                .flatMap { it.instantiate(targetsToGenerate) }
                .sortedBy { it.sortingSignature }
    }.groupByFileAndWrite(fileNameBuilder)
}

@JvmName("groupByFileAndWriteTemplates")
fun Sequence<MemberTemplate>.groupByFileAndWrite(
        targetsToGenerate: Collection<KotlinTarget>,
        fileNameBuilder: (TargetedSourceFile) -> File
) {
    flatMap { it.instantiate(targetsToGenerate) }
        .groupByFileAndWrite(fileNameBuilder)
}

@JvmName("groupByFileAndWriteMembers")
fun Sequence<MemberBuilder>.groupByFileAndWrite(
        fileNameBuilder: (TargetedSourceFile) -> File
) {
    val groupedMembers = groupBy { TargetedSourceFile(it.target, it.sourceFile) }

    for ((psf, members) in groupedMembers) {
        val file = fileNameBuilder(psf)
        members.writeTo(file, psf)
    }
}

fun List<MemberBuilder>.writeTo(file: File, targetedSource: TargetedSourceFile) {
    val (target, sourceFile) = targetedSource
    println("Generating file: $file")
    file.parentFile.mkdirs()
    FileWriter(file).use { writer ->
        writer.appendln(COPYRIGHT_NOTICE)

        when (target.platform) {
            Platform.Common, Platform.JVM -> {
                if (sourceFile.multifile) {
                    writer.appendln("@file:kotlin.jvm.JvmMultifileClass")
                }

                writer.appendln("@file:kotlin.jvm.JvmName(\"${sourceFile.jvmClassName}\")")
                sourceFile.jvmPackageName?.let {
                    writer.appendln("@file:kotlin.jvm.JvmPackageName(\"$it\")")
                }
                writer.appendln()
            }
        }

        writer.append("package ${sourceFile.packageName ?: "kotlin"}\n\n")
        writer.append("${COMMON_AUTOGENERATED_WARNING}\n\n")
        if (target.platform == Platform.JS) {
            writer.appendln("import kotlin.js.*")
            if (sourceFile == SourceFile.Arrays) {
                writer.appendln("import primitiveArrayConcat")
                writer.appendln("import withType")
            }
        }
        if (target.platform == Platform.Common) {
            writer.appendln("import kotlin.random.*")
        }
        if (sourceFile.packageName == "kotlin.collections") {
            writer.appendln("import kotlin.ranges.contains")
            writer.appendln("import kotlin.ranges.reversed")
        }

        writer.appendln()

        for (f in this) {
            f.build(writer)
        }
    }
}
