package com.kyudong.netflixandroid.splash

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.kyudong.netflixandroid.NetflixApp
import com.kyudong.netflixandroid.login.LoginActivity
import com.kyudong.netflixandroid.R
import com.kyudong.netflixandroid.home.HomeActivity

class SplashActivity : AppCompatActivity() {

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 // 3 second

    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            if (NetflixApp.prefs.userToken == "") {
                Toast.makeText(applicationContext, "로그인 화면으로 이동", Toast.LENGTH_SHORT).show()
                Log.e("pref", "login")
                val intent = Intent(application, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(applicationContext, "홈 화면으로 이동", Toast.LENGTH_SHORT).show()
                Log.e("pref", "home")
                val intent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        mDelayHandler = Handler()

        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
    }

    public override fun onDestroy() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)

            super.onDestroy()
        }

    }
}
