package com.kyudong.netflixandroid.signup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.kyudong.netflixandroid.KeyboardVisibilityUtils
import com.kyudong.netflixandroid.R
import com.kyudong.netflixandroid.model.Account
import com.kyudong.netflixandroid.network.ApiService
import com.kyudong.netflixandroid.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_sign_up_second.*
import kotlinx.android.synthetic.main.toolbar_sign_up.*
import org.jetbrains.anko.textColor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpSecondActivity : AppCompatActivity() {

    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils
    private var email: String? = null
    private var pwd: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_second)

        if (intent.hasExtra("email")) {
            email = intent.getStringExtra("email")
            pwd = intent.getStringExtra("password")
        }

        Log.e("email", email)
        Log.e("pwd", pwd)

        // 스크롤뷰 edittext 에서 화면 올라가고 내려가고
        keyboardVisibilityUtils = KeyboardVisibilityUtils(window,
                onShowKeyboard = {
                    sv_sign_up_second.run {
                        smoothScrollTo(scrollX, scrollY + 300)
                    }
                }, onHideKeyboard = {
        })

        /**     닉네임 변할 때 다음 버튼 활성화      **/
        edit_sign_up_nickname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length != 0) {
                    btn_second_next_sign_up.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.rouge))
                } else {
                    btn_second_next_sign_up.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.brownish_grey))
                }
            }
        })

        /**     닉네임 레이아웃 클릭시 색 변화     **/
        edit_sign_up_nickname.setOnFocusChangeListener { view, b ->
            if (!b) {
                hideKeyboard(edit_sign_up_nickname)
                if (edit_sign_up_nickname.text.toString().trim().length > 7) {
                    txv_sign_up_nickname.setText(R.string.sign_up_nickname_length_error)
                    txv_sign_up_nickname.setTextColor(ContextCompat.getColor(applicationContext, R.color.scarlet))
                }
            } else {
                edit_sign_up_nickname.setBackgroundResource(R.drawable.custom_sign_up_id_clicked)
                txv_sign_up_nickname.setText(R.string.sign_up_nickname_info)
            }
        }

        img_back_btn_second.setOnClickListener {
            finish()
        }

        // 회원가입 3번째 페이지로 넘어가는 버튼
        btn_second_next_sign_up.setOnClickListener {
            val nickname = edit_sign_up_nickname.text.toString().trim()

            if (TextUtils.isEmpty(nickname)) {
                Toast.makeText(applicationContext, "닉네임을 입력해주세요!", Toast.LENGTH_SHORT).show()
                edit_sign_up_nickname.setBackgroundResource(R.drawable.custom_sign_up_id_errored)
            } else if (nickname.length > 7) {
                Toast.makeText(applicationContext, "닉네임은 7글자 이하로 작성해주세요!", Toast.LENGTH_SHORT).show()
                edit_sign_up_nickname.setBackgroundResource(R.drawable.custom_sign_up_id_errored)
                txv_sign_up_nickname.setText(R.string.sign_up_nickname_length_error)
                txv_sign_up_nickname.setTextColor(ContextCompat.getColor(applicationContext, R.color.scarlet))
            } else {
                val service = RetrofitClientInstance.retrofitInstance()?.create(ApiService::class.java)

                val input = HashMap<String, Any>()
                input["nickName"] = nickname
                input["email"] = email.toString().trim()
                input["password"] = pwd.toString().trim()

                val call = service?.signUp(input)
                call?.enqueue(object : Callback<Account> {
                    override fun onFailure(call: Call<Account>?, t: Throwable?) {
                        Toast.makeText(applicationContext, "Network Error!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<Account>?, response: Response<Account>?) {
                        if (response!!.isSuccessful) {
                            val intentNext = Intent(application, SignUpLastActivity::class.java)
                            intentNext.putExtra("email", email.toString().trim())
                            intentNext.putExtra("password", pwd.toString().trim())
                            startActivity(intentNext)
                            if (SignUpFirstActivity.activity != null) {
                                val acti = SignUpFirstActivity.activity
                                acti?.finish()
                            }
                            finish()
                        } else {
                            when (response.code()) {
                                400 -> {
                                    edit_sign_up_nickname.setBackgroundResource(R.drawable.custom_sign_up_id_errored)
                                    txv_sign_up_nickname.setText(R.string.sign_up_nickname_error)
                                    txv_sign_up_nickname.setTextColor(ContextCompat.getColor(applicationContext, R.color.scarlet))
                                }
                            }
                        }
                    }

                })
            }
        }
    }

    // Hide keyboard //
    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        Log.e("hide", "hide")
    }

//    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
//        if (event.action == MotionEvent.ACTION_UP) {
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
}
