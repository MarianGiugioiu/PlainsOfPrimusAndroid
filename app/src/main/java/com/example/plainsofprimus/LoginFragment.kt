package com.example.plainsofprimus

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.plainsofprimus.model.Character
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.fragment_login.*
import kotlin.math.log

class LoginFragment : Fragment() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var signInButton: SignInButton
    private lateinit var signOutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_login, container, false)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }!!

        auth = Firebase.auth

        signInButton = view.findViewById(R.id.sign_in_button)
        signOutButton = view.findViewById(R.id.sign_out_button)

        signInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        signOutButton.setOnClickListener {
            auth.signOut()
            googleSignInClient.signOut()
            changeFragment()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        initViews()
    }

    fun initViews() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.w(TAG, "user not signed in..")
            signInButton.visibility = View.VISIBLE
            signOutButton.visibility = View.GONE
        } else {
            signInButton.visibility = View.GONE
            signOutButton.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(MainActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user == null) {
            return
        }

        val config = RealmConfiguration.Builder()
            .name("primus.realm").build()
        val realm = Realm.getInstance(config)

        val character = realm.where(Character::class.java).equalTo("username", user.email).findFirst()
        if (character == null) {
            val maxId: Number? = realm.where(Character::class.java).max("id")
            val nextId = if (maxId == null) 1 else maxId.toInt() + 1
            Log.d(TAG, "updateUI: $nextId")
            realm.beginTransaction()
            val newCharacter = realm.createObject(Character::class.java, nextId)
            newCharacter.username = user.email
            newCharacter.name = ""
            newCharacter.level = 1
            newCharacter.weapon = null
            newCharacter.helmet = null
            newCharacter.chestplate = null
            newCharacter.leggings = null
            newCharacter.boots = null
            realm.commitTransaction()
        } else {
            character.username?.let { Log.d(TAG, "old character$it") }
        }

        changeFragment()
    }

    private fun changeFragment() {
        (activity as MainActivity).setToolbarTitle("Home")
        val fragmentManager = getFragmentManager()?.beginTransaction()
        fragmentManager?.replace(R.id.fragment_container, HomeFragment())?.commit()
    }

    companion object {
        private const val TAG = "LoginFragment"
        private const val RC_SIGN_IN = 9001
    }
}