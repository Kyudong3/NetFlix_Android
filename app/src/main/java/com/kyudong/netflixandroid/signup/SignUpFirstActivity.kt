package com.kyudong.netflixandroid.signup

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kyudong.netflixandroid.R
import kotlinx.android.synthetic.main.activity_sign_up_first.*
import android.opengl.ETC1.getHeight
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import com.kyudong.netflixandroid.KeyboardVisibilityUtils
import android.app.Activity
import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.view.View.OnTouchListener
import androidx.core.view.ViewCompat.setY
import androidx.core.view.ViewCompat.setX
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import com.kyudong.netflixandroid.KeyboardEvent
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import android.view.View.OnFocusChangeListener
import java.util.regex.Pattern
import android.widget.EditText
import com.kyudong.netflixandroid.login.LoginActivity
import com.kyudong.netflixandroid.model.EmailCheck
import com.kyudong.netflixandroid.network.ApiService
import com.kyudong.netflixandroid.network.RetrofitClientInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpFirstActivity : AppCompatActivity() {

    private var boolPwd: Boolean = true
    private var boolFormEmail: Boolean = false
    private var boolFormPwd: Boolean = false
    private var check: EmailCheck? = null
    private var emailCheck: EmailCheck = EmailCheck()

    companion object {
        var activity : SignUpFirstActivity? = null
    }

    override fun onBackPressed() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_first)

        activity = this

        /**    비밀번호 암호화 여부    **/
        imv_sign_up_pwd_correct.setOnClickListener {
            if (boolPwd) {
                imv_sign_up_pwd_correct.setImageResource(R.drawable.correct)
                edit_sign_up_pwd.inputType = 0x00000091
                boolPwd = false
            } else {
                imv_sign_up_pwd_correct.setImageResource(R.drawable.ncorrect)
                edit_sign_up_pwd.inputType = 0x00000081
                boolPwd = true
            }
        }

        /**      이메일 형식 체크      **/
        edit_sign_up_email.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                // code to execute when EditText loses focus
                edit_sign_up_email.setBackgroundResource(R.drawable.custom_sign_up_id)
                hideKeyboard(edit_sign_up_email)
                if (!TextUtils.isEmpty(edit_sign_up_email.text.toString().trim()) && !Patterns.EMAIL_ADDRESS.matcher(edit_sign_up_email.text.toString().trim()).matches()) {
                    //imv_sign_up_check.visibility = View.INVISIBLE
                    txv_sign_up_id_error.setText(R.string.sign_up_id_form_error)
                    txv_sign_up_id_error.visibility = View.VISIBLE
                    boolFormEmail = false
                } else {
                    txv_sign_up_id_error.visibility = View.INVISIBLE
                    boolFormEmail = true
                }
            } else {
                // focus 가 inside 이면
                edit_sign_up_email.setBackgroundResource(R.drawable.custom_sign_up_id_clicked)
            }
        }

        /**     비밀번호 형식 체크    **/
        edit_sign_up_pwd.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(edit_sign_up_pwd)
                // 비밀번호는 최소한 5 ~ 12자 정도 외어야 합니다 ( 기준 )
                edit_sign_up_pwd.setBackgroundResource(R.drawable.custom_sign_up_id)
                if (isValidPassword(edit_sign_up_pwd.text.toString().trim())) {
                    text_pwd_rule.setTextColor(ContextCompat.getColor(applicationContext, R.color.black_60))
                    boolFormPwd = true
                } else {
                    text_pwd_rule.setTextColor(ContextCompat.getColor(applicationContext, R.color.scarlet))
                    boolFormPwd = false
                }
            } else {
                edit_sign_up_pwd.setBackgroundResource(R.drawable.custom_sign_up_id_clicked)
            }
        }

        img_back_btn.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        /**     회원가입2 로 넘어가는 버튼 이벤트     **/
        btn_normal_sign_up.setOnClickListener {
            val email = edit_sign_up_email.text.toString().trim()
            val pwd = edit_sign_up_pwd.text.toString().trim()

            if (!isValidPassword(edit_sign_up_pwd.text.toString().trim())
                    || (TextUtils.isEmpty(edit_sign_up_email.text.toString().trim())
                            && Patterns.EMAIL_ADDRESS.matcher(edit_sign_up_email.text.toString().trim()).matches())) {
                Toast.makeText(applicationContext, "아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show()
            } else if (!imv_sign_up_checkbox.isChecked) {
                Toast.makeText(applicationContext, "이용약관에 동의해주세요", Toast.LENGTH_SHORT).show()
            } else {
                val service = RetrofitClientInstance.retrofitInstance()?.create(ApiService::class.java)

                val call = service?.signUpCheck(email)
                call?.enqueue(object : Callback<EmailCheck> {
                    override fun onFailure(call: Call<EmailCheck>?, t: Throwable?) {
                        Log.e("net", "Network Error!!")
                        t?.printStackTrace()
                    }

                    override fun onResponse(call: Call<EmailCheck>?, response: Response<EmailCheck>?) {
                        if (response?.isSuccessful!!) {
                            check = response.body()
                            val available = check?.emailCheck
                            Log.e("check", " : + $available")

                            if (!available!!) {
                                edit_sign_up_email.setBackgroundResource(R.drawable.custom_sign_up_id_errored)
                                txv_sign_up_id_error.visibility = View.VISIBLE
                                txv_sign_up_id_error.setText(R.string.sign_up_id_error)
                                txv_sign_up_id_error.setTextColor(ContextCompat.getColor(applicationContext, R.color.scarlet))
                            } else {
                                Log.e("success", "Email Available")
                                val intentToSecond = Intent(application, SignUpSecondActivity::class.java)
                                intentToSecond.putExtra("email", email)
                                intentToSecond.putExtra("password", pwd)
                                startActivity(intentToSecond)
                                //finish()
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

    // password validation check
    private fun isValidPassword(password: String): Boolean {
        val PASSWORD_STRING = "^(?=.*[0-9])(?=.*[a-z]|[A-Z])(?=\\S+$).{6,}$"

        val pattern = Pattern.compile(PASSWORD_STRING)
        val matcher = pattern.matcher(password)

        return matcher.matches()
    }

    /************    라이프 사이클     *************/

    override fun onDestroy() {
        Log.e("Lifecycle", "onDestroy")
        super.onDestroy()
    }

    override fun onStop() {
        Log.e("Lifecycle", "onStop")
        super.onStop()
    }

    override fun onPause() {
        Log.e("Lifecycle", "onPause")
        super.onPause()
    }

    override fun onResume() {
        Log.e("Lifecycle", "onResume")
        edit_sign_up_pwd.text?.clear()
        edit_sign_up_email.text.clear()
        imv_sign_up_checkbox.isChecked = false
        super.onResume()
    }

    override fun onRestart() {
        Log.e("Lifecycle", "onRestart")
        super.onRestart()
    }
}
