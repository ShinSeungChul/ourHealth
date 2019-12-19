package com.example.wlgusdn.ourhealth
class TodayKcalList{
    var time : String = ""
    var foodname : String = ""
    var kcal : Int = 0
    var fat : Int = 0
    var protein : Int = 0
    var carbohydrate : Int = 0

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