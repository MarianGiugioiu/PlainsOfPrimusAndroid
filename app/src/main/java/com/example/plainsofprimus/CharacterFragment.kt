package com.example.plainsofprimus

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.plainsofprimus.model.Character
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import io.realm.Realm
import io.realm.RealmConfiguration
import java.io.ByteArrayOutputStream


class CharacterFragment : Fragment() {
    private var character: Character? = null
    private lateinit var realm: Realm
    private lateinit var characterName: TextView
    private lateinit var characterLevel: TextView
    private lateinit var characterNameInput: EditText
    private lateinit var characterLevelInput: EditText
    private lateinit var changeCharacterName: Button
    private lateinit var changeCharacterLevel: Button
    private lateinit var saveCharacterName: Button
    private lateinit var saveCharacterLevel: Button
    private lateinit var characterHelmet: ImageView
    private lateinit var characterChestplate: ImageView
    private lateinit var characterLeggings: ImageView
    private lateinit var characterBoots: ImageView
    private lateinit var characterWeapon: ImageView
    private lateinit var sendScreenshot: Button
    private lateinit var layout: ConstraintLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_character, container, false)
        loadData()
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        layout = view.findViewById(R.id.character_layout)
        if (character != null) {
            characterName = view.findViewById(R.id.character_name)
            characterLevel = view.findViewById(R.id.character_level)
            characterNameInput = view.findViewById(R.id.character_name_input)
            characterLevelInput = view.findViewById(R.id.character_level_input)
            changeCharacterName = view.findViewById(R.id.change_character_name)
            changeCharacterLevel = view.findViewById(R.id.change_character_level)
            saveCharacterName = view.findViewById(R.id.save_character_name)
            saveCharacterLevel = view.findViewById(R.id.save_character_level)
            sendScreenshot = view.findViewById(R.id.send_screenshot)
            characterHelmet = view.findViewById(R.id.character_helmet)
            characterChestplate = view.findViewById(R.id.character_chestplate)
            characterLeggings = view.findViewById(R.id.character_leggings)
            characterBoots = view.findViewById(R.id.character_boots)
            characterWeapon = view.findViewById(R.id.character_weapon)

            characterName.text = character!!.name
            characterLevel.text = character!!.level.toString()
            if (character!!.helmet != null && character!!.helmet!!.image != null) {
                Picasso.with(context).load(character!!.helmet!!.image).into(characterHelmet)
            }
            if (character!!.chestplate != null && character!!.chestplate!!.image != null) {
                Picasso.with(context).load(character!!.chestplate!!.image).into(characterChestplate)
            }
            if (character!!.leggings != null && character!!.leggings!!.image != null) {
                Picasso.with(context).load(character!!.leggings!!.image).into(characterLeggings)
            }
            if (character!!.boots != null && character!!.boots!!.image != null) {
                Picasso.with(context).load(character!!.boots!!.image).into(characterBoots)
            }

            if (character!!.weapon != null && character!!.weapon!!.image != null) {
                Picasso.with(context).load(character!!.weapon!!.image).into(characterWeapon)
            }

            characterName.visibility = View.VISIBLE
            characterLevel.visibility = View.VISIBLE
            changeCharacterName.visibility = View.VISIBLE
            changeCharacterLevel.visibility = View.VISIBLE

            changeCharacterName.setOnClickListener {
                characterNameInput.setText(character!!.name)
                characterName.visibility = View.GONE
                characterNameInput.visibility = View.VISIBLE
                changeCharacterName.visibility = View.GONE
                saveCharacterName.visibility = View.VISIBLE
                sendScreenshot.visibility = View.GONE
            }

            changeCharacterLevel.setOnClickListener {
                characterLevelInput.setText(character!!.level.toString())
                characterLevel.visibility = View.GONE
                characterLevelInput.visibility = View.VISIBLE
                changeCharacterLevel.visibility = View.GONE
                saveCharacterLevel.visibility = View.VISIBLE
                sendScreenshot.visibility = View.GONE
            }

            saveCharacterName.setOnClickListener {
                realm.beginTransaction()
                character!!.name = characterNameInput.text.toString()
                realm.commitTransaction()
                characterName.text = character!!.name
                characterName.visibility = View.VISIBLE
                characterNameInput.visibility = View.GONE
                changeCharacterName.visibility = View.VISIBLE
                saveCharacterName.visibility = View.GONE
                sendScreenshot.visibility = View.VISIBLE
            }

            saveCharacterLevel.setOnClickListener {
                realm.beginTransaction()
                character!!.level = Integer.parseInt(characterLevelInput.text.toString())
                realm.commitTransaction()
                characterLevel.text = character!!.level.toString()
                characterLevel.visibility = View.VISIBLE
                characterLevelInput.visibility = View.GONE
                changeCharacterLevel.visibility = View.VISIBLE
                saveCharacterLevel.visibility = View.GONE
                sendScreenshot.visibility = View.VISIBLE
            }

            sendScreenshot.setOnClickListener {
                changeCharacterName.visibility = View.GONE
                changeCharacterLevel.visibility = View.GONE

                val bitmap: Bitmap = takeScreenShot(view)

                changeCharacterName.visibility = View.VISIBLE
                changeCharacterLevel.visibility = View.VISIBLE

                shareScreenshot(bitmap)
            }
        } else {
            layout.visibility = View.GONE
        }
    }

    private fun loadData() {
        val user = Firebase.auth.currentUser
        val config = RealmConfiguration.Builder()
            .name("primus.realm").build()
        realm = Realm.getInstance(config)
        if (user != null) {
            character = realm.where(Character::class.java).equalTo("username", user.email).findFirst()
        }
    }

    private fun takeScreenShot(view: View): Bitmap {
        val screenshot = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val draw = Canvas(screenshot)
        val drawGeeks = view.background
        if (drawGeeks != null) drawGeeks.draw(draw)
        else draw.drawColor(Color.WHITE)
        view.draw(draw)
        return screenshot
    }

    private fun shareScreenshot(inImage: Bitmap) {
        val i = Intent(Intent.ACTION_SEND)
        val uri: Uri? = context?.let { getImageUri(it, inImage) }
        i.putExtra(Intent.EXTRA_STREAM, uri)
        i.type = "image/png"
        startActivity(Intent.createChooser(i, "Share"))
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Screenshot",
            null
        )
        return Uri.parse(path)
    }

    companion object {
        private const val TAG = "CharacterFragment"
    }
}