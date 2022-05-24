package com.example.plainsofprimus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plainsofprimus.model.Armor
import io.realm.Realm
import io.realm.RealmConfiguration

class ArmorsFragment : Fragment() {

    var armors: List<Armor>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_armors, container, false)

        loadData()

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        println("View1: ${recyclerView.toString()}")

        recyclerView.setHasFixedSize(true)

        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = armors?.let { ArmorsAdapter(it, R.layout.rowlayout) }

        return view
    }

    private fun loadData() {
        val config = RealmConfiguration.Builder()
            .name("primus.realm").build()
        val realm = Realm.getInstance(config)

        armors = realm.where(Armor::class.java).findAll()
        println("Armors: ${armors?.size}")
        armors?.forEach { armor ->
            println("Armor: ${armor.name}")
        }
    }

    inner class ArmorsAdapter(val armors: List<Armor>, val itemLayout: Int): RecyclerView.Adapter<ArmorsFragment.ArmorViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArmorViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
            return ArmorViewHolder(v)
        }

        override fun onBindViewHolder(holder: ArmorViewHolder, position: Int) {
            holder.itemTitle.text = armors[position].name
        }

        override fun getItemCount(): Int {
            return armors.size
        }

    }

    inner class ArmorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemTitle: TextView = itemView.findViewById(R.id.item_title)
    }

}