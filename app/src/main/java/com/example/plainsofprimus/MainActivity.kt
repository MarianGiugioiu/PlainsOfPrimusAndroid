package com.example.plainsofprimus

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.plainsofprimus.model.Armor
import com.example.plainsofprimus.model.Weapon
import com.google.android.material.navigation.NavigationView
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        nav_menu.setNavigationItemSelectedListener(this)
        nav_menu.itemIconTintList = null
        nav_menu.setBackgroundResource(R.drawable.frag_bg)

        val states = arrayOf(
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf(android.R.attr.state_enabled)
        )

        val colors = intArrayOf(
            Color.WHITE,
            Color.WHITE,
            Color.WHITE,
            Color.WHITE,
            Color.WHITE
        )

        val myList = ColorStateList(states, colors)

        nav_menu.itemTextColor = myList

        setToolbarTitle("Plains of Primus")
        changeFragment(HomeFragment())

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)

        when(item.itemId){
            R.id.home -> {
                setToolbarTitle("Home")
                changeFragment(HomeFragment())
            }

            R.id.character -> {
                setToolbarTitle("Character")
                changeFragment(CharacterFragment())
            }

            R.id.armors -> {
                setToolbarTitle("Armors")
                changeFragment(ArmorsFragment())
            }

            R.id.weapons -> {
                setToolbarTitle("Weapons")
                changeFragment(WeaponsFragment())
            }

            R.id.account -> {
                setToolbarTitle("Account")
                changeFragment(LoginFragment())
            }
        }
        return true
    }

    private fun changeFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager.beginTransaction()
        fragmentManager.replace(R.id.fragment_container, fragment).commit()
    }

    fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    private fun initData() {
        Realm.init(this)

        val config = RealmConfiguration.Builder()
            .name("primus.realm").build()
        //Realm.deleteRealm(config)

        val realm = Realm.getInstance(config)

        /*realm.beginTransaction()
        realm.deleteAll()
        realm.commitTransaction()*/

        var nextId = 1

        //Armors
        val allArmors = realm.where(Armor::class.java).findAll()
        if (allArmors.size == 0) {
            realm.beginTransaction()

            val armor1 = realm.createObject(Armor::class.java, nextId)
            armor1.name = "DiamondHelmet"
            armor1.type = "helmet"
            armor1.armorValue = 80
            armor1.health = 70
            armor1.strength = 50
            armor1.intellect = 80
            armor1.agility = 50
            armor1.image = "https://www.seekpng.com/png/detail/154-1548200_the-diamond-helmet-minecraft-diamond-helmet-png.png"
            nextId += 1

            val armor2 = realm.createObject(Armor::class.java, nextId)
            armor2.name = "DiamondChestplate"
            armor2.type = "chestplate"
            armor2.armorValue = 90
            armor2.health = 80
            armor2.strength = 70
            armor2.intellect = 60
            armor2.agility = 60
            armor2.image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdFrqhzqtg9tCc7xG0fvLOOBOe9oPgMgDHmg&usqp=CAU"
            nextId += 1

            val armor3 = realm.createObject(Armor::class.java, nextId)
            armor3.name = "DiamondLeggings"
            armor3.type = "leggings"
            armor3.armorValue = 90
            armor3.health = 80
            armor3.strength = 70
            armor3.intellect = 50
            armor3.agility = 90
            armor3.image = "https://static.wikia.nocookie.net/minecraft_gamepedia/images/8/87/Diamond_Leggings_%28item%29_JE3_BE3.png/revision/latest?cb=20200226193941"
            nextId += 1

            val armor4 = realm.createObject(Armor::class.java, nextId)
            armor4.name = "DiamondBoots"
            armor4.type = "boots"
            armor4.armorValue = 80
            armor4.health = 60
            armor4.strength = 60
            armor4.intellect = 30
            armor4.agility = 90
            armor4.image = "https://static.wikia.nocookie.net/minecraft_gamepedia/images/0/01/Diamond_Boots_%28item%29_JE3_BE3.png/revision/latest?cb=20200226193855"
            nextId += 1

            val armor5 = realm.createObject(Armor::class.java, nextId)
            armor5.name = "GoldenHelmet"
            armor5.type = "helmet"
            armor5.armorValue = 60
            armor5.health = 50
            armor5.strength = 40
            armor5.intellect = 70
            armor5.agility = 50
            armor5.image = "https://static.wikia.nocookie.net/minecraft_gamepedia/images/6/67/Golden_Helmet_%28item%29_JE3_BE3.png/revision/latest?cb=20190406141031"
            nextId += 1

            val armor6 = realm.createObject(Armor::class.java, nextId)
            armor6.name = "GoldenChestplate"
            armor6.type = "chestplate"
            armor6.armorValue = 70
            armor6.health = 50
            armor6.strength = 50
            armor6.intellect = 40
            armor6.agility = 60
            armor6.image = "https://static.wikia.nocookie.net/minecraft_gamepedia/images/f/f6/Golden_Chestplate_%28item%29_JE1_BE1.png/revision/latest?cb=20190403172902"
            nextId += 1

            val armor7 = realm.createObject(Armor::class.java, nextId)
            armor7.name = "GoldenLeggings"
            armor7.type = "leggings"
            armor7.armorValue = 80
            armor7.health = 70
            armor7.strength = 60
            armor7.intellect = 40
            armor7.agility = 90
            armor7.image = "https://static.wikia.nocookie.net/minecraft_gamepedia/images/3/39/Golden_Leggings_%28item%29_JE1_BE1.png/revision/latest?cb=20190403172957"
            nextId += 1

            val armor8 = realm.createObject(Armor::class.java, nextId)
            armor8.name = "GoldenBoots"
            armor8.type = "boots"
            armor8.armorValue = 70
            armor8.health = 50
            armor8.strength = 50
            armor8.intellect = 40
            armor8.agility = 90
            armor8.image = "https://static.wikia.nocookie.net/minecraft_gamepedia/images/5/55/Golden_Boots_%28item%29_JE2_BE2.png/revision/latest?cb=20190407145755"
            nextId += 1

            realm.commitTransaction()

            /*allArmors.forEach { armor ->
                println("Armor: ${armor.name}")
            }*/
        }

        //Weapons
        val allWeapons = realm.where(Weapon::class.java).findAll()
        if (allWeapons.size == 0) {
            nextId = 1
            realm.beginTransaction()
            val weapon1 = realm.createObject(Weapon::class.java, nextId)
            weapon1.name = "DiamondSword"
            weapon1.attackDamage = 200
            weapon1.specialBonus = "Armor penetration: 30%"
            weapon1.image = "https://static.wikia.nocookie.net/minecraft_gamepedia/images/6/6a/Diamond_Sword_JE2_BE2.png/revision/latest?cb=20200217235945"
            nextId += 1

            val weapon2 = realm.createObject(Weapon::class.java, nextId)
            weapon2.name = "GoldenSword"
            weapon2.attackDamage = 150
            weapon2.specialBonus = "Lifesteal: 10%"
            weapon2.image = "https://static.wikia.nocookie.net/minecraft_gamepedia/images/0/03/Golden_Sword_JE1.png/revision/latest?cb=20190516111417"
            nextId += 1

            val weapon3 = realm.createObject(Weapon::class.java, nextId)
            weapon3.name = "IronSword"
            weapon3.attackDamage = 100
            weapon3.specialBonus = "Cleave: 50%"
            weapon3.image = "https://static.wikia.nocookie.net/kingdom-of-fun/images/c/c7/Iron_Sword.png/revision/latest/smart/width/250/height/250?cb=20150805101123"
            nextId += 1

            realm.commitTransaction()

            /*allWeapons.forEach { weapon ->
                println("Weapon: ${weapon.name}")
            }*/
        }
    }
}