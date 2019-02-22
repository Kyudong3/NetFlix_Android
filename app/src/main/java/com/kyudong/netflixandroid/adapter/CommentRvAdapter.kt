package com.kyudong.netflixandroid.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kyudong.netflixandroid.R
import com.kyudong.netflixandroid.home.comment.CommentActivity
import com.kyudong.netflixandroid.model.Comment
import kotlinx.android.synthetic.main.rv_detail_comment.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Kyudong on 11/02/2019.
 */
class CommentRvAdapter(private val context: Context?, private val commentList: List<Comment>) : RecyclerView.Adapter<CommentRvAdapter.ViewHolder>() {

    private var month: Long = 0
    private var day: Long = 0
    private var hour: Long = 0
    private var minute: Long = 0
    private var subtract: Long = 0

    interface itemClick {
        fun onClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_detail_comment, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.content?.text = commentList[position].content
        holder.nickname?.text = commentList[position].account?.nickname
        holder.replyCnt?.text = "답글 0개"

        holder.replyCnt?.setOnClickListener {
            val intent = Intent(context, CommentActivity::class.java)
            context?.startActivity(intent)
        }

        calculateTime(holder, position)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    private fun calculateTime(holder: ViewHolder, position: Int) {
        val sDF = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        sDF.timeZone = TimeZone.getTimeZone("GMT")
        val date = Date()
        val parseSDF = sDF.parse(commentList[position].createDate)
        subtract = date.time - parseSDF.time
        month = subtract / 1000 / 60 / 60 / 24 / 30
        day = subtract / 1000 / 60 / 60 / 24
        hour = subtract / 1000 / 60 / 60
        minute = subtract / 1000 / 60

        if (month == 0L) {
            if (day != 0L) {
                if (day == 1L) {
                    holder.time?.text = "어제"
                } else {
                    holder.time?.text = day.toString() + "일 전"
                }
            } else {
                if (hour != 0L) {
                    holder.time?.text = hour.toString() + "시간 전"
                } else {
                    holder.time?.text = minute.toString() + "분 전"
                }
            }
        } else if (minute <= 0) {
            holder.time?.text = "방금"
        } else {
            holder.time?.text = month.toString() + "달 전"
        }
    }

    class ViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
        var profile = view?.imv_comment_profile
        var nickname = view?.txv_comment_nickname
        var content = view?.txv_comment_content
        var time = view?.txv_comment_time
        var replyCnt = view?.txv_comment_reply_cnt
    }
}

