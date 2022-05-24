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
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.plainsofprimus.model.Character
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.fragment_login.view.*
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
    private lateinit var sendScreenshot: Button


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

            characterName.text = character!!.name
            characterLevel.text = character!!.level.toString()

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
            }

            changeCharacterLevel.setOnClickListener {
                characterLevelInput.setText(character!!.level.toString())
                characterLevel.visibility = View.GONE
                characterLevelInput.visibility = View.VISIBLE
                changeCharacterLevel.visibility = View.GONE
                saveCharacterLevel.visibility = View.VISIBLE
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
            }

            sendScreenshot.setOnClickListener {
                var bitmap: Bitmap = takeScreenShot(view)
                shareScreenshot(bitmap)
            }
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
        var uri: Uri? = context?.let { getImageUri(it, inImage) }
        i.putExtra(Intent.EXTRA_STREAM, uri)
        i.type = "image/png"
        startActivity(Intent.createChooser(i, "Share"))
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
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