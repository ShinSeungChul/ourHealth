package com.example.wlgusdn.ourhealth
class TodayKcalList{
    var time : String? = null
    var foodname : String? = null
    var kcal : Int? = null
    var fat : Int? = null
    var protein : Int? = null
    var carbohydrate : Int? = null

    constructor(time : String, foodname : String , kcal : Int,fat : Int,protein : Int,carbo : Int)
    {
        this.time = time
        this.foodname = foodname
        this.kcal = kcal
        this.fat = fat
        this.protein = protein
        this.carbohydrate = carbo
    }


}