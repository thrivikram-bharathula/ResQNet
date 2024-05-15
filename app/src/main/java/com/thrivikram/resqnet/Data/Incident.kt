package com.thrivikram.resqnet.Data

import java.util.Dictionary

data class Incident(
    val id: Int,
    val address : String,
    val district : String,
    val state : String,
    val po : Int,
    val createdBy : String,
    val status : String,
    val incidentLocation : String,
    val creatorID: Int,
    val participants : List<Participant>,
    val creatorPhoneNumber : Int
)