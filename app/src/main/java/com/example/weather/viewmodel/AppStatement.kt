package com.example.weather.viewmodel

sealed class AppStatement {
    data class Loading(var progress:Int):AppStatement()
    data class Success(var temperature:Int,var feelsLike:Int):AppStatement()
    data class Error( var error:Throwable):AppStatement()
}