package com.kyudong.netflixandroid.network

import com.kyudong.netflixandroid.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Kyudong on 31/01/2019.
 */
interface ApiService {

    /** 로그인 **/
    @FormUrlEncoded
    @POST("/auth/signIn")
    fun signIn(@FieldMap param : HashMap<String, Any>) : Call<Token>

    /** 페이스북 로그인 **/
    @FormUrlEncoded
    @POST("/oAuth/signIn/facebook")
    fun facebookSignIn(@Field("token") token : String) : Call<Token>

    /** 회원가입 이메일 체크 **/
    @FormUrlEncoded
    @POST("/auth/check")
    fun signUpCheck(@Field("email") email : String) : Call<EmailCheck>

    /** 회원가입 **/
    @FormUrlEncoded
    @POST("/auth/signUp")
    fun signUp(@FieldMap parameters : HashMap<String, Any>) : Call<Account>

    /** 계정 정보 가져오기 **/
    @GET("/accounts/{id}")
    fun getAccount(@Header("Authorization") token : String, @Path("id") id : String) : Call<Account>

    /** 홈 게시글 **/
    @GET("/posts/list")
    fun postHome(@Header("Authorization") token : String) : Call<List<Post>>

    /** 글 상세 내용 **/
    @GET("/posts/{postId}")
    fun postDetail(@Header("Authorization") token : String, @Path("postId") id : Int, @Query("accountId") accountId : String) : Call<Post>

    /** 글 작성 **/
    @Multipart
    @POST("/posts")
    fun writePost(@Header("Authorization") token : String,  @Part("membership") membership : RequestBody, @Part("postName") name : RequestBody, @Part("content") content : RequestBody,
                  @Part("period") period : RequestBody, @Part("recruitNumber") recruit : RequestBody, @Part("fee") fee : RequestBody, @Part("accountId") id : RequestBody,
                  @Part file : MultipartBody.Part?) : Call<Unit>

    /** 댓글 요청하기 **/
    @FormUrlEncoded
    @POST("/comments")
    fun postComment(@Header("Authorization") token : String, @FieldMap param : HashMap<String, Any>) : Call<Unit>

    /** **/


    /** 마이페이지 **/


    /** 타인프로필 **/

}