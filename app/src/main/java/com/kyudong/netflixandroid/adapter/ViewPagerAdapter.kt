package com.kyudong.netflixandroid.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import com.kyudong.netflixandroid.signup.fragment.VpagerFragmentOne
import com.kyudong.netflixandroid.signup.fragment.VpagerFragmentSecond
import com.kyudong.netflixandroid.signup.fragment.VpagerFragmentThird

/**
 * Created by Kyudong on 2019. 1. 21..
 */
class ViewPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val fragment = when(position) {
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