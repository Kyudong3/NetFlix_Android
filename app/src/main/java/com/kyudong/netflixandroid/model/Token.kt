package com.kyudong.netflixandroid.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Kyudong on 16/02/2019.
 */
class Token {

    // Token
    @SerializedName("accessToken")
    val accessToken: String? = null

    // Token Type
    @SerializedName("tokenType")
    val tokenType: String? = null

    // Id
    @SerializedName("accountId")
    val accountId : Int? = null

}