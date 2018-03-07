package com.photopick.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.photopick.PhotoPick
import com.photopick.adapter.FolderAdapter
import com.photopick.adapter.PhotoAdapter
import com.photopick.base.BaseFragment
import com.photopick.bean.FolderBean
import com.photopick.bean.PhotoBean
import com.photopick.config.PickOption
import com.photopick.utils.CameraHelper
import com.photopick.utils.CameraHelper.ICameraListener
import com.photopick.utils.MediaLoader
import com.photopick.utils.ScreenUtil
import com.photopick.utils.StringUtils
import com.photopick.widget.FolderPopupWindow
import com.photopick.widget.GridSpacingItemDecoration
import com.zhangbiao.photopick.R
import java.util.ArrayList

/**
 * Created by szhangbiao on 2018/3/5.
 */
class PhotoPickFragment : BaseFragment(), View.OnClickListener, PhotoAdapter.OnPickChangedListener, FolderAdapter.OnItemClickListener {

    private lateinit var pickRlTitle: RelativeLayout
    private lateinit var pickTvBack: ImageView
    private lateinit var pickTvTitle:TextView
    private lateinit var pickTvCancel:TextView
    private lateinit var rvContainer:RecyclerView
    private lateinit var rlBottom:RelativeLayout
    private lateinit var tvPreview:TextView
    private lateinit var llComplete:LinearLayout
    private lateinit var tvComplete:TextView
    private lateinit var tvNumber:TextView

    private lateinit var tvEmpty:TextView
    /**
     * 选项
     */
    private lateinit var option: PickOption
    /**
     * 图片加载
     */
    private lateinit var mediaLoader: MediaLoader


    private var isAnimation = false
    private lateinit var folderWindow: FolderPopupWindow
    private var animation: Animation? = null

    private lateinit var photoAdapter:PhotoAdapter

    private var allMediaList: ArrayList<PhotoBean> = ArrayList()
    private var allFolderList: ArrayList<FolderBean> = ArrayList()
    /**
     * 相机打开帮助类
     */
    private var cameraHelper: CameraHelper? = null

    override fun onCreateView(): View {
        return LayoutInflater.from(getFragmentActivity()).inflate(R.layout.photo_pick_activity_picker,null)
    }

    override fun initWidgets(view: View) {
        option = if (arguments.getSerializable(
                PhotoPick.PICK_OPTION) == null) PickOption() else arguments.getSerializable(
            PhotoPick.PICK_OPTION) as PickOption
        initView(view)
        setupView()
        setupData()
    }

    private fun initView(view: View) {
        pickRlTitle=findView(view,R.id.pickRlTitle)
        pickTvBack=findView(view,R.id.pickTvBack)
        pickTvTitle=findView(view,R.id.pickTvTitle)
        pickTvCancel=findView(view,R.id.pickTvCancel)
        rvContainer=findView(view,R.id.rv_photo_container)
        rvContainer=findView(view,R.id.rv_photo_container)
        rlBottom=findView(view,R.id.rl_bottom)
        tvPreview=findView(view,R.id.tv_photo_preview)
        llComplete=findView(view,R.id.ll_photo_complete)
        tvComplete=findView(view,R.id.tv_photo_complete)
        tvNumber=findView(view,R.id.tv_photo_number)
        tvEmpty=findView(view,R.id.tv_photo_empty)
    }

    private fun setupView() {
        isNumberComplete()
        StringUtils.tempTextFont(tvEmpty)

        folderWindow = FolderPopupWindow(getFragmentActivity())
        folderWindow.setPictureTitleView(pickTvTitle)
        folderWindow.setOnItemClickListener(this)

        tvPreview.setOnClickListener(this)
        pickTvBack.setOnClickListener(this)
        pickTvCancel.setOnClickListener(this)
        pickTvTitle.setOnClickListener(this)
        llComplete.setOnClickListener(this)
    }

    private fun setupData() {
        rvContainer.setHasFixedSize(true)
        rvContainer.addItemDecoration(
            GridSpacingItemDecoration(option.spanCount,
                ScreenUtil.dip2px(getFragmentActivity(), 2f), false))
        rvContainer.layoutManager = GridLayoutManager(getFragmentActivity(), option.spanCount)
        (rvContainer.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        photoAdapter = PhotoAdapter(getFragmentActivity(), option)
        rvContainer.adapter = photoAdapter
        photoAdapter.setOnPickChangedListener(this)
        photoAdapter.setPickMediaList(option.pickedMediaList)
        changeImageNumber(option.pickedMediaList)

        mediaLoader = MediaLoader(getFragmentActivity(), option.mediaFilterSize)
        readLocalMedia()
    }

    private fun readLocalMedia() {
        mediaLoader.loadAllMedia(object : MediaLoader.IMediaLoadListener {
            override fun loadComplete(folders: ArrayList<FolderBean>) {
                if (folders.size > 0) {
                    allFolderList = folders
                    val folder = folders[0]
                    val localImg = folder.photoList
                    // 这里解决有些机型会出现拍照完，相册列表不及时刷新问题
                    // 因为onActivityResult里手动添加拍照后的照片，
                    // 如果查询出来的图片大于或等于当前adapter集合的图片则取更新后的，否则就取本地的
                    if (localImg.size >= allMediaList.size) {
                        allMediaList = localImg
                        folderWindow.bindFolder(folders)
                    }
                }
                if (photoAdapter != null) {
                    if (allMediaList == null) {
                        allMediaList = ArrayList<PhotoBean>()
                    }
                    photoAdapter.updateDataList(allMediaList)
                    tvEmpty.visibility = if (allMediaList.size > 0) View.INVISIBLE else View.VISIBLE
                }
                dismissLoadingDialog()
            }
        })
    }

    /**
     * none number style
     */
    @SuppressLint("StringFormatMatches")
    private fun isNumberComplete() {
        tvComplete.text = getString(R.string.photo_pick_please_select)
        animation = AnimationUtils.loadAnimation(getFragmentActivity(), R.anim.phoenix_window_in)
    }

    /**
     * change image selector state
     * @param selectImages
     */
    @SuppressLint("StringFormatMatches")
    private fun changeImageNumber(selectImages: List<PhotoBean>) {
        val enable = selectImages.isNotEmpty()
        if (enable) {
            llComplete.isEnabled = true
            llComplete.isClickable = true
            llComplete.alpha = 1F
            tvPreview.isEnabled = true
//            tvPreview.setTextColor(if (themeColor == THEME_DEFAULT) ContextCompat.getColor(mContext, R.color.green) else themeColor)
            if (!isAnimation) {
                tvNumber.startAnimation(animation)
            }
            tvNumber.visibility = View.VISIBLE
            tvNumber.text = "(${selectImages.size})"
            tvComplete.text = getString(R.string.photo_pick_completed)
            isAnimation = false
        } else {
            llComplete.isEnabled = false
            llComplete.isClickable = false
            llComplete.alpha = 0.7F
            tvPreview.isEnabled = false
            tvPreview.setTextColor(
                ContextCompat.getColor(getFragmentActivity(), R.color.color_gray_1))
            tvNumber.visibility = View.GONE
            tvComplete.text = getString(R.string.photo_pick_please_select)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        animation?.cancel()
//        ImagesObservable.instance.clearLocalMedia()
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.pickTvBack || id == R.id.pickTvCancel) {
            if (folderWindow.isShowing) {
                folderWindow.dismiss()
            } else {
                popBackStack()
            }
        }
        if (id == R.id.pickTvTitle) {
            if (folderWindow.isShowing) {
                folderWindow.dismiss()
            } else {
                if (allMediaList.size > 0) {
                    folderWindow.showAsDropDown(pickRlTitle)
//                    val selectedImages = photoAdapter.getPickMediaList()
//                    folderWindow.notifyDataCheckedStatus(selectedImages)
                }
            }
        }
        if (id == R.id.ll_photo_complete) {
            onResult(photoAdapter.getPickMediaList())
        }
    }


    override fun onItemClick(folderName: String, images: ArrayList<PhotoBean>) {
        pickTvTitle.text = folderName
        photoAdapter.updateDataList(images)
        folderWindow.dismiss()
    }

    override fun onTakePhoto() {
        if (cameraHelper == null) {
            cameraHelper = CameraHelper(activity, option, object : ICameraListener {
                override fun onFinish(photoBean: PhotoBean) {
                    val images = photoAdapter.getPickMediaList()
                    images.add(photoBean)
                    onResult(images)
                }
            })
        }
        cameraHelper?.takePhoto()
    }

    override fun onChange(selectImages: List<PhotoBean>) {
        changeImageNumber(selectImages)
    }

    /**
     * return image result
     * @param images images
     */
    private fun onResult(images: MutableList<PhotoBean>) {
        dismissLoadingDialog()
        val intent = Intent()
        val result = ArrayList<PhotoBean>(images.size)
        result.addAll(images)
        intent.putExtra(PhotoPick.PHOENIX_RESULT, result)
        getFragmentActivity().setResult(Activity.RESULT_OK, intent)
        popBackStack()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        cameraHelper?.onActivityForResult(requestCode, resultCode, data)
    }
}