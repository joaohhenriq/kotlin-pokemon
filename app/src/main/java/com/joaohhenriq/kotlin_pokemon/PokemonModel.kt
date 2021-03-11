package com.joaohhenriq.kotlin_pokemon

import android.location.Location

class Pokemon {
    var name: String = ""
    var des: String = ""
    var image: Int = 0
    var power: Double = 0.0
    var isCatched: Boolean = false
    var location: Location? = null

    constructor(name: String, des: String, image: Int, power: Double, lat: Double, lgn: Double) {
        this.name = name
        this.des = des
        this.image = image
        this.power = power
        this.location = Location("name")
        this.location!!.latitude = lat
        this.location!!.longitude = lgn
        this.isCatched = false
    }
}