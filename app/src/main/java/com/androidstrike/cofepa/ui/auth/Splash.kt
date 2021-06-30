package com.androidstrike.cofepa.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.androidstrike.cofepa.R
import kotlinx.android.synthetic.main.fragment_splash.view.*

class Splash : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        view.logo_image.alpha = 0f
        view.logo_image.animate().setDuration(2000).alpha(1f).withEndAction { findNavController().navigate(R.id.action_splash_to_signIn) }

        return view
    }
}