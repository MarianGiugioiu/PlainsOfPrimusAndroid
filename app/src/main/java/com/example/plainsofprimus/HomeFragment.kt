package com.example.plainsofprimus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth

        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        val userEmail: TextView = view.findViewById(R.id.user_email)

        //userEmail.text = auth.currentUser?.email

        return view
    }

}