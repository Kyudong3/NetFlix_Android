package com.kyudong.netflixandroid.signup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kyudong.netflixandroid.R
import kotlinx.android.synthetic.main.activity_sign_up_first.*

class SignUpFirstActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_first)

        btn_normal_sign_up.setOnClickListener {
            val intentToSecond = Intent(application, SignUpSecondActivity::class.java)
            startActivity(intentToSecond)
        }
    }
}
