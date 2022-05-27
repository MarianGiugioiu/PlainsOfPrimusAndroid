package com.example.plainsofprimus

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plainsofprimus.dto.ArmorDTO
import com.example.plainsofprimus.model.Armor
import com.example.plainsofprimus.model.Character
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.*
import kotlin.collections.ArrayList

class ArmorsFragment : Fragment() {
    private var armors: List<ArmorDTO>? = null
    private lateinit var searchBar: SearchView
    private lateinit var adapter: ArmorsAdapter
    private var character: Character? = null
    private lateinit var realm: Realm

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_armors, container, false)

        loadData()

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)

        recyclerView.setHasFixedSize(true)

        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = armors?.let { ArmorsAdapter(it as ArrayList<ArmorDTO>) }!!

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

        val armorsRealm = realm.where(Armor::class.java).findAll()

        armors = ArrayList()
        armorsRealm?.forEach { armor ->
            (armors as ArrayList<ArmorDTO>).add(ArmorDTO(armor))
        }
    }

    inner class ArmorsAdapter(val armors: List<ArmorDTO>): RecyclerView.Adapter<ArmorsFragment.ArmorViewHolder>(), Filterable {
        private var armorsFilter: List<ArmorDTO>? = null
        init {
            armorsFilter = armors
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArmorViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_armor, parent, false)
            return ArmorViewHolder(v)
        }

        override fun onBindViewHolder(holder: ArmorViewHolder, position: Int) {
            holder.itemName.text = armorsFilter!![position].name
            holder.itemArmorValue.text = armorsFilter!![position].armorValue.toString()
            holder.itemHealth.text = armorsFilter!![position].health.toString()
            holder.itemStrength.text = armorsFilter!![position].strength.toString()
            holder.itemIntellect.text = armorsFilter!![position].intellect.toString()
            holder.itemAgility.text = armorsFilter!![position].agility.toString()
            Picasso.with(context).load(armorsFilter!![position].image).into(holder.itemImage)

            if (character == null) {
                holder.itemUse.visibility = View.GONE
            } else {
                holder.itemUse.text = "Use " + armorsFilter!![position].type
                holder.itemUse.setOnClickListener {
                    val currentArmor = realm.where(Armor::class.java)
                        .equalTo("name", armorsFilter!![position].name).findFirst()
                    Log.d(TAG, currentArmor!!.type.toString())
                    realm.beginTransaction()
                    if (armorsFilter!![position].type.equals("helmet")) {
                        character!!.helmet = currentArmor
                    } else if (armorsFilter!![position].type.equals("chestplate")) {
                        character!!.chestplate = currentArmor
                    } else if (armorsFilter!![position].type.equals("leggings")) {
                        character!!.leggings = currentArmor
                    } else if (armorsFilter!![position].type.equals("boots")) {
                        character!!.boots = currentArmor
                    }
                    realm.commitTransaction()
                }
            }
        }

        override fun getItemCount(): Int {
            return armorsFilter!!.size
        }

        override fun getFilter(): Filter {
            return object: Filter() {
                override fun performFiltering(p0: CharSequence?): FilterResults {
                    armorsFilter = if (p0 == null || p0.isBlank()) {
                        armors
                    } else {
                        val filteredList = ArrayList<ArmorDTO>()
                        armors.filter {
                            (it.name!!.lowercase(Locale.getDefault()).contains(p0.toString()
                                .lowercase(Locale.getDefault())))
                        }.forEach {filteredList.add(it)}
                        filteredList
                    }

                    armorsFilter?.forEach { armor ->
                        println("Armor: ${armor.name}")
                    }

                    return FilterResults().apply { values = armorsFilter }
                }

                override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                    armorsFilter = if (p1?.values == null)
                        ArrayList()
                    else
                        p1.values as List<ArmorDTO>?

                    armorsFilter?.forEach { armor ->
                        println("Armor: ${armor.name}")
                    }

                    notifyDataSetChanged()
                }

            }
        }

    }

    inner class ArmorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemName: TextView = itemView.findViewById(R.id.item_name)
        var itemArmorValue: TextView = itemView.findViewById(R.id.item_armor)
        var itemHealth: TextView = itemView.findViewById(R.id.item_health)
        var itemStrength: TextView = itemView.findViewById(R.id.item_strength)
        var itemIntellect: TextView = itemView.findViewById(R.id.item_intellect)
        var itemAgility: TextView = itemView.findViewById(R.id.item_agility)
        var itemImage: ImageView = itemView.findViewById(R.id.item_image)
        var itemUse: AppCompatButton = itemView.findViewById(R.id.item_use)
    }

    companion object {
        private const val TAG = "ArmorFragment"
    }

}