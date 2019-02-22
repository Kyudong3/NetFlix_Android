package com.kyudong.netflixandroid.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import com.kyudong.netflixandroid.R
import com.kyudong.netflixandroid.signup.SignUpFirstActivity
import kotlinx.android.synthetic.main.activity_login.*
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.kyudong.netflixandroid.KeyboardVisibilityUtils
import com.kyudong.netflixandroid.NetflixApp
import com.kyudong.netflixandroid.home.HomeActivity
import com.kyudong.netflixandroid.network.ApiService
import com.kyudong.netflixandroid.network.RetrofitClientInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.kyudong.netflixandroid.model.Token
import java.util.*


class LoginActivity : AppCompatActivity() {

    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils
    private var token: Token? = null
    private lateinit var loginId: Button
    private lateinit var editPwd: EditText
    private lateinit var editId: EditText
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val service = RetrofitClientInstance.retrofitInstance()?.create(ApiService::class.java)

        /** 페이스북 로그아웃 **/
        LoginManager.getInstance().logOut()

        callbackManager = CallbackManager.Factory.create()

        loginId = findViewById(R.id.btn_normal_login)
        editPwd = findViewById(R.id.edit_user_pwd)
        editId = findViewById(R.id.edit_user_id)

        /**     스크롤뷰 edittext 에서 화면 올라가고 내려가고   **/
        keyboardVisibilityUtils = KeyboardVisibilityUtils(window,
                onShowKeyboard = { keyboardHeight ->
                    sv_login.run {
                        smoothScrollTo(scrollX, scrollY + 300)
                    }
                }, onHideKeyboard = {
        })

        /**     email focus 시 로그인 버튼 색 바뀌기       **/
        edit_user_id.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length != 0) {
                } else {
                    btn_normal_login.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.brownish_grey))
                    btn_normal_login.isClickable = false
                }
            }

        })

        /**     비밀번호 focus 시 로그인 버튼 색 바뀌기       **/
        edit_user_pwd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (TextUtils.isEmpty(edit_user_id.text.toString().trim()) || p0?.length == 0) {
                    btn_normal_login.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.brownish_grey))
                    btn_normal_login.isClickable = false
                } else {
                    btn_normal_login.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.rouge))
                    btn_normal_login.isClickable = true
                }
            }
        })

        /**     아이디/비밀번호 찾기 Click       **/
        text_find_id_pwd.setOnClickListener {
            Toast.makeText(applicationContext, "기능을 준비중입니다", Toast.LENGTH_SHORT).show()
        }

        /**     Sign Up click       **/
        text_sign_up.setOnClickListener {
            val intentSignUp = Intent(application, SignUpFirstActivity::class.java)
            startActivity(intentSignUp)
            finish()
        }

        /**     Login btn click     **/
        btn_normal_login.setOnClickListener {
            val loginId = edit_user_id.text.toString().trim()
            val loginPwd = edit_user_pwd.text.toString().trim()

            if (TextUtils.isEmpty(loginId)) {
                Toast.makeText(applicationContext, "아이디를 입력하세요", Toast.LENGTH_SHORT).show()
            } else if (!TextUtils.isEmpty(loginId) && !Patterns.EMAIL_ADDRESS.matcher(loginId).matches()) {
                Toast.makeText(applicationContext, "이메일 형식이 아닙니다", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(loginPwd)) {
                Toast.makeText(applicationContext, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
            } else {

                val input = HashMap<String, Any>()
                input["email"] = loginId
                input["password"] = loginPwd
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
                            Toast.makeText(applicationContext, "홈 성공!!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(applicationContext, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else { // Fail
                            when (response.code()) {
                                401 -> {
                                    Log.e("fail", "Unauthorized")
                                    Toast.makeText(applicationContext, "아이디와 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                                    input_layout_login_1.error = "잘못된 이메일입니다"
                                    input_layout_login_2.error = "잘못된 패스워드입니다"
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

        imv_social_login.setOnClickListener {
            img_facebook_login.performClick()
        }

        img_facebook_login.setReadPermissions("email")
        img_facebook_login.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Log.e("login", "success")
                Log.e("token", result?.accessToken?.token)
                val call = service?.facebookSignIn(result?.accessToken?.token!!)
                call?.enqueue(object : Callback<Token> {
                    override fun onFailure(call: Call<Token>?, t: Throwable?) {
                        Log.e("Network", "network error")
                        t?.printStackTrace()
                    }

                    override fun onResponse(call: Call<Token>?, response: Response<Token>?) {
                        if (response?.isSuccessful!!) {
                            token = response.body()
                            Log.e("token", token?.accessToken.toString())
                            NetflixApp.prefs.userToken = token?.accessToken.toString()
                            NetflixApp.prefs.userId = token?.accountId.toString()
                            Toast.makeText(applicationContext, "페이스북 로그인 성공!!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(applicationContext, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            when (response.code()) {
                                500 -> {
                                    Log.e("Network", "Internal Server Error")
                                }
                                else -> {
                                    Log.e("Network", "zxcvzcx")
                                }
                            }
                        }
                    }

                })

            }

            override fun onCancel() {
                Log.e("login", "cancel")
            }

            override fun onError(error: FacebookException?) {
                Log.e("login", "error")
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

    }


//    /**     EditText Focus 없애기      **/
//    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
//        if (event.action == MotionEvent.ACTION_DOWN) {
//            val v = currentFocus
//            if (v is EditText) {
//                val outRect = Rect()
//                v.getGlobalVisibleRect(outRect)
//                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
//                    v.clearFocus()
//                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.hideSoftInputFromWindow(v.windowToken, 0)
//                    Log.e("hide", "hide")
//                }
//            }
//        }
//        return super.dispatchTouchEvent(event)
//    }


    /************    라이프 사이클     *************/

//    override fun onDestroy() {
//        keyboardVisibilityUtils.detachKeyboardListeners()
//        Log.e("Lifecycle", "onDestroy")
//        super.onDestroy()
//    }
//
//    override fun onStop() {
//        Log.e("Lifecycle", "onStop")
//        super.onStop()
//    }
//
//    override fun onPause() {
//        Log.e("Lifecycle", "onPause")
//        super.onPause()
//    }
//
//    override fun onResume() {
//        Log.e("Lifecycle", "onResume")
//        super.onResume()
//    }
//
//    override fun onRestart() {
//        Log.e("Lifecycle", "onRestart")
//        super.onRestart()
//    }
}
