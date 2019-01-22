package com.kyudong.netflixandroid.signup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kyudong.netflixandroid.R
import com.kyudong.netflixandroid.adapter.ViewPagerAdapter
import com.kyudong.netflixandroid.home.HomeActivity
import kotlinx.android.synthetic.main.activity_sign_up_last.*

class SignUpLastActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_last)

        // 닉네임 데이터 받음 //
        if (intent.hasExtra("nickname")) {
            text_sign_up_nickname.text = intent.extras.getString("nickname")
        }

        // 뷰페이저 설정 //
        val adapter = ViewPagerAdapter(supportFragmentManager)
        sign_up_vpPager.adapter = adapter
        sign_up_tb.setupWithViewPager(sign_up_vpPager)

        // 시작 버튼 누를 시 전환 //
        btn_last_start_sign_up.setOnClickListener {
            val intentHome = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intentHome)
        }
    }
}
