package com.example.ukids

data class C2R(val address:Address, val road_address:RoadAddress)
data class Address(val address_name: String)
data class RoadAddress(val address_name: String)