package br.com.sscode.core.extensions

fun <T> List<T>.anotherInstance(): List<T> = ArrayList(this)