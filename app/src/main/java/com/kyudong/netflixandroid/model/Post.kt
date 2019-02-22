package com.kyudong.netflixandroid.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.util.*

/**
 * Created by Kyudong on 2019. 1. 23..
 */
data class Post(@SerializedName("id") var id : Int, @SerializedName("postName") var postName : String, @SerializedName("content") var content : String,
                @SerializedName("period") var period : Int, @SerializedName("recruitNumber") var number : Int, @SerializedName("fee") var fee : Int,
                @SerializedName("membership") var membership : String, @SerializedName("createDate") var time : String, @SerializedName("hits") var hits : Int,
                @SerializedName("account") var account: Account, @SerializedName("comment") var comment : List<Comment>, @SerializedName("img") var img : List<Image>,
                @SerializedName("open") var open : Boolean) {

    // id
//    @SerializedName("id")
//    var id : Int? = null
//
//    // 게시글 이름
//    @SerializedName("postName")
//    var postName : String? = null
//
//    // 내용
//    @SerializedName("content")
//    var content : String? = null
//
//    // 모집 기간
//    @SerializedName("period")
//    var period : Int? = null
//
//    // 모집 인원 수
//    @SerializedName("recruitNumber")
//    var number : Int? = null
//
//    // 요금
//    @SerializedName("fee")
//    var fee : Int? = null
//
//    // 멤버쉽
//    @SerializedName("membership")
//    var membership : String? = null
//
//    // 생성 날짜
//    @SerializedName("createDate")
//    var createDate : String? = null
//
//    // 조회수
//    @SerializedName("hits")
//    var hits : Int? = null
//
//    // 계정
//    @SerializedName("account")
//    var account : Account? = null
//
//    // 댓글
//    @SerializedName("comment")
//    var comment : List<Comment>? = null
//
//    // img
//    @SerializedName("img")
//    var img : List<Image>? = null
//
//    // open
//    @SerializedName("open")
//    var open : Boolean? = null
}