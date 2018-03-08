# PhotoPick-kotlin图片选择器
PhotoPick是kotlin版 Android 自定义相册，实现了拍照、图片多选、ImageLoader无绑定 任由开发者选择
## 特点
* 全部代码由kotlin实现，体验kotlin语法的简洁高效的特性
* 调用的便利性，开启某个功能只需要调用enableXXX(true/false)方法
* ImageLoader可配置，定制项目需要的图片加载方案。
* 采用activity+fragments的UI架构，方面后续功能的拓展
* 沉浸式状态栏的实现，兼容到API 19
## 效果预览
## 快速添加
1.在项目的 `build.gradle` 中添加：
```java
allprojects {
    repositories {
	    ...
	    maven { url 'https://jitpack.io' }
    }
}
```
2.添加依赖
```java
dependencies {
    compile 'com.zhangbiao.photopick:photopick:1.0.0'
}
```
## 使用说明
请在`app`中的 `AndroidManifest` 中的`application`标签下添加
```xml
<provider
    android:name="android.support.v4.content.FileProvider"
    android:authorities="${applicationId}.provider"//如果一个设备中出现两个同样的authorities会出现无法安装的情况
    android:exported="false"
    android:grantUriPermissions="true"
    >
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths"
        />
</provider>
```
在`res`中创建`xml`文件夹，在其中创建文件。sample中定义了filepaths.xml。
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <paths>
        <!--代表外部存储区域的根目录下的文件 Environment.getExternalStorageDirectory()/Sample 目录-->
        <external-path
            name="sample_file"
            path="Sample"
            />
        <!--代表app 私有的存储区域 Context.getFilesDir()目录下的images目录 /data/user/0/com.hm.camerademo/files/Sample-->
        <files-path
            name="sample_private_files"
            path="Sample"
            />
        <!--代表app 私有的存储区域 Context.getCacheDir()目录下的images目录 /data/user/0/com.hm.camerademo/cache/Sample-->
        <cache-path
            name="sample_private_cache"
            path="Sample"
            />
        <!--代表app 外部存储区域根目录下的文件 Context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)目录下的Pictures目录-->
        <!--/storage/emulated/0/Android/data/com.hm.camerademo/files/Sample-->
        <external-files-path
            name="sample_external_files"
            path="Sample"
            />
        <!--代表app 外部存储区域根目录下的文件 Context.getExternalCacheDir目录下的images目录-->
        <!--/storage/emulated/0/Android/data/com.hm.camerademo/cache/Sample-->
        <external-cache-path
            name="sample_external_cache"
            path="Sample"
            />
    </paths>
</resources>
```
初始化配置
```java
PhotoPick.config()//一定要在功能使用前调用，只需要调用一次，最好在Application或启动页中调用
    .imageLoader(PicassoImageLoader())//使用Picasso加载图片
    .configProvider(BuildConfig.APPLICATION_ID + ".provider")
```
功能使用
```java
private var IMAGE_FOLDER = Environment.getExternalStorageDirectory().absolutePath + File.separator + "Sample"//文件夹的名字最好跟file_paths中设置的path保持一致
private val REQUEST_CODE = 0x000111
 PhotoPick.with()
        .savePath(IMAGE_FOLDER)// 图片存放路径
        .spanCount(4)// 每行显示个数
        .maxPickNumber(9)// 最大选择数量
        .enableAnimation(true)// 选择界面图片点击效果
        .enableCamera(true)// 是否开启拍照
        .pickedMediaList(mAdapter.mList)// 已选图片数据
        //如果是在Activity里使用就传Activity，如果是在Fragment里使用就传Fragment
        .start(this@MainActivity, 0, REQUEST_CODE)
```
获取结果
```java
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
```
## 感谢（Thanks）
UI及动画效果参考了[phoenix](https://github.com/guoxiaoxing/phoenix) ，部分代码参考了[GalleryPick](https://github.com/YancyYe/GalleryPick)
## 支持版本
支持 API Level 14+。
## Author
szhangbiao, szhangbiao@gmail.com
