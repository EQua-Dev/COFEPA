package com.androidstrike.cofepa.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.androidstrike.cofepa.ui.verify.SubVerify
import com.androidstrike.cofepa.ui.verify.VerifyHistory

class MyPagerAdapter(
    var context: FragmentActivity?,
    fm: FragmentManager,
    var totalTabs: Int): FragmentPagerAdapter(fm)
{
    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 ->{
                VerifyHistory()
            }
            1 ->{
                SubVerify()
            }else -> getItem(position)
        }
    }


}