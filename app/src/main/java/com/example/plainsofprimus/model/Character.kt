package com.example.plainsofprimus.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Character(): RealmObject() {
    @PrimaryKey
    var id: Long = 0

    var username: String? = null

    var name: String? = null

    var level: Int? = null

    var weapon: Weapon? = null

    var helmet: Armor? = null

    var chestplate: Armor? = null

    var leggings: Armor? = null

    var boots: Armor? = null
}