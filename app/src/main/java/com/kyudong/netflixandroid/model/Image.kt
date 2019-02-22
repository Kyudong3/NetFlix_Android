package com.kyudong.netflixandroid.model

import com.google.gson.annotations.SerializedName
import java.net.URI

/**
 * Created by Kyudong on 16/02/2019.
 */
class Image {

    @SerializedName("id")
    var id : Int? = null

    @SerializedName("imgName")
    var imgName : String? = null

    @SerializedName("imgUrl")
    var imgUrl : String? = null
}