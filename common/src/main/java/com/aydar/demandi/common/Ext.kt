package com.aydar.demandi.common

fun Any.getTag(): String {
    return this::class.java.simpleName
}