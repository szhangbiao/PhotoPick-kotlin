package com.photopick.config
import android.app.Activity
import android.os.Environment
import android.support.v4.app.Fragment
import com.photopick.bean.PhotoBean
import com.photopick.utils.ReflectUtils
import java.io.Serializable

/**
 * Created by szhangbiao on 2018/3/5.
 */
class PickOption : Serializable {
    // 是否开启多选  默认 ： false
    var enableMultiSelect: Boolean = true
    //最大选择张数，默认为0，表示不限制
    var maxPickNumber = 0
    //是否显示拍照按钮
    var enableCamera = false
    //图片选择界面每行图片个数
    var spanCount = 4
    //显示多少kb以下的图片/视频，默认为0，表示不限制
    var mediaFilterSize: Int = 0
    //选择列表图片宽度
    var thumbnailWidth = 160
    //选择列表图片高度
    var thumbnailHeight = 160
    //选择列表点击动画效果
    var enableAnimation = true
    //拍照、视频的保存地址
    var savePath = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DCIM).absolutePath

    //已选择的数据、图片/视频/音频预览的数据
    var pickedMediaList: ArrayList<PhotoBean> = ArrayList()

    fun enableMultiPhoto(enableMulti:Boolean):PickOption{
        enableMultiSelect=enableMulti
        return this
    }

    fun enableCamera(enable: Boolean): PickOption {
        enableCamera = enable
        return this
    }

    fun maxPickNumber(max: Int): PickOption {
        maxPickNumber = max
        return this
    }

    fun spanCount(span: Int): PickOption {
        spanCount = span
        return this
    }

    fun thumbnailWidth(width: Int): PickOption {
        thumbnailWidth = width
        return this
    }

    fun thumbnailHeight(height: Int): PickOption {
        thumbnailHeight = height
        return this
    }

    fun enableAnimation(enable: Boolean): PickOption {
        enableAnimation = enable
        return this
    }

    fun pickedMediaList(photoList: ArrayList<PhotoBean>): PickOption {
        pickedMediaList = photoList
        return this
    }

    fun mediaFilterSize(mediaSize: Int): PickOption {
        mediaFilterSize = mediaSize
        return this
    }

    fun savePath(path: String): PickOption {
        savePath = path
        return this
    }

    fun start(fragment: Fragment, type: Int, requestCode: Int) {
        val starter = ReflectUtils.loadStarter(ReflectUtils.Picker)
        starter?.start(fragment, this, type, requestCode)
    }

    fun start(activity: Activity, type: Int, requestCode: Int) {
        val starter = ReflectUtils.loadStarter(ReflectUtils.Picker)
        starter?.start(activity, this, type, requestCode)
    }

}