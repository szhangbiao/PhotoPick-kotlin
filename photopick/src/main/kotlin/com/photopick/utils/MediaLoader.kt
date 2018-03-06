package com.photopick.utils

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.FragmentActivity
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.text.TextUtils
import com.photopick.bean.FolderBean
import com.photopick.bean.PhotoBean
import com.zhangbiao.photopick.R
import java.io.File
import java.util.ArrayList
import java.util.Collections

/**
 * Created by szhangbiao on 2018/3/6.
 */
class MediaLoader(private val activity: FragmentActivity, mediaFilterSize: Int) {
    private var type: Int = 1
    private var mediaFilterSize: Int = 0

    init {
        this.mediaFilterSize = mediaFilterSize * 1000
    }

    fun loadAllMedia(mediaLoadListener: IMediaLoadListener) {
        activity.supportLoaderManager.initLoader(type, null,
            object : LoaderManager.LoaderCallbacks<Cursor> {
                override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
                    val sizeCondition = if (mediaFilterSize > 0) " AND " + SIZE + "<" + mediaFilterSize.toString() else ""
                    return CursorLoader(
                        activity,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        IMAGE_SELECTION + sizeCondition, IMAGE_SELECTION_ARGS,
                        MediaStore.Files.FileColumns.DATE_ADDED + " DESC")
                }

                override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
                    if (data == null) {
                        return
                    }
                    try {
                        val imageFolders = ArrayList<FolderBean>()
                        val allImageFolder = FolderBean("", "", "", ArrayList())
                        val latelyImages = ArrayList<PhotoBean>()
                        val count = data.count
                        if (count > 0) {
                            data.moveToFirst()
                            do {
                                val path = data.getString(
                                    data.getColumnIndexOrThrow(ALL_PROJECTION[1]))
                                // 如原图路径不存在或者路径存在但文件不存在,就结束当前循环
                                if (TextUtils.isEmpty(path) || !File(path).exists()) {
                                    continue
                                }
                                val name = data.getString(
                                    data.getColumnIndexOrThrow(ALL_PROJECTION[2]))
                                val addTime: Long = data.getLong(
                                    data.getColumnIndexOrThrow(ALL_PROJECTION[3]))
                                val image = PhotoBean(path, name, addTime)

                                val folder = getImageFolder(path, imageFolders)
                                val images = folder.photoList

                                images.add(image)
                                latelyImages.add(image)
                            } while (data.moveToNext())

                            if (latelyImages.size > 0) {
                                sortFolder(imageFolders)
                                imageFolders.add(0, allImageFolder)
                                allImageFolder.firstImagePath = latelyImages[0].photoPath
                                val title = activity.getString(R.string.photo_pick_camera_roll)
                                allImageFolder.folderName = title
                                allImageFolder.photoList = latelyImages
                            }
                            mediaLoadListener.loadComplete(imageFolders)
                        } else {
                            // 如果没有相册
                            mediaLoadListener.loadComplete(imageFolders)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onLoaderReset(loader: Loader<Cursor>?) {
                }
            })
    }

    private fun sortFolder(imageFolders: List<FolderBean>) {
        // 文件夹按图片数量排序
        Collections.sort(imageFolders, Comparator<FolderBean> { lhs, rhs ->
            if (lhs.photoList == null || rhs.photoList == null) {
                return@Comparator 0
            }
            val lsize = lhs.photoList.size
            val rsize = rhs.photoList.size
            if (lsize == rsize) 0 else if (lsize < rsize) 1 else -1
        })
    }

    private fun getImageFolder(path: String, imageFolders: MutableList<FolderBean>): FolderBean {
        val imageFile = File(path)
        val folderFile = imageFile.parentFile

        for (folder in imageFolders) {
            if (folder.folderName == folderFile.name) {
                return folder
            }
        }
        val newFolder = FolderBean(folderFile.name, folderFile.absolutePath, path, ArrayList())
        imageFolders.add(newFolder)
        return newFolder
    }


    interface IMediaLoadListener {
        fun loadComplete(folders: MutableList<FolderBean>)
    }

    companion object {

        private val ALL_QUERY_URI = MediaStore.Files.getContentUri("external")
        private val DURATION = "duration"
        private val SIZE = "_size"
        private val LATITUDE = "latitude"
        private val LONGITUDE = "longitude"

        /**
         * 全部媒体数据 - SELECTION_ARGS
         */
        private val ALL_SELECTION_ARGS = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString())

        /**
         * 全部媒体数据 - PROJECTION
         */
        private val ALL_PROJECTION = arrayOf(MediaStore.Images.Media._ID,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.DATE_ADDED,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.SIZE,
            LATITUDE,
            LONGITUDE,
            DURATION)

        /**
         * 全部媒体数据 - SELECTION
         */
        private val ALL_SELECTION = (
            MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO
                + " AND "
                + MediaStore.Files.FileColumns.SIZE + ">0"
                + " AND "
                + DURATION + ">0")


        /**
         * 图片 - PROJECTION
         */
        private val IMAGE_PROJECTION = arrayOf(MediaStore.Images.Media._ID,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.DATE_ADDED,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.SIZE,
            LATITUDE,
            LONGITUDE)

        /**
         * 图片 - SELECTION
         */
        private val IMAGE_SELECTION = (
            MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=?"
                + " or "
                + MediaStore.Images.Media.MIME_TYPE + "=?"
            )

        /**
         * 图片 - SELECTION_ARGS
         */
        private val IMAGE_SELECTION_ARGS = arrayOf("image/jpeg", "image/png", "image/webp")

        /**
         * 视频 - PROJECTION
         */
        private val VIDEO_PROJECTION = arrayOf(MediaStore.Images.Media._ID,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.DATE_ADDED,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.SIZE,
            LATITUDE,
            LONGITUDE,
            DURATION)


        /**
         * 获取全部图片和视频，但过滤掉gif图片
         */
        private val SELECTION_NOT_GIF = (
            MediaStore.Images.Media.MIME_TYPE + "=?"
                + " OR "
                + MediaStore.Images.Media.MIME_TYPE + "=?"
                + " OR "
                + MediaStore.Images.Media.MIME_TYPE + "=?"
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                + " AND "
                + MediaStore.MediaColumns.SIZE + ">0"
            )

        private val SELECTION_NOT_GIF_ARGS = arrayOf("image/jpeg", "image/png", "image/webp",
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString())

        private val ORDER_BY = MediaStore.Files.FileColumns._ID + " DESC"
    }
}