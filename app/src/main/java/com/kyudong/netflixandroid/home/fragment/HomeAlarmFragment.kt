package com.kyudong.netflixandroid.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kyudong.netflixandroid.R

/**
 * Created by Kyudong on 2019. 1. 23..
 */
class HomeAlarmFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() : HomeAlarmFragment {
            val fragment = HomeAlarmFragment()
            fragment.retainInstance = true

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_home_alarm, container, false)

    }



}