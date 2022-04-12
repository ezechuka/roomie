package com.javalon

/**
 * Automatically generates a TypeConverter
 * for the class with this annotation.
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class AddConverter(val name: String = "", val asList: Boolean = false)