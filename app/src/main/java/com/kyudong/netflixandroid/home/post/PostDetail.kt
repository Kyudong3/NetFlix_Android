package com.kyudong.netflixandroid.home.post

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.bumptech.glide.Glide
import com.kyudong.netflixandroid.NetflixApp
import com.kyudong.netflixandroid.R
import com.kyudong.netflixandroid.ReportDialog
import com.kyudong.netflixandroid.adapter.CommentRvAdapter
import com.kyudong.netflixandroid.model.Comment
import com.kyudong.netflixandroid.model.Post
import com.kyudong.netflixandroid.network.ApiService
import com.kyudong.netflixandroid.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_post_detail.*
import kotlinx.android.synthetic.main.toolbar_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class PostDetail : AppCompatActivity() {

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_report -> {
                ReportDialog.Builder(this)
                        .show()
                true
            }
            R.id.menu_mijung -> {
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        val menuInflater: MenuInflater = menuInflater
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    private val llm = LinearLayoutManager(this)
    private lateinit var adapter: CommentRvAdapter
    private val service = RetrofitClientInstance.retrofitInstance()?.create(ApiService::class.java)
    private var id: Int = -1
    private var month: Long = 0
    private var day: Long = 0
    private var hour: Long = 0
    private var minute: Long = 0
    private var subtract: Long = 0
    private var selected: Boolean = false
    private var postName: String? = null
    private var content: String? = null
    private var postId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        NetflixApp.prefs.mRefresh = "retain"

        if (intent.hasExtra("id")) {
            id = intent.extras.getInt("id")
            Log.e("id", "detail is is $id")
        }

        val callDetail = service?.postDetail("Bearer " + NetflixApp.prefs.userToken, id, NetflixApp.prefs.userId)
        callDetail?.enqueue(object : Callback<Post> {
            override fun onFailure(call: Call<Post>?, t: Throwable?) {
                Log.e("network", "Network Error")
                t?.printStackTrace()
            }

            override fun onResponse(call: Call<Post>?, response: Response<Post>?) {
                if (response!!.isSuccessful) {
                    val post = response.body()

                    if (post?.img?.isEmpty()!!) {
                        Log.e("image", "Image is Empty!!!")
                    }

                    /**     이용자 뷰     **/
                    if (NetflixApp.prefs.userId != response.body()?.account?.id.toString()) {
                        Log.e("이용자뷰", "이용자뷰")
                        setDataInView(response.body())      // 이용자와 작성자 뷰가 원래 달라야 하지만 지금은 똑같이 해놓음
                    }
                    /**     작성자 뷰    **/
                    else {
                        Log.e("작성자뷰", "작성자뷰")
                        setDataInView(response.body())      // 이용자와 작성자 뷰가 원래 달라야 하지만 지금은 똑같이 해놓음
                    }

                    initCommentRecyclerView(response.body()!!)

                } else {
                    Log.e("response Error", "response Error")
                }
            }
        })

        /**     비밀 댓글 여부     **/
        imv_comment_lock.setOnClickListener {
            if (selected) {
                imv_comment_lock.isSelected = false
                selected = false
            } else {
                imv_comment_lock.isSelected = true
                selected = true
            }
        }

        /**     댓글 등록 버튼     **/
        txv_comment_register.setOnClickListener {
            val inputData = HashMap<String, Any>()
            inputData["content"] = edit_comment_detail.text.toString().trim()
            inputData["accountId"] = NetflixApp.prefs.userId
            inputData["secret"] = selected
            inputData["postId"] = postId.toString()

            hideKeyboard(edit_comment_detail)

            val callWriteComment = service?.postComment("Bearer " + NetflixApp.prefs.userToken, inputData)
            callWriteComment?.enqueue(object : Callback<Unit> {
                override fun onFailure(call: Call<Unit>?, t: Throwable?) {
                    Log.e("network", "Network error!")
                    t?.printStackTrace()
                }

                override fun onResponse(call: Call<Unit>?, response: Response<Unit>?) {
                    if (response?.isSuccessful!!) {
                        Log.e("comment", "Comment post success")
                    } else {
                        Log.e("comment", "comment receive error")
                    }
                }
            })
        }

        imv_toolbar_detail_back.setOnClickListener {
            finish()
        }
    }

    /** 키보드 숨기기 **/
    // Hide keyboard //
    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        Log.e("hide", "hide")
    }

    /**     댓글 리사이클러뷰 세팅     **/
    private fun initCommentRecyclerView(post: Post) {
        val commentList: List<Comment> = post.comment
        rv_detail_comment.layoutManager = llm
        rv_detail_comment.setHasFixedSize(true)
        adapter = CommentRvAdapter(applicationContext, commentList)
        rv_detail_comment.adapter = adapter
    }

    /**     상세 뷰에 데이터 세팅     **/
    private fun setDataInView(post: Post?) {
        postName = post?.postName
        content = post?.content
        postId = post?.id

        txv_name_detail.text = postName
        txv_recruit_detail.text = "${post?.number}명 남음"
        txv_nickname_detail.text = post?.account?.nickname
        txv_membership_detail.text = post?.membership
        txv_period_detail.text = "${post?.period}"
        txv_fee_detail.text = "${post?.fee}"
        txv_content_detail.text = post?.content

        if (post?.img?.isEmpty()!!) {
            cl_detail_second.visibility = View.GONE
            Log.e("image", "visibility gone")
        } else {
            Log.e("image", "visibility visible")
            cl_detail_second.visibility = View.VISIBLE
            Glide.with(applicationContext).load(post.img[0].imgUrl).into(imv_image_detail)
        }

        // 시간 계산
        calculateTime(post)
    }

    /**     작성 시간 계산      **/
    private fun calculateTime(post: Post?) {
        val sDF = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        sDF.timeZone = TimeZone.getTimeZone("GMT")
        val date = Date()
        val parseSDF = sDF.parse(post?.time)
        subtract = date.time - parseSDF.time
        month = subtract / 1000 / 60 / 60 / 24 / 30
        day = subtract / 1000 / 60 / 60 / 24
        hour = subtract / 1000 / 60 / 60
        minute = subtract / 1000 / 60

        if (month == 0L) {
            if (day != 0L) {
                if (day == 1L) {
                    txv_time_detail.text = "어제"
                } else {
                    txv_time_detail.text = day.toString() + "일 전"
                }
            } else {
                if (hour != 0L) {
                    txv_time_detail.text = hour.toString() + "시간 전"
                } else {
                    txv_time_detail.text = minute.toString() + "분 전"
                }
            }
        } else if (minute <= 0) {
            txv_time_detail.text = "방금"
        } else {
            txv_time_detail.text = month.toString() + "달 전"
        }
    }
}
