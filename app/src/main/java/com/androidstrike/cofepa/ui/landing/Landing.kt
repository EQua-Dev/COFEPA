package com.androidstrike.cofepa.ui.landing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.close
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.androidstrike.cofepa.R
import com.androidstrike.cofepa.ui.landing.fragments.Home
import com.androidstrike.cofepa.ui.landing.fragments.Payment
import com.androidstrike.cofepa.ui.landing.fragments.Verify
import com.androidstrike.cofepa.utils.toast
import kotlinx.android.synthetic.main.activity_landing.*

class Landing : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        setSupportActionBar(toolbar)

        replaceFragment(Home(), "Home")

        toggle = ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        nav_view.setNavigationItemSelectedListener {

            it.isChecked = true

            when(it.itemId){
                R.id.nav_home -> replaceFragment(Home(), it.title.toString())
                R.id.nav_payment -> replaceFragment(Payment(), it.title.toString())
                R.id.nav_verify -> replaceFragment(Verify(), it.title.toString())
            }
            true
        }


    }

    private fun replaceFragment(fragment: Fragment, title: String){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container,fragment)
        fragmentTransaction.commit()
        drawer_layout.closeDrawers()
        setTitle(title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }


}