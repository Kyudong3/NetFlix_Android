package com.kyudong.netflixandroid.home.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.kyudong.netflixandroid.NetflixApp
import com.kyudong.netflixandroid.R
import com.kyudong.netflixandroid.adapter.HomeRvAdapter
import com.kyudong.netflixandroid.home.post.PostDetail
import com.kyudong.netflixandroid.home.write.PostWrite
import com.kyudong.netflixandroid.model.Post
import com.kyudong.netflixandroid.network.ApiService
import com.kyudong.netflixandroid.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.fragment_home_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Kyudong on 2019. 1. 23..
 */
class HomeFragment : androidx.fragment.app.Fragment() {

    private val service = RetrofitClientInstance.retrofitInstance().create(ApiService::class.java)
    private var adapter2: HomeRvAdapter? = null

    companion object {
        fun newInstance(): HomeFragment {
            val homeFrag = HomeFragment()

            homeFrag.retainInstance = true

            return homeFrag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.e("Lifecycle", "onCreateView")
        return inflater.inflate(R.layout.fragment_home_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("Lifecycle", "onViewCreated")


        /** 작성 버튼 클릭 시 **/
        home_fab.setOnClickListener {
            val intent = Intent(context, PostWrite::class.java)
            startActivity(intent)
        }

        refresh_layout.setOnRefreshListener {
            refreshRv()
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.e("Lifecycle", "onActivityCreated")

        loadRecyclerViewData()

    }

    /**     새로고침     **/
    fun refreshRv() {
        Toast.makeText(context, "good", Toast.LENGTH_SHORT).show()
        loadRecyclerViewData()
        refresh_layout.isRefreshing = false
    }

    /**     아이템 클릭 이벤트     **/
    private fun clickListenerRecyclerView(list: List<Post>) {
        adapter2?.itemClick = object : HomeRvAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                Log.e("item clicked", "$position")
                val intent = Intent(context, PostDetail::class.java)
                Log.e("item id", "${list[position - 1].id}")
                intent.putExtra("id", list[position - 1].id)
                startActivity(intent)
            }
        }
    }

    /**     게시글 데이터 통신     **/
    private fun loadRecyclerViewData() {
        val call = service.postHome("Bearer " + NetflixApp.prefs.userToken)
        Log.e("token", " : ${NetflixApp.prefs.userToken}")

        call.enqueue(object : Callback<List<Post>> {
            override fun onFailure(call: Call<List<Post>>?, t: Throwable?) {
                Log.e("error", "error!!!")

                t?.printStackTrace()
            }

            override fun onResponse(call: Call<List<Post>>?, response: Response<List<Post>>?) {

                if (response!!.isSuccessful) {

                    if (rv_home_frag.adapter == null) {
                        initRecyclerView(response.body()!!)
                    } else {
                        adapter2!!.postList = response.body()!!
                        adapter2!!.notifyDataSetChanged()
                    }
                    clickListenerRecyclerView(response.body()!!)


                } else {
                    when (response.body()) {

                    }
                }


            }

        })
    }

    /** 리사이클러뷰 세팅 **/
    fun initRecyclerView(list: List<Post>) {
        rv_home_frag.setHasFixedSize(true)
        rv_home_frag.layoutManager = LinearLayoutManager(context)
        adapter2 = HomeRvAdapter(list)
        rv_home_frag.adapter = adapter2

    }


}