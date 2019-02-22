package com.kyudong.netflixandroid

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Window
import android.widget.RadioGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.dialog_report.*
import org.jetbrains.anko.textColor

/**
 * Created by Kyudong on 12/02/2019.
 */
class ReportDialog(context: Context?) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(false)
        //window.setDimAmount(0.0f)
        //window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        window.requestFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_report)

        /** 다이얼로그 취소 버튼 **/
        txv_dialog_cancel_btn.setOnClickListener {
            dismiss()
        }

        /** 다이얼로그 신고하기 버튼 **/
        txv_dialog_report_btn.setOnClickListener {
            // 신고가 접수 되었다는 다이얼로그 생성
            Log.e("button clicked", "Button Clicked!!")
            if (rb_1.isChecked || rb_2.isChecked || rb_3.isChecked || rb_4.isChecked || rb_5.isChecked) {
                /** **/
                dismiss()
                val builder = AlertDialog.Builder(context)
                // Display a message on alert dialog
                builder.setMessage("신고가 접수되었습니다")

                // Set a positive button and its click listener on alert dialog
                builder.setPositiveButton("확인"){dialog, which ->
                    // Do something when user press the positive button
                }
            } else {
                Log.e("report", "Select at least 1")
            }
        }

        /** 라디오 버튼 바뀌었을 때 **/
        rg_report.setOnCheckedChangeListener({ radioGroup, i ->
            Log.e("radio", "Selected radio button is  " + i)
            if (rb_1.isChecked || rb_2.isChecked || rb_3.isChecked || rb_4.isChecked || rb_5.isChecked) {
                txv_dialog_report_btn.textColor = ContextCompat.getColor(context, R.color.rouge)
            } else {

            }
        })
    }

    class Builder(context: Context) {
        private val dialog = ReportDialog(context)

        fun dismiss(): ReportDialog {
            dialog.dismiss()
            return dialog
        }

        fun show(): ReportDialog {
            dialog.show()
            return dialog
        }
    }
}