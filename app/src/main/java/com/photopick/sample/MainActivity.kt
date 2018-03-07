package com.photopick.sample

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.photopick.PhotoPick
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.io.File

class MainActivity : AppCompatActivity() {
    private var IMAGE_FOLDER = Environment.getExternalStorageDirectory().absolutePath + File.separator + "Sample"
    private val REQUEST_CODE = 0x000111

    private lateinit var mAdapter: ImageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PhotoPick.config().imageLoader(PicassoImageLoader()).configProvider(
            BuildConfig.APPLICATION_ID + ".provider")

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this@MainActivity, 4,
            GridLayoutManager.VERTICAL, false)
        mAdapter = ImageAdapter(this)
        recyclerView.adapter = mAdapter
        mAdapter.setOnAddMediaListener(object : ImageAdapter.OnAddMediaListener {
            override fun onaddMedia() {
                RxPermissions(this@MainActivity).request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA)
                    .subscribe(object : Observer<Boolean> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(aBoolean: Boolean) {
                            if (aBoolean) {
                                PhotoPick.with()
                                    .savePath(IMAGE_FOLDER)
                                    .spanCount(4)
                                    .maxPickNumber(9)
                                    .enableAnimation(true)
                                    .enableCamera(true)
                                    .pickedMediaList(mAdapter.mList)
                                    .start(this@MainActivity, 0, REQUEST_CODE)
                            } else {
                                showToast("读取内存卡权限被拒绝，请到设置里开启权限")
//                                closeActivity()
                            }
                        }

                        override fun onError(e: Throwable) {
                            showToast("读取内存卡权限被拒绝，请到设置里开启权限")
                        }

                        override fun onComplete() {

                        }
                    })
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            //返回的数据
            val result = PhotoPick.result(data)
            if (result != null) {
                mAdapter.updateDataList(result)
            }
        }
    }
}

fun MainActivity.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}