package com.javalon.roomie

import com.javalon.roomie_annotation.AddConverter

@AddConverter(name = "PersonListConverter")
data class Person(
    val firstName: String,
    val lastName: String,
    val age: Int,
    val gender: String
)

@AddConverter
data class User(
    val id: Long,
    val isApproved: Boolean
)