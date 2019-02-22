package com.kyudong.netflixandroid.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.kyudong.netflixandroid.R
import com.kyudong.netflixandroid.model.Post
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Kyudong on 2019. 1. 23..
 */
class HomeRvAdapter(private val context: Context?, private val postList: List<Post>, spinnerList: ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER: Int = 0
    private val TYPE_ITEM: Int = 1
    private var adapter = ArrayAdapter(context, R.layout.simple_spinner_item, spinnerList)
    var itemClick: ItemClick? = null
    private var month: Long = 0
    private var day: Long = 0
    private var hour: Long = 0
    private var minute: Long = 0
    private var subtract: Long = 0

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    override fun getItemViewType(position: Int): Int {
        when (position) {
            0 -> return TYPE_HEADER
            else -> return TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        return postList.size + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_HEADER) {
            val header = LayoutInflater.from(context).inflate(R.layout.rv_home_header, parent, false)
            return HeaderHolder(header)
        } else {
            val item = LayoutInflater.from(context).inflate(R.layout.rv_home_item, parent, false)
            return ItemHolder(item)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderHolder) {
            val h: HeaderHolder = holder
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            h.optSpinner?.adapter = adapter
            //h.optSpinner?.adapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, spinnerList)
        } else if (holder is ItemHolder) {
            val i: ItemHolder = holder
            i.itemView.setOnClickListener { view ->
                itemClick?.onClick(view, position)
            }

            i.nameTxv?.text = postList[position-1].postName
            i.periodTxv?.text = "${postList[position-1].period}개월"
            i.systemTxv?.text = postList[position-1].membership
            i.nicknameTxv?.text = postList[position-1].account?.nickname
            i.hitTxv?.text = postList[position-1].hits.toString()
            i.commentTxv?.text = postList[position-1].comment?.size.toString()
            i.numberTxv?.text = postList[position-1].number.toString() + " 명 남음"


            val sDF = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            sDF.timeZone = TimeZone.getTimeZone("GMT")

            val date = Date()

            val parsDF = sDF.parse(postList[position-1].time)
            subtract = date.time - parsDF.time
            month = subtract / 1000 / 60 / 60 / 24 / 30
            day = subtract / 1000 / 60 / 60 / 24
            hour = subtract / 1000 / 60 / 60
            minute = subtract / 1000 / 60

            if (month == 0L) {
                if (day != 0L) {
                    if (day == 1L) {
                        i.timeTxv?.text = "어제"
                    } else {
                        i.timeTxv?.text = day.toString() + "일 전"
                    }
                } else {
                    if (hour != 0L) {
                        i.timeTxv?.text = hour.toString() + "시간 전"
                    } else {
                        i.timeTxv?.text = minute.toString() + "분 전"
                    }
                }
            } else if (minute <= 0) {
                i.timeTxv?.text = "방금"
            } else {
                i.timeTxv?.text = month.toString() + "달 전"
            }

        }
    }

    class HeaderHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var optSpinner = itemView?.findViewById<Spinner>(R.id.spinner_home)
    }

    class ItemHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var clAbove = itemView?.findViewById<ConstraintLayout>(R.id.cl_rv_item_above)
        var clBelow = itemView?.findViewById<ConstraintLayout>(R.id.cl_rv_item_below)
        var periodTxv = itemView?.findViewById<TextView>(R.id.txv_item_period)
        var systemTxv = itemView?.findViewById<TextView>(R.id.txv_item_pay_system)
        var nameTxv = itemView?.findViewById<TextView>(R.id.txv_item_name)
        var nicknameTxv = itemView?.findViewById<TextView>(R.id.txv_item_nickname)
        var numberTxv = itemView?.findViewById<TextView>(R.id.txv_item_number)
        var hitTxv = itemView?.findViewById<TextView>(R.id.txv_item_hit_cnt)
        var commentTxv = itemView?.findViewById<TextView>(R.id.txv_item_comment_cnt)
        var timeTxv = itemView?.findViewById<TextView>(R.id.txv_item_time)
        var profileImv = itemView?.findViewById<ImageView>(R.id.img_item_profile)
    }
}