package com.androidstrike.cofepa.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.androidstrike.cofepa.R
import com.androidstrike.cofepa.ui.auth.AuthActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*


class SplashScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        logo_image.alpha = 0f
        logo_image.animate().setDuration(2000).alpha(1f).withEndAction{
            val i  = Intent(this, AuthActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}