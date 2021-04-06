package com.androidstrike.cofepa.ui.landing.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.androidstrike.cofepa.R
import com.androidstrike.cofepa.interfaces.IOnBackPressed
import kotlinx.android.synthetic.main.fragment_home.*

class Home : Fragment(),IOnBackPressed  {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        webViewSetup()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun webViewSetup() {
        web_view.webViewClient = WebViewClient()
        web_view.apply {
            loadUrl("https://www.caritasuni.edu.ng/")
            settings.javaScriptEnabled = true

        }
    }

    override fun OnBackPressed(){
        if (web_view.canGoBack())
            web_view.goBack()
        else
            super.OnBackPressed()
    }



}