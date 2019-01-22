package com.kyudong.netflixandroid.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kyudong.netflixandroid.R
import com.kyudong.netflixandroid.signup.SignUpFirstActivity
import kotlinx.android.synthetic.main.activity_login.*
import android.view.WindowManager



class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //첫 번째 방법
        /*btn_normal_login.setOnClickListener(this)
        btn_social_login1.setOnClickListener(this)
        btn_social_login2.setOnClickListener(this)
        text_find_id_pwd.setOnClickListener(this)
        text_sign_up.setOnClickListener(this)
        */

        //두 번째 방법
        text_sign_up.setOnClickListener {
            val intentSignUp = Intent(application, SignUpFirstActivity::class.java)
            startActivity(intentSignUp)
        }
    }
}
