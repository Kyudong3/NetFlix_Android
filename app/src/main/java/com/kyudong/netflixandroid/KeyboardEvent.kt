package com.kyudong.netflixandroid

import android.app.Activity
import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sign_up_first.*
import android.widget.RelativeLayout



/**
 * Created by Kyudong on 2019. 1. 28..
 */
class KeyboardEvent(val mInputg : InputMethodManager, private val imm : InputMethodManager,
                    private val ve : View, private val vp : View, private val vc : View, private val rl : View) {

    // Keyboard event handling when touching views //
    fun event() {
        ve.setOnTouchListener({ view, ev ->
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    showSoftKeyboard(view)
                }
            }
            false
        })
        vp.setOnTouchListener({ view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    showSoftKeyboard(view)
                }
            }
            false
        })
        vc.setOnTouchListener({ view, motionEvent ->
            hideKeyboard(view)
            false
        })
    }

    // Hide keyboard //
    fun hideKeyboard(view: View) {
        //val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        mInputg.hideSoftInputFromWindow(view.windowToken, 0)
        Log.e("hide", "hide")
        val param = rl.layoutParams as ConstraintLayout.LayoutParams
        param.setMargins(param.leftMargin, 160, param.rightMargin, param.bottomMargin) // left, top, right, bottom
        rl.layoutParams = param
    }

    // Show keyboard //
    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            //val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            Log.e("show", "show")
            val parameter = rl.layoutParams as ConstraintLayout.LayoutParams
            parameter.setMargins(parameter.leftMargin, 50, parameter.rightMargin, parameter.bottomMargin) // left, top, right, bottom
            rl.layoutParams = parameter
        }
    }
}