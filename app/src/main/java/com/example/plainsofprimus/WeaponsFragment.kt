package com.example.plainsofprimus

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plainsofprimus.model.Armor
import com.example.plainsofprimus.model.Weapon
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_weapons.*

class WeaponsFragment : Fragment() {

    var weapons: List<Weapon>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_weapons, container, false)

        loadData()

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        println("View1: ${recyclerView.toString()}")

        recyclerView.setHasFixedSize(true)

        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = weapons?.let { WeaponsAdapter(it, R.layout.rowlayout) }

        return view
    }

    private fun loadData() {
        val config = RealmConfiguration.Builder()
            .name("primus.realm").build()
        val realm = Realm.getInstance(config)

        weapons = realm.where(Weapon::class.java).findAll()
        println("Weapons: ${weapons?.size}")
        weapons?.forEach { weapon ->
            println("Weapon: ${weapon.name}")
        }
    }

    inner class WeaponsAdapter(val weapons: List<Weapon>, val itemLayout: Int): RecyclerView.Adapter<WeaponsFragment.WeaponViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeaponViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
            return WeaponViewHolder(v)
        }

        override fun onBindViewHolder(holder: WeaponViewHolder, position: Int) {
            holder.itemTitle.text = weapons[position].name
        }

        override fun getItemCount(): Int {
            return weapons.size
        }

    }

    inner class WeaponViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemTitle: TextView = itemView.findViewById(R.id.item_title)
    }
}