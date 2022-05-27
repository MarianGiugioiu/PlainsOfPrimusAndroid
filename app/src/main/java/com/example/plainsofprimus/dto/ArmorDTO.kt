package com.example.plainsofprimus.dto

import com.example.plainsofprimus.model.Armor
import io.realm.RealmObject

class ArmorDTO {
    var name: String? = null

    var image: String? = null

    var type: String? = null

    var armorValue: Int? = null

    var health: Int? = null

    var strength: Int? = null

    var intellect: Int? = null

    var agility: Int? = null

    constructor(armor: Armor) {
        this.name = armor.name
        this.image = armor.image
        this.type = armor.type
        this.armorValue = armor.armorValue
        this.health = armor.health
        this.strength = armor.strength
        this.intellect = armor.intellect
        this.agility = armor.agility
    }
}