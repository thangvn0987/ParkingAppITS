package com.example.parkingappits.data.remote

import com.example.parkingappits.domain.model.RouteRequestBody
import com.example.parkingappits.domain.model.RouteResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenRouteServiceApi {
    @POST("v2/directions/driving-car/geojson")
    suspend fun getRoute(
        @Header("Authorization") apiKey: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Body body: RouteRequestBody
    ): RouteResponse


}
