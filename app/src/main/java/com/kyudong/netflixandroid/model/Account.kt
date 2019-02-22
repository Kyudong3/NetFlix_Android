package com.kyudong.netflixandroid.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Kyudong on 2019. 1. 25..
 */
class Account {

    // id
    @SerializedName("id")
    val id : Int? = null

    // 아이디(이메일 형식)
    @SerializedName("email")
    val email : String? = null

    // 비밀번호
    @SerializedName("password")
    val password : String? = null

    // 닉네임
    @SerializedName("nickName")
    val nickname : String? = null

    // providerId
    @SerializedName("providerId")
    val providerId : String? = null

    @SerializedName("providerType")
    val providerType : String? = null

    // 이미지 url
    val imageUrl : String? = null



}