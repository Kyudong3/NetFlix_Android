package com.kyudong.netflixandroid.splash

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.kyudong.netflixandroid.login.LoginActivity
import com.kyudong.netflixandroid.R

class SplashActivity : AppCompatActivity() {

    //private lateinit var handler: Handler

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 // 3 second

    // Splash 1번째 방법 //
    internal val mRunnable2: Runnable = Runnable {
        if (!isFinishing) {
            val intent = Intent(application, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mDelayHandler = Handler()

        mDelayHandler!!.postDelayed(mRunnable2, SPLASH_DELAY)

        // Splash 2번째 방법
        /*
        Handler().postDelayed({
            val intent2 = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
        */
    }

    public override fun onDestroy() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable2)

            super.onDestroy()
        }

    }
}
