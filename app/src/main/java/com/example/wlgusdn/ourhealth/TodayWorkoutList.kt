package com.example.wlgusdn.health

class TodayWorkoutList {
    var time : String? = null
    var workout : String? = null//exercise step floor
    var kcal : Int? = null

    constructor(time : String , workout : String , kcal : Int)
    {
        this.time = time
        this.workout = workout
        this.kcal = kcal
    }
}