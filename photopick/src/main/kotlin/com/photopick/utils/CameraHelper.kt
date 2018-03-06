package com.photopick.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.photopick.PhotoPick
import com.photopick.bean.PhotoBean
import com.photopick.config.PickOption
import java.io.File

/**
 * Created by szhangbiao on 2018/3/6.
 */
class CameraHelper(private val mActivity: Activity, pickOption: PickOption,
    cameraListener: ICameraListener?) {
    private val REQ_CAMERA = 100001

    private val imageFolder: String
    private var pickOption: PickOption = pickOption
    private var cameraListener: ICameraListener?
    private var imagePath: String

    init {
        this.cameraListener = cameraListener
        imageFolder = pickOption.savePath
        imagePath = generateImgePath()
    }

    /**
     * 拍照获取
     */
    fun takePhoto() {
        val imgFile = File(generateImgePath())
        if (!imgFile.getParentFile().exists()) {
            imgFile.getParentFile().mkdirs()
        }
        var imgUri: Uri? = null

        imgUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(mActivity, PhotoPick.config().getConfigProvider(), imgFile)
        } else {
            Uri.fromFile(imgFile)
        }

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)
        mActivity.startActivityForResult(intent, REQ_CAMERA)
    }

    fun onActivityForResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQ_CAMERA) {
            val photoBean = PhotoBean(imagePath, "", 0)
            cameraListener?.onFinish(photoBean)
        }
    }

    /**
     * 产生图片的路径，带文件夹和文件名，文件名为当前毫秒数
     */
    private fun generateImgePath(): String {
        return imageFolder + File.separator + System.currentTimeMillis().toString() + ".jpg"
    }

    interface ICameraListener {
        fun onFinish(photoBean: PhotoBean)
    }
}