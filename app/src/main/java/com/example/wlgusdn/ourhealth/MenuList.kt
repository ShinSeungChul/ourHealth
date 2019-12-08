package com.example.wlgusdn.ourhealth

class MenuList {

    var foodname : String? = null
    var recommend : String? = null
    var kcal : String? = null
    var car : String? = null
    var pro : String? = null
    var fat : String? = null


    constructor(fn : String , recommend : String , kcal : String , car : String , pro : String , fat : String)
    {
        this.foodname = fn
        this.recommend = recommend
        this.kcal = kcal
        this.car = car
        this.pro = pro
        this.fat = fat

    }
}