package com.photopick.config
import android.app.Activity
import android.os.Environment
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.support.v4.app.Fragment
import com.photopick.base.BaseFragment
import com.photopick.bean.PhotoBean
import com.photopick.utils.ReflectUtils

/**
 * Created by szhangbiao on 2018/3/5.
 */
class PickOption() :Parcelable{
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
    var pickedMediaList: MutableList<PhotoBean> = mutableListOf()

    constructor(parcel: Parcel) : this() {
        enableMultiSelect = parcel.readByte() != 0.toByte()
        maxPickNumber = parcel.readInt()
        enableCamera = parcel.readByte() != 0.toByte()
        spanCount = parcel.readInt()
        mediaFilterSize = parcel.readInt()
        thumbnailWidth = parcel.readInt()
        thumbnailHeight = parcel.readInt()
        enableAnimation = parcel.readByte() != 0.toByte()
        savePath = parcel.readString()
    }


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

    fun pickedMediaList(photoList: MutableList<PhotoBean>): PickOption {
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

    fun start(fragment: BaseFragment, type: Int, requestCode: Int) {
        val starter = ReflectUtils.loadStarter(ReflectUtils.Picker)
        starter?.start(fragment, this, type, requestCode)
    }

    fun start(activity: Activity, type: Int, requestCode: Int) {
        val starter = ReflectUtils.loadStarter(ReflectUtils.Picker)
        starter?.start(activity, this, type, requestCode)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (enableMultiSelect) 1 else 0)
        parcel.writeInt(maxPickNumber)
        parcel.writeByte(if (enableCamera) 1 else 0)
        parcel.writeInt(spanCount)
        parcel.writeInt(mediaFilterSize)
        parcel.writeInt(thumbnailWidth)
        parcel.writeInt(thumbnailHeight)
        parcel.writeByte(if (enableAnimation) 1 else 0)
        parcel.writeString(savePath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<PickOption> {
        override fun createFromParcel(parcel: Parcel): PickOption {
            return PickOption(parcel)
        }

        override fun newArray(size: Int): Array<PickOption?> {
            return arrayOfNulls(size)
        }
    }
}