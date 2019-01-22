package com.kyudong.netflixandroid.signup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kyudong.netflixandroid.R
import kotlinx.android.synthetic.main.activity_sign_up_second.*

class SignUpSecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_second)

        btn_second_next_sign_up.setOnClickListener {
            if (edit_sign_up_nickname.text.toString().trim().isEmpty()) {
                Toast.makeText(applicationContext, "닉네임을 입력해주세요!", Toast.LENGTH_LONG).show()
            } else {
                val intentNext = Intent(application, SignUpLastActivity::class.java)
                intentNext.putExtra("nickname", edit_sign_up_nickname.text.toString())
                startActivity(intentNext)
            }
        }
    }
}
