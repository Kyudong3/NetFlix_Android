package com.kyudong.netflixandroid.signup.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kyudong.netflixandroid.R

/**
 * Created by Kyudong on 2019. 1. 21..
 */
class VpagerFragmentOne: Fragment() {

    companion object {
        fun newInstance() : VpagerFragmentOne {
            val fragmentOne = VpagerFragmentOne()
            val args = Bundle()
            fragmentOne.arguments = args
            return fragmentOne
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up_vp_first, container, false)
    }
}