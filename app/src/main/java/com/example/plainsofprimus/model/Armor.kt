package com.example.plainsofprimus.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Armor(): RealmObject() {
    @PrimaryKey
    var id: Long = 0

    var name: String? = null

    var image: String? = null

    var type: String? = null

    var armorValue: Int? = null

    var health: Int? = null

    var strength: Int? = null

    var intellect: Int? = null

    var agility: Int? = null
}