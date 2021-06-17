package com.example.demo.model;

import kotlinx.serialization.Serializable

@Serializable
data class Version(val uid: String = "head", val number: Int = 0) {
    fun next() = Version(uid, number + 1)
}