package com.kyudong.netflixandroid.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import android.util.Log
import android.view.View
import com.kyudong.netflixandroid.signup.fragment.VpagerFragmentOne
import com.kyudong.netflixandroid.signup.fragment.VpagerFragmentSecond
import com.kyudong.netflixandroid.signup.fragment.VpagerFragmentThird

/**
 * Created by Kyudong on 2019. 1. 21..
 */
class ViewPagerAdapter(fm: androidx.fragment.app.FragmentManager?) : androidx.fragment.app.FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        val fragment = when(position) {
            0 -> VpagerFragmentOne.newInstance()
            1 -> VpagerFragmentSecond.newInstance()
            2 -> VpagerFragmentThird.newInstance()
            else -> VpagerFragmentOne.newInstance()
        }

        return fragment
    }

    override fun getCount(): Int {
        return 3
    }
}