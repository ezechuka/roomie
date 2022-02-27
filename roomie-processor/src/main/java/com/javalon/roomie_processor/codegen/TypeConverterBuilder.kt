package com.javalon.roomie_processor.codegen

import com.google.gson.Gson
import com.javalon.roomie_processor.model.ConverterData
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

class TypeConverterBuilder(
    private val data: ConverterData,
    private val fileName: String = String()
) {
    private val modelClassName = ClassName(data.packageName, data.modelName)
    private val typeTokenInterfaceName = ClassName("com.google.gson.reflect", "TypeToken")
    private val itemsListClassName = ClassName("kotlin.collections", "List")
        .parameterizedBy(modelClassName)
    private val typeConverterClassName = ClassName("androidx.room", "TypeConverter")

    fun build(): TypeSpec = if (fileName.isEmpty()) {
        TypeSpec.classBuilder("${data.modelName}Converter")
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
                .returns(itemsListClassName)
                .addParameter("value", String::class)
                .addStatement(
                    "val type = object : %T<%T>() {}.type",
                    typeTokenInterfaceName,
                    itemsListClassName
                )
                .addStatement("return %T().fromJson<%T>(value, type)", Gson::class, itemsListClassName)
                .build()
        )

        addFunction(
            FunSpec.builder("fromList")
                .addAnnotation(typeConverterClassName)
                .returns(String::class)
                .addParameter("list", itemsListClassName)
                .addStatement("val gson = %T()", Gson::class)
                .addStatement("return gson.toJson(list)")
                .build()
        )
    }
}