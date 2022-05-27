package com.example.plainsofprimus.dto

import com.example.plainsofprimus.model.Weapon

class WeaponDTO {
    var name: String? = null

    var image: String? = null

    var attackDamage: Int? = null

    var specialBonus: String? = null

    constructor(weapon: Weapon) {
        this.name = weapon.name
        this.image = weapon.image
        this.attackDamage = weapon.attackDamage
        this.specialBonus = weapon.specialBonus
    }
}