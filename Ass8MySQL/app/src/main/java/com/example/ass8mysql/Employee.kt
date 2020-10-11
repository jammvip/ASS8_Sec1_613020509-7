package com.example.ass8mysql

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Employee (
    @Expose
    @SerializedName( "name") val name: String,

    @Expose
    @SerializedName( "gender") val gender: String,

    @Expose
    @SerializedName( "email") val email: String,

    @Expose
    @SerializedName( "salary") val salary: Int){}