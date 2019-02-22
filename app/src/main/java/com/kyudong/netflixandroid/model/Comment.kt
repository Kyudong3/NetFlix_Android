package com.kyudong.netflixandroid.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Kyudong on 2019. 1. 25..
 */
class Comment {

    // id
    @SerializedName("id")
    var id : Int? = null

    // 답글에 대한 정보를 나타내는 변수
    @SerializedName("parentCommentId")
    var parentCommentId : Int? = null

    // 댓글 내용
    @SerializedName("content")
    var content : String? = null

    // 생성 날짜
    @SerializedName("createDate")
    var createDate : String? = null

    // 비밀 댓글 여부
    @SerializedName("secret")
    var secret : Boolean? = null

    @SerializedName("account")
    var account : Account? = null
}