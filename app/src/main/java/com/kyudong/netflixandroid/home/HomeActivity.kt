package com.kyudong.netflixandroid.home

import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.kyudong.netflixandroid.NetflixApp
import com.kyudong.netflixandroid.R
import com.kyudong.netflixandroid.R.id.menu_alarm
import com.kyudong.netflixandroid.home.fragment.HomeAlarmFragment
import com.kyudong.netflixandroid.home.fragment.HomeFragment
import com.kyudong.netflixandroid.home.fragment.HomeMypageFragment
import com.kyudong.netflixandroid.model.Account
import com.kyudong.netflixandroid.network.ApiService
import com.kyudong.netflixandroid.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar_home.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import com.facebook.login.LoginManager



class HomeActivity : AppCompatActivity() {

    private lateinit var token: String

    private val mOnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.menu_alarm -> {
                    Toast.makeText(applicationContext, "menu_alarm Clicked", Toast.LENGTH_LONG).show()
                    val frag = HomeAlarmFragment()
                    addFragment(frag)
                    return true
                }

                R.id.menu_home -> {
                    Toast.makeText(applicationContext, "menu_home Clicked", Toast.LENGTH_LONG).show()
                    val frag = HomeFragment()
                    addFragment(frag)
                    return true
                }

                R.id.menu_mypage -> {
                    Toast.makeText(applicationContext, "menu_mypage Clicked", Toast.LENGTH_LONG).show()
                    val frag = HomeMypageFragment()
                    addFragment(frag)
                    return true
                }
            }
            return false
        }

    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.layout_frame_home, fragment)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        NetflixApp.prefs.mRefresh = ""

        Log.e("token", NetflixApp.prefs.userToken)
        //NetflixApp.prefs.userToken = ""

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.layout_frame_home, HomeFragment.newInstance(), "home")
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

