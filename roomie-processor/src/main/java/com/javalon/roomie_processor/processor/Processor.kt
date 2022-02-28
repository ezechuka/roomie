package com.javalon.roomie_processor.processor

import com.javalon.roomie_annotation.AddConverter
import com.javalon.roomie_processor.codegen.TypeConverterBuilder
import com.javalon.roomie_processor.logger.ProcessorLogger
import com.javalon.roomie_processor.model.ConverterData
import com.squareup.kotlinpoet.FileSpec
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

@SupportedSourceVersion(SourceVersion.RELEASE_8)
class Processor: AbstractProcessor() {

    private lateinit var typeUtils: Types
    private lateinit var elemUtils: Elements
    private lateinit var logger: ProcessorLogger

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        processingEnv?.let {
            typeUtils = it.typeUtils
            elemUtils = it.elementUtils
            logger = ProcessorLogger(it)
        }
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(AddConverter::class.java.canonicalName)
    }

    override fun process(p0: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
       val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
           ?: return false

       roundEnv?.getElementsAnnotatedWith(AddConverter::class.java)?.forEach {
            if (!validateTypeElement(it)) {
                logger.e("Must be applied to classes only.", it)
                return false
            }

           val converterData = getConverterData(it)
           val converter = it.getAnnotation(AddConverter::class.java)
           val fileName = if (converter.name.isEmpty()) {
               "${converterData.modelName}Converter"
           } else {
              converter.name
           }

           FileSpec.builder(converterData.packageName, fileName)
               .addType(TypeConverterBuilder(converterData, fileName).build())
               .build()
               .writeTo(File(kaptKotlinGeneratedDir))

       }
        return true
    }

    private fun getConverterData(elem: Element): ConverterData {
        val packageName = processingEnv.elementUtils.getPackageOf(elem).toString()
        val modelName = elem.simpleName.toString()
        return ConverterData(packageName, modelName)
    }

    private fun validateTypeElement(elem: Element): Boolean {
        (elem as? TypeElement)?.let {
            return true
        } ?: return false
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}