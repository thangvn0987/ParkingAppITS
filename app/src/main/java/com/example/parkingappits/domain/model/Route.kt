package com.example.parkingappits.domain.model

data class Route(
    val coordinates: List<List<Double>>,  // polyline: [[lng, lat], ...]
    val distance: Double,
    val duration: Double
)
