package com.example.plainsofprimus

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plainsofprimus.model.Armor
import com.example.plainsofprimus.model.Weapon
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults

class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    var weapons: RealmResults<Weapon>? = null
    var armors: RealmResults<Armor>? = null

    private fun loadData() {
        val config = RealmConfiguration.Builder()
            .name("primus.realm").build()
        val realm = Realm.getInstance(config)

        armors = realm.where(Armor::class.java).findAll()
        println("Armors: ${armors?.size}")
        armors?.forEach { armor ->
            println("Armor: ${armor.name}")
        }

        weapons = realm.where(Weapon::class.java).findAll()
        println("Weapons: ${weapons?.size}")
        weapons?.forEach { weapon ->
            println("Weapon: ${weapon.name}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        loadData()
        println("Weapon: here")
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = weapons?.get(position)?.name ?: ""
    }

    override fun getItemCount(): Int {
        return weapons?.size ?: 0
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemTitle: TextView

        init {
            println("Weapon: here")
            itemTitle = itemView.findViewById(R.id.item_title)
        }
    }
}