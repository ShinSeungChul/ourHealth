package com.example.wlgusdn.ourhealth

class ClientData
{
    var name : String? = null
    var id : String? = null
    var age : Int? = null
    var height : Int? = null
    var weight : Int? = null
    var kcal : Int? = null
    var car : Int? = null
    var pro : Int? = null
    var fat : Int? = null

    var fatper : Float? = null
    var proper : Float? = null
    var carper : Float? = null
    var tfat : Float? = null
    var tpro : Float? = null
    var tcar : Float? = null
    var calavg : Float? = null

    var breakfasttime : Int? = null
    var launchtime : Int? = null
    var dinnertime : Int? = null
    var extracount : Int? = null

    constructor(name : String , id : String , age : Int , height : Int , weight : Int , kcal : Int , car : Int , pro : Int , fat : Int)
    {
        this.name = name
        this.id = id
        this.age = age
        this.height = height
        this.weight = weight
        this.kcal = kcal
        this.car = car
        this.pro = pro
        this.fat = fat
        this.fatper = 0f
        this.proper = 0f
        this.carper = 0f
        this.tfat = 0f
        this.tcar = 0f
        this.tpro = 0f

        this.breakfasttime = 8
        this.launchtime = 12
        this.dinnertime = 6
        this.extracount = 0
        this.calavg = 0f

    }


}