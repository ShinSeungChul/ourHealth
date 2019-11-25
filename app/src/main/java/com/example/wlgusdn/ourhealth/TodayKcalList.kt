package com.example.wlgusdn.ourhealth
class TodayKcalList{
    var time : String? = null
    var foodname : String? = null
    var kcal : Int? = null

    constructor(time : String, foodname : String , kcal : Int)
    {
        this.time = time
        this.foodname = foodname
        this.kcal = kcal

    }


}