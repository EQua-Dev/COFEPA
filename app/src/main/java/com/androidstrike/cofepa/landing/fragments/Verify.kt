package com.androidstrike.cofepa.landing.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.androidstrike.cofepa.R
import com.androidstrike.cofepa.adapters.MyPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_verify.*

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

        tabLayout.addTab(tabLayout.newTab().setText("Verify"))
        tabLayout.addTab(tabLayout.newTab().setText("History"))

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = fragmentManager?.let { MyPagerAdapter(activity, it,  tabLayout.tabCount) }
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })



    }
}