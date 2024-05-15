package com.thrivikram.resqnet.Data

data class Participant (
    val participantId : Int,
    val participantType : String,
    val particiapantSubType : String,
    val onFieldID : Array<OnField>,
    val onFieldPhoneNumber : Int,
    val participantPhoneNumber : Int,
    val participantCurrentLocation: String,
)