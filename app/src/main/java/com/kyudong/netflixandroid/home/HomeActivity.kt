package com.kyudong.netflixandroid.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.MenuItem
import com.kyudong.netflixandroid.NetflixApp
import com.kyudong.netflixandroid.R
import com.kyudong.netflixandroid.home.fragment.HomeAlarmFragment
import com.kyudong.netflixandroid.home.fragment.HomeFragment
import com.kyudong.netflixandroid.home.fragment.HomeMypageFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar_home.*
import java.lang.IllegalStateException


class HomeActivity : AppCompatActivity() {

    private lateinit var token: String

    companion object {
        val HOME = "home"
        val ALARM = "alarm"
        val MYPAGE = "mypage"
    }

    var homeFragment: HomeFragment? = null
    var alarmFragment: HomeAlarmFragment? = null
    var mypageFragment: HomeMypageFragment? = null

    var current : String = HOME

    private val mOnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.menu_alarm -> {
                    if(alarmFragment == null) {
                        alarmFragment = HomeAlarmFragment.newInstance()
                    }
                    addFragment(alarmFragment, ALARM)
                    return true
                }

                R.id.menu_home -> {
                    if(homeFragment == null) {
                        homeFragment = HomeFragment.newInstance()
                    }
                    addFragment(homeFragment, HOME)
                    return true
                }

                R.id.menu_mypage -> {
                    if(mypageFragment == null) {
                        mypageFragment = HomeMypageFragment.newInstance()
                    }
                    addFragment(mypageFragment, MYPAGE)
                    return true
                }
            }
            return false
        }

    }

    private fun addFragment(fragment: Fragment?, tag: String) {


        val found = supportFragmentManager.findFragmentByTag(tag)

        val temp = when(current) {
            HOME -> homeFragment
            ALARM -> alarmFragment
            MYPAGE -> mypageFragment
            else -> throw IllegalStateException()
        }

        if(found == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.layout_frame_home, fragment!!, tag)
                    .hide(temp!!)
                    .commit()
        } else {
            if(!found.isVisible) {
                supportFragmentManager
                        .beginTransaction()
                        .show(found)
                        .hide(temp!!)
                        .commit()
            }
        }

        current = tag


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        NetflixApp.prefs.mRefresh = ""

        Log.e("token", NetflixApp.prefs.userToken)
        //NetflixApp.prefs.userToken = ""

        if (savedInstanceState == null) {

            homeFragment = HomeFragment.newInstance()

            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.layout_frame_home, homeFragment!!, HOME)
                    .commit()
        }

        imv_toolbar_home_back.setOnClickListener {
            Log.e("home back btn", "What activity will be shown?")
        }

        imv_home_search.setOnClickListener {
            NetflixApp.prefs.userToken = ""
        }

        home_bottom_nav.selectedItemId = R.id.menu_home
        home_bottom_nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

//        val service = RetrofitClientInstance.retrofitInstance()?.create(ApiService::class.java)
//        val call = service?.getAccount("Bearer " + NetflixApp.prefs.userToken, NetflixApp.prefs.userId)
//        call?.enqueue(object : Callback<Account> {
//            override fun onFailure(call: Call<Account>?, t: Throwable?) {
//                Log.e("Network", "Home Network Error")
//                t?.printStackTrace()
//            }
//
//            override fun onResponse(call: Call<Account>?, response: Response<Account>?) {
//                if (response!!.isSuccessful) {
//                    val body = response.body()
//
//                    NetflixApp.prefs.userId = body?.id.toString()
//                    NetflixApp.prefs.userEmail = body?.email.toString()
//                    NetflixApp.prefs.userNickname = body?.nickname.toString()
//
//                    /*
//                    NetflixApp.prefs.providerId
//                    NetflixApp.prefs.providerType
//                    */
//
//                    Log.e("prefs", body?.id.toString())
//                    Log.e("prefs", body?.email.toString())
//                    Log.e("prefs", body?.nickname.toString())
//
//                } else {
//                    when (response.code()) {
//                        404 -> {
//                            Log.e("404 Err", "Id Not Found")
//                        }
//                    }
//                }
//            }
//
//        })
    }
}

