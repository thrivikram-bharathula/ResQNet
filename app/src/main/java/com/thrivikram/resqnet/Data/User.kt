package com.thrivikram.resqnet.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserTable")
data class User (
    val UserName :String,
    @PrimaryKey(autoGenerate = true)
    val aadharNumber : Int,
    val phoneNumber : Int,
    val address : String,
    val district : String,
    val state : String,
    val po : Int,
    val currLocation : String,
    val street : String,
    val houseNumber : String,
    val landmark : String,
    val otp : Int,
)