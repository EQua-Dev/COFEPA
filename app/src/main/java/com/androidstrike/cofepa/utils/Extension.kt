package com.androidstrike.cofepa.utils

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.androidstrike.cofepa.ui.auth.AuthActivity
import com.androidstrike.cofepa.ui.landing.Landing

fun Context.toast(message:String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.login(){
    val intent = Intent(this, Landing::class.java).apply {
        //            we add these flags to ensure that we destroy all other activities when the HomeActivity is launched
//                    this prevents the user from being taken back to the login screen onBackPressed
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
}

fun Context.logout(){
    val intent = Intent(this, AuthActivity::class.java).apply {
        //            we add these flags to ensure that we destroy all other activities when the HomeActivity is launched
//                    this prevents the user from being taken back to the login screen onBackPressed
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
}

//common function to handle progress bar visibility
fun View.visible(isVisible: Boolean){
    visibility = if (isVisible) View.VISIBLE else View.GONE
}
