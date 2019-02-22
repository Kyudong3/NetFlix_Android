package com.kyudong.netflixandroid

import android.app.Application
import android.content.SharedPreferences
import com.kyudong.netflixandroid.Prefs.CustomSharedPrefs

/**
 * Created by Kyudong on 05/02/2019.
 */
class NetflixApp : Application() {

    companion object {
        lateinit var prefs: CustomSharedPrefs
    }

    override fun onCreate() {
        prefs = CustomSharedPrefs(applicationContext)
        super.onCreate()
    }
}