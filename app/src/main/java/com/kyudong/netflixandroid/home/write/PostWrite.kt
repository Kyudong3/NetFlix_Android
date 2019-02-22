package com.kyudong.netflixandroid.home.write

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.widget.EditText
import com.kyudong.netflixandroid.R
import kotlinx.android.synthetic.main.activity_post_write.*
import org.jetbrains.anko.textColor
import android.R.attr.data
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.CursorLoader
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.MediaStore
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.SyncStateContract
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.kyudong.netflixandroid.NetflixApp
import com.kyudong.netflixandroid.model.Account
import com.kyudong.netflixandroid.network.ApiService
import com.kyudong.netflixandroid.network.RetrofitClientInstance
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream


class PostWrite : AppCompatActivity() {

    private var mSelectedFirst: Boolean = false
    private var title: String? = null
    private var month: String? = null
    private var fee: String? = null
    private var content: String? = null
    private var file: File? = null
    private var uri: Uri? = null
    private lateinit var imageView: ImageView
    private var membership : String = "프리미엄"
    private var recruitMember : Int = 0

    private val REQ_CODE_SELECT_IMAGE: Int = 100

    private var uploadfile2 : MultipartBody.Part? = null
    private var file3 : RequestBody? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_write)

        NetflixApp.prefs.mRefresh = "retain"

        imageView = findViewById(R.id.imv_write_photo)

        Log.e("select", " : " + !mSelectedFirst)
        btn_write_pay_system_pre.isSelected = true

        /** 작성(체크) 표시 클릭시 **/
        imv_toolbar_post_checked.setOnClickListener {
            getInfoState()
            checkInfoState()
            postToServer()
        }

        /** 작성(버튼) 클릭시 **/
        btn_write_write.setOnClickListener {
            getInfoState()
            checkInfoState()
            postToServer()
        }

        /** 명수 이미지 버튼 클릭시 **/
        imv_write_person_1.setOnClickListener {
            imv_write_person_1.isSelected = true
            imv_write_person_2.isSelected = false
            imv_write_person_3.isSelected = false
            txv_remain_person.setText(R.string.write_person_left_1)
            recruitMember = 1
        }

        imv_write_person_2.setOnClickListener {
            imv_write_person_1.isSelected = true
            imv_write_person_2.isSelected = true
            imv_write_person_3.isSelected = false
            txv_remain_person.text = resources.getString(R.string.write_person_left_2)
            recruitMember = 2
        }

        imv_write_person_3.setOnClickListener {
            imv_write_person_1.isSelected = true
            imv_write_person_2.isSelected = true
            imv_write_person_3.isSelected = true
            txv_remain_person.text = resources.getString(R.string.write_person_left_3)
            recruitMember = 3
        }

        /** **/
        btn_write_pay_system_basic.setOnClickListener {
            btn_write_pay_system_basic.isSelected = true
            btn_write_pay_system_std.isSelected = false
            btn_write_pay_system_pre.isSelected = false
            membership = "베이직"
            txv_write_pay_system_info.setText(R.string.write_post_pay_system_basic)
        }

        btn_write_pay_system_std.setOnClickListener {
            btn_write_pay_system_basic.isSelected = false
            btn_write_pay_system_std.isSelected = true
            btn_write_pay_system_pre.isSelected = false
            membership = "스탠다드"
            txv_write_pay_system_info.setText(R.string.write_post_pay_system_std)
        }

        btn_write_pay_system_pre.setOnClickListener {
            btn_write_pay_system_basic.isSelected = false
            btn_write_pay_system_std.isSelected = false
            btn_write_pay_system_pre.isSelected = true
            membership = "프리미엄"
            txv_write_pay_system_info.setText(R.string.write_post_pay_system_premium)
        }

        /** 사진 첨부하기 **/
        btn_write_attach.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "select Picture"), REQ_CODE_SELECT_IMAGE)
        }

        /** 사진 삭제하기 **/
        imv_write_remove_photo.setOnClickListener {
            cl_write_photo.visibility = View.GONE
            txv_write_attach.visibility = View.VISIBLE
            btn_write_attach.text = resources.getText(R.string.picture_attach)
        }

        /** 백 버튼 클릭 시 **/
        imv_toolbar_post_back.setOnClickListener {
            finish()
        }
    }

    /** 갤러리에서 사진 선택완료 후 **/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_CODE_SELECT_IMAGE) {

                val mInput: InputStream = contentResolver.openInputStream(data?.data)
                val img: Bitmap = BitmapFactory.decodeStream(mInput)
                Glide.with(applicationContext).load(data?.data).into(imageView)
                if (data != null) {
                    Log.e("content", contentResolver.getType(data.data))
                }

                //imv_write_photo.setImageBitmap(img)
                cl_write_photo.visibility = View.VISIBLE
                txv_write_attach.visibility = View.GONE
                btn_write_attach.text = resources.getText(R.string.write_post_adjust)

                Log.e("okay", "okayokay")
                uri = data?.data
                Log.e("uri", " : $uri")

                val filePath: String = checkPermission(this, uri)
                //pathdd(applicationContext, uri)
                Log.e("file", filePath)
                file = File(filePath)
            }
        }
    }

    /** 작성 정보 상태 가져오기 **/
    private fun getInfoState() {
        title = edit_write_title.text.toString().trim()
        month = edit_write_month.text.toString().trim()
        fee = edit_write_fee.text.toString().trim()
        content = edit_write_post_content.text.toString().trim()
    }

    /** 작성 정보 체크하기 **/
    private fun checkInfoState() {
        if (title?.length != 0) {
            txv_write_title.textColor = ContextCompat.getColor(applicationContext, R.color.black_87)
        } else {
            txv_write_title.textColor = ContextCompat.getColor(applicationContext, R.color.scarlet)
        }

        if (month?.length != 0) {
            txv_write_post_month.textColor = ContextCompat.getColor(applicationContext, R.color.black_87)
        } else {
            txv_write_post_month.textColor = ContextCompat.getColor(applicationContext, R.color.scarlet)
        }

        if (fee?.length != 0) {
            txv_write_post_fee.textColor = ContextCompat.getColor(applicationContext, R.color.black_87)
        } else {
            txv_write_post_fee.textColor = ContextCompat.getColor(applicationContext, R.color.scarlet)
        }

        if (content?.length != 0) {
            txv_write_post_content.textColor = ContextCompat.getColor(applicationContext, R.color.black_87)
        } else {
            txv_write_post_content.textColor = ContextCompat.getColor(applicationContext, R.color.scarlet)
        }

        if (txv_remain_person.text != resources.getText(R.string.write_person_left_0)) {
            txv_write_person.setTextColor(ContextCompat.getColor(applicationContext, R.color.black_87))
        } else {
            txv_write_person.setTextColor(ContextCompat.getColor(applicationContext, R.color.scarlet))
        }
    }

    private fun checkPermission(context: Context, uri: Uri?): String {
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            Log.e("err", "error from permisson")
            return ""
        } else {
            return pathdd(context, uri)
        }
    }

    private fun pathdd(context: Context, uri: Uri?): String {
        var filePath = ""
        if (uri?.host!!.contains("com.android.providers.media")) {
            val wholeId = DocumentsContract.getDocumentId(uri)
            val id = wholeId.split(":")[1]
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val sel = MediaStore.Images.Media._ID + "=?"
            val cursor: Cursor = context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection, sel, arrayOf(id), null)
            val columnIndex = cursor.getColumnIndex(projection[0])

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex)
            }
            cursor.close()
            Log.e("filePath", "dasd + ${filePath}")
            return filePath

        } else {

            return "ddd"
        }
    }

    /** 글 작성 Api 호출 **/
    private fun postToServer() {
        if (title?.length != 0 && month?.length != 0 && fee?.length != 0 && content?.length != 0
                && txv_remain_person.text != resources.getText(R.string.write_person_left_0)) {

            val token = NetflixApp.prefs.userToken
            val postName = RequestBody.create(MediaType.parse("text/plain"), title)
            val content = RequestBody.create(MediaType.parse("text/plain"), content)
            val period = RequestBody.create(MediaType.parse("text/plain"), month)
            val fee = RequestBody.create(MediaType.parse("text/plain"), fee)
            val id = RequestBody.create(MediaType.parse("text/plain"), NetflixApp.prefs.userId)
            //val email = RequestBody.create(MediaType.parse("text/plain"), "kyudong3@naver.com")
            val recruitNumber = RequestBody.create(MediaType.parse("text/plain"), recruitMember.toString())
            val membership = RequestBody.create(MediaType.parse("text/plain"), membership)

            if (file != null) {
                Log.e("file", "file not null")
                file3 = RequestBody.create(MediaType.parse("image/*"), file)
                uploadfile2 = MultipartBody.Part.createFormData("files", file?.name, file3)
            } else {
                Log.e("file", "file null")
                file3 = null
                uploadfile2 = null
            }

            val service = RetrofitClientInstance.retrofitInstance()?.create(ApiService::class.java)

            val call = service?.writePost("Bearer $token", membership, postName, content, period, recruitNumber, fee, id, uploadfile2)
            call?.enqueue(object : Callback<Unit> {
                override fun onFailure(call: Call<Unit>?, t: Throwable?) {
                    Log.e("write failure", t.toString())
                }

                override fun onResponse(call: Call<Unit>?, response: Response<Unit>?) {

                    if (response!!.isSuccessful) {
                        Log.e("success", "hello")
                        NetflixApp.prefs.mRefresh = ""
                        finish()
                    } else {
                        Log.e("success", "failed")
                    }
                }
            })

        }
    }
}
