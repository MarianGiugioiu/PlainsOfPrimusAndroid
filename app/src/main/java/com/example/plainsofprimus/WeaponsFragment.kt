package com.example.plainsofprimus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plainsofprimus.dto.WeaponDTO
import com.example.plainsofprimus.model.Weapon
import com.example.plainsofprimus.model.Character
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.*
import kotlin.collections.ArrayList

class WeaponsFragment : Fragment() {
    private var weapons: List<WeaponDTO>? = null
    private lateinit var searchBar: SearchView
    private lateinit var adapter: WeaponsAdapter
    private var character: Character? = null
    private lateinit var realm: Realm

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_weapons, container, false)

        loadData()

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)

        recyclerView.setHasFixedSize(true)

        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = weapons?.let { WeaponsAdapter(it as ArrayList<WeaponDTO>) }!!

        recyclerView.adapter = adapter

        searchBar = view.findViewById(R.id.search_bar)

        searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                adapter.filter.filter(p0)
                return true
            }

        })

        return view
    }

    private fun loadData() {
        val user = Firebase.auth.currentUser
        val config = RealmConfiguration.Builder()
            .name("primus.realm").build()
        realm = Realm.getInstance(config)
        if (user != null) {
            character = realm.where(Character::class.java).equalTo("username", user.email).findFirst()
        }

        val weaponsRealm = realm.where(Weapon::class.java).findAll()

        weapons = ArrayList()
        weaponsRealm?.forEach { weapon ->
            (weapons as ArrayList<WeaponDTO>).add(WeaponDTO(weapon))
        }
    }

    inner class WeaponsAdapter(val weapons: List<WeaponDTO>): RecyclerView.Adapter<WeaponsFragment.WeaponViewHolder>(), Filterable {
        private var weaponsFilter: List<WeaponDTO>? = null
        init {
            weaponsFilter = weapons
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeaponViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_weapon, parent, false)
            return WeaponViewHolder(v)
        }

        override fun onBindViewHolder(holder: WeaponViewHolder, position: Int) {
            holder.itemName.text = weaponsFilter!![position].name
            holder.itemDamage.text = weaponsFilter!![position].attackDamage.toString()
            holder.itemEffect.text = weaponsFilter!![position].specialBonus
            Picasso.with(context).load(weaponsFilter!![position].image).into(holder.itemImage)
            if (character == null) {
                holder.itemUse.visibility = View.GONE
            } else {
                holder.itemUse.setOnClickListener {
                    val currentWeapon = realm.where(Weapon::class.java)
                        .equalTo("name", weaponsFilter!![position].name).findFirst()
                    realm.beginTransaction()
                    character!!.weapon = currentWeapon
                    realm.commitTransaction()
                }
            }
        }

        override fun getItemCount(): Int {
            return weaponsFilter!!.size
        }

        override fun getFilter(): Filter {
            return object: Filter() {
                override fun performFiltering(p0: CharSequence?): FilterResults {
                    weaponsFilter = if (p0 == null || p0.isBlank()) {
                        weapons
                    } else {
                        val filteredList = ArrayList<WeaponDTO>()
                        weapons.filter {
                            (it.name!!.lowercase(Locale.getDefault()).contains(p0.toString()
                                .lowercase(Locale.getDefault())))
                        }.forEach {filteredList.add(it)}
                        filteredList
                    }

                    weaponsFilter?.forEach { weapon ->
                        println("Weapon: ${weapon.name}")
                    }

                    return FilterResults().apply { values = weaponsFilter }
                }

                override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                    weaponsFilter = if (p1?.values == null)
                        ArrayList()
                    else
                        p1.values as List<WeaponDTO>?

                    weaponsFilter?.forEach { weapon ->
                        println("Weapon: ${weapon.name}")
                    }

                    notifyDataSetChanged()
                }

            }
        }

    }

    inner class WeaponViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemName: TextView = itemView.findViewById(R.id.item_name)
        var itemDamage: TextView = itemView.findViewById(R.id.item_damage)
        var itemEffect: TextView = itemView.findViewById(R.id.item_effect)
        var itemImage: ImageView = itemView.findViewById(R.id.item_image)
        var itemUse: AppCompatButton = itemView.findViewById(R.id.item_use)
    }

    companion object {
        private const val TAG = "WeaponFragment"
    }

}