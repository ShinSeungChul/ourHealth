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
    }


}