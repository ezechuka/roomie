package com.javalon.roomie_processor.logger

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.tools.Diagnostic

class ProcessorLogger(private val env: ProcessingEnvironment) {

    fun n(message: String, elem: Element?) {
        print(Diagnostic.Kind.NOTE, message, elem)
    }

    fun w(message: String, elem: Element?) {
        print(Diagnostic.Kind.WARNING, message, elem)
    }

    fun e(message: String, elem: Element?) {
        print(Diagnostic.Kind.ERROR, message, elem)
    }

    fun print(kind: Diagnostic.Kind, message: String, elem: Element?) {
        env.messager.printMessage(kind, message, elem)
    }
}