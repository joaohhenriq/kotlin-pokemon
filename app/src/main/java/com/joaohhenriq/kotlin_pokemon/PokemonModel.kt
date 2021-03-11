package com.joaohhenriq.kotlin_pokemon

class Pokemon {
    var name: String = ""
    var des: String = ""
    var image: Int = 0
    var power: Double = 0.0
    var lat: Double = 0.0
    var lgn: Double = 0.0
    var isCatched: Boolean = false

    constructor(name: String, des: String, image: Int, power: Double, lat: Double, lgn: Double) {
        this.name = name
        this.des = des
        this.image = image
        this.power = power
        this.lat = lat
        this.lgn = lgn
        this.isCatched = false
    }
}