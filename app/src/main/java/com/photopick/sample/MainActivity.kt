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
    private var IMAGE_FOLDER = Environment.getExternalStorageDirectory().absolutePath + File.separator + "Sample"//文件夹的名字最好跟file_paths中设置的path保持一致
    private val REQUEST_CODE = 0x000111

    private lateinit var mAdapter: ImageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PhotoPick.config()//一定要在功能使用前调用，只需要调用一次，最好在Application或启动页中调用
            .imageLoader(PicassoImageLoader())//使用Picasso加载图片
            .configProvider(BuildConfig.APPLICATION_ID + ".provider")

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
                                    .savePath(IMAGE_FOLDER)// 图片存放路径
                                    .spanCount(4)// 每行显示个数
                                    .maxPickNumber(9)// 最大选择数量
                                    .enableAnimation(true)// 选择界面图片点击效果
                                    .enableCamera(true)// 是否开启拍照
                                    .pickedMediaList(mAdapter.mList)// 已选图片数据
                                    //如果是在Activity里使用就传Activity，如果是在Fragment里使用就传Fragment
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