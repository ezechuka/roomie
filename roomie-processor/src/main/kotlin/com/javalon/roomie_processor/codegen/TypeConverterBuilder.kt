package com.javalon.roomie_processor.codegen

import com.google.gson.Gson
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

class TypeConverterBuilder(
    private val packageName: String,
    private val modelName: String,
    private val fileName: String = String(),
    private val asList: Boolean = false
) {
    private val modelClassName = ClassName(packageName, modelName)
    private val typeTokenInterfaceName = ClassName("com.google.gson.reflect", "TypeToken")
    private val converterTypeListClassName = ClassName("kotlin.collections", "List")
        .parameterizedBy(modelClassName)
    private val typeConverterClassName = ClassName("androidx.room", "TypeConverter")

    fun build(): TypeSpec = if (fileName.isEmpty()) {
        TypeSpec.classBuilder("${modelName}Converter")
            .addModifiers(KModifier.OPEN)
            .addBaseMethods()
            .build()
    } else {
        TypeSpec.classBuilder(fileName)
            .addModifiers(KModifier.OPEN)
            .addBaseMethods()
            .build()
    }

    private fun TypeSpec.Builder.addBaseMethods(): TypeSpec.Builder = apply {
        addFunction(
            FunSpec.builder("fromString")
                .addAnnotation(typeConverterClassName)
                .returns(
                    if (asList)
                        converterTypeListClassName
                    else modelClassName
                )
                .addParameter("value", String::class)
                .addStatement(
                    "val type = object : %T<%T>() {}.type",
                    typeTokenInterfaceName,
                    if (asList)
                        converterTypeListClassName
                    else modelClassName,
                )
                .addStatement(
                    "return %T().fromJson<%T>(value, type)",
                    Gson::class,
                    if (asList)
                        converterTypeListClassName
                    else
                        modelClassName
                )
                .build()
        )

        addFunction(
            FunSpec.builder("fromList")
                .addAnnotation(typeConverterClassName)
                .returns(String::class)
                .addParameter("value", if (asList) converterTypeListClassName else modelClassName)
                .addStatement("val gson = %T()", Gson::class)
                .addStatement("return gson.toJson(value)")
                .build()
        )
    }
}