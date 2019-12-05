package com.example.wlgusdn.ourhealth

class Day30List{
    var time : String? = null
    var kcal : Int? = null
    var fat : Int? = null
    var protein : Int? = null
    var carbohydrate : Int? = null
    var workout : Int = 0


    fun getWorkoutKcal() : Int
    {
        return workout
    }
    fun setWorkoutKcal(kcal : Int)
    {
        workout += kcal
    }





}