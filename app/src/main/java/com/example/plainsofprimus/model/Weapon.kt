package com.example.plainsofprimus.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Weapon(): RealmObject() {
    @PrimaryKey
    var id: Long = 0

    var name: String? = null

    var image: String? = null

    var attackDamage: Int? = null

    var specialBonus: String? = null
}