package com.kyudong.netflixandroid.signup.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kyudong.netflixandroid.R

/**
 * Created by Kyudong on 2019. 1. 21..
 */
class VpagerFragmentSecond : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() : VpagerFragmentSecond {
            val fragmentSecond = VpagerFragmentSecond()
            val args = Bundle()
            fragmentSecond.arguments = args
            return fragmentSecond
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up_vp_second, container, false)
    }
}