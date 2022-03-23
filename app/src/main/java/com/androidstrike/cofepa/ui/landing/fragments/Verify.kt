package com.androidstrike.cofepa.ui.landing.fragments

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.androidstrike.cofepa.R
import com.androidstrike.cofepa.adapters.MyPagerAdapter
import com.androidstrike.cofepa.utils.Common
import com.androidstrike.cofepa.utils.toast
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.copied_message.view.*
import kotlinx.android.synthetic.main.custom_reference_dialog.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Verify : Fragment() {

    lateinit var fm: FragmentManager
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verify, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tabLayout = view?.findViewById(R.id.tab_title)!!
        viewPager = view?.findViewById(R.id.view_pager)!!

        tabLayout.addTab(tabLayout.newTab().setText("History"))
        tabLayout.addTab(tabLayout.newTab().setText("Verify"))

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = childFragmentManager?.let { MyPagerAdapter(activity, it,  tabLayout.tabCount) }
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
                tabLayout.setSelectedTabIndicatorColor(Color.WHITE)
                tabLayout.setTabTextColors(Color.BLACK, Color.WHITE)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
                tabLayout.setTabTextColors(Color.BLACK, Color.WHITE)
            }
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }
}