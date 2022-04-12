package com.javalon.roomie_processor.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueArgument
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.javalon.roomie_processor.codegen.TypeConverterBuilder
import com.squareup.kotlinpoet.FileSpec

class ConverterProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation("com.javalon.AddConverter")
            .filterIsInstance<KSClassDeclaration>()

        if (!symbols.iterator().hasNext()) return emptyList()

        symbols.forEach { it.accept(Visitor(), Unit) }
        return symbols.filterNot { it.validate() }.toList()
    }

    inner class Visitor : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            if (classDeclaration.classKind != ClassKind.CLASS) {
                logger.error("@AddConverter must be applied to classes only.")
                return
            }

            val annotation: KSAnnotation = classDeclaration.annotations.first {
                it.shortName.asString() == "AddConverter"
            }

            val nameArgument: KSValueArgument = annotation.arguments.first { arg ->
                arg.name?.asString() == "name"
            }

            val asListArgument: KSValueArgument = annotation.arguments.first { arg ->
                arg.name?.asString() == "asList"
            }

            val packageName = classDeclaration.packageName.asString()
            val modelName = classDeclaration.simpleName.asString()
            val converterName = nameArgument.value as String
            val asList = asListArgument.value as Boolean
            val fileName = if (converterName.isBlank() && !asList) {
                "${modelName}Converter"
            } else if (converterName.isBlank() && asList) {
                "${modelName}ListConverter"
            } else {
                converterName
            }

            val fileSpec = FileSpec.builder(packageName, fileName)
                .addType(TypeConverterBuilder(packageName, modelName, fileName, asList).build())
                .build()

            codeGenerator.createNewFile(
                dependencies = Dependencies(false),
                packageName = packageName,
                fileName = fileName
            ).use { outputStream ->
                outputStream.writer()
                    .use {
                        fileSpec.writeTo(it)
                    }
            }
        }
    }
}