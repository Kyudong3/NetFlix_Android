package com.kyudong.netflixandroid.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import android.util.Log
import android.widget.Toast
import com.kyudong.netflixandroid.NetflixApp
import com.kyudong.netflixandroid.R
import com.kyudong.netflixandroid.adapter.ViewPagerAdapter
import com.kyudong.netflixandroid.home.HomeActivity
import com.kyudong.netflixandroid.login.LoginActivity
import com.kyudong.netflixandroid.model.Account
import com.kyudong.netflixandroid.model.Token
import com.kyudong.netflixandroid.network.ApiService
import com.kyudong.netflixandroid.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_sign_up_last.*
import kotlinx.android.synthetic.main.toolbar_sign_up.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpLastActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var pwd: String
    private var token : Token? = null

    override fun onBackPressed() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_last)

        if (intent.hasExtra("email")) {
            email = intent.getStringExtra("email")
            pwd = intent.getStringExtra("password")
        }

        val service = RetrofitClientInstance.retrofitInstance()?.create(ApiService::class.java)

        val input = HashMap<String, Any>()
        input["email"] = email.trim()
        input["password"] = pwd.trim()

        /**     뷰페이저 설정   **/
        val adapter = ViewPagerAdapter(supportFragmentManager)
        sign_up_vpPager.adapter = adapter
        sign_up_tb.setupWithViewPager(sign_up_vpPager)

        sign_up_vpPager.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        btn_last_start_sign_up.isEnabled = false
                        Log.e("page", " : + $position")
                    }
                    1 -> {
                        btn_last_start_sign_up.isEnabled = false
                        Log.e("page", " : + $position")
                    }
                    else -> {
                        btn_last_start_sign_up.isEnabled = true
                        Log.e("page", " : + $position")

                    }
                }
            }

        })

        img_back_btn_second.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        /**     Skip 버튼 누를시 뷰페이저 스킵하고 시작   **/
        txv_sign_up_last_skip.setOnClickListener {
            val call = service?.signIn(input)
            call?.enqueue(object : Callback<Token> {
                override fun onFailure(call: Call<Token>?, t: Throwable?) {
                    Toast.makeText(applicationContext, "Network Error!!", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Token>?, response: Response<Token>?) {
                    // 만약 이메일이 존재한다면 중복된 이메일이 있습니다 표시
                    // 존재하지 않는다면 로그인 성공 후 화면 전환
                    if (response!!.isSuccessful) { // Success
                        token = response.body()
                        Log.e("token", token?.accessToken.toString())
                        NetflixApp.prefs.userToken = token?.accessToken.toString()
                        Toast.makeText(applicationContext, "홈 성공!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        intent.putExtra("token", token?.accessToken.toString())
                        startActivity(intent)
                        finish()
                    } else { // Fail
                        when (response.code()) {
                            401 -> {
                                Log.e("fail", "Unauthorized")
                            }
                            else -> {
                                throw IllegalStateException("${response.code()}, ${response.body()}")
                            }
                        }
                    }
                }
            })
        }

        /**     시작 버튼은 뷰페이저 다 넘겨야 활성화     **/
        btn_last_start_sign_up.setOnClickListener {
            val call = service?.signIn(input)
            call?.enqueue(object : Callback<Token> {
                override fun onFailure(call: Call<Token>?, t: Throwable?) {
                    Toast.makeText(applicationContext, "Network Error!!", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Token>?, response: Response<Token>?) {
                    // 만약 이메일이 존재한다면 중복된 이메일이 있습니다 표시
                    // 존재하지 않는다면 로그인 성공 후 화면 전환
                    if (response!!.isSuccessful) { // Success
                        token = response.body()
                        Log.e("token", token?.accessToken.toString())
                        NetflixApp.prefs.userToken = token?.accessToken.toString()
                        NetflixApp.prefs.userId = token?.accountId.toString()
                        Toast.makeText(applicationContext, "홈 성공!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        intent.putExtra("token", token?.accessToken.toString())
                        startActivity(intent)
                        finish()
                    } else { // Fail
                        when (response.code()) {
                            401 -> {
                                Log.e("fail", "Unauthorized")
                            }
                            else -> {
                                throw IllegalStateException("${response.code()}, ${response.body()}")
                            }
                        }
                    }
                }
            })
        }
    }
}
