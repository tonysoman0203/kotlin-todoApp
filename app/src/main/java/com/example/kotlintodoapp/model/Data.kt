package com.example.kotlintodoapp.model

data class Data(
    val place: String,
    val unit: String,
    val value: Int
) {
    fun getValueWithUnit(): String {
        return "${this.value} ${"\u2103"}"
    }
}
