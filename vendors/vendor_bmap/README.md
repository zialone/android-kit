由于百度地图没有gradle依赖地址，所以需要发布到自建maven仓库。 
发布脚本参考 ./z_raw/BaiduLBS_AndroidSDK_Lib/maven_publishing.gradle

发布后可以删除项目中 app/build  vendors/vendor_bmap/build.gradle中
```
repositories {
    flatDir {
        dirs './z_raw/BaiduLBS_AndroidSDK_Lib'
    }
}
```