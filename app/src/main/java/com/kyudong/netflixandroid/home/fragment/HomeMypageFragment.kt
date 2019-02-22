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
class HomeMypageFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() : HomeMypageFragment {
            val frag = HomeMypageFragment()

            frag.retainInstance = true

            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_home_mypage, container, false)
    }
}