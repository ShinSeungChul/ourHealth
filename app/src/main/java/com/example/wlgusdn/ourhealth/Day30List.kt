package com.example.wlgusdn.ourhealth

class Day30List{
    var time : String = ""
    var kcal : Int = 0
    var fat : Int = 0
    var protein : Int = 0
    var carbohydrate : Int = 0
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