package com.example.wlgusdn.ourhealth

class AllFood {
    var date : String? = null
    var time : String? = null
    var name : String? = null
    var kcal : Int? = null
    var car : Int? = null
    var pro : Int? = null
    var fat : Int? = null

    constructor(date : String , time : String , name: String , kcal: Int , car: Int , pro: Int , fat: Int)
    {
        this.date = date
        this.time = time
        this.name = name
        this.kcal = kcal
        this.car = car
        this.pro = pro
        this.fat = fat
    }



}