package com.kyudong.netflixandroid.home.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kyudong.netflixandroid.R

/**
 * Created by Kyudong on 2019. 1. 23..
 */
class HomeMypageFragment : Fragment() {

    /*companion object {
        fun newInstance() : HomeMypageFragment {
            val frag = HomeMypageFragment()
            val args = Bundle()
            frag.arguments = args
            return frag
        }
    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_home_mypage, container, false)
    }
}