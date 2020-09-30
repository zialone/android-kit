> android-kit 致力于为android关键场景提供解决方案。  
> 知之非艰,行之惟艰。

### 目的
快速构建一个组件化开发项目，并提供一些关键场景提供解决方案。  

### 快速使用

当前项目使用的package为com.hcanyz.android_kit，实际项目中需要更换为自己的package  
一个package快速转换工具，使用步骤：
1. 注意备份
2. 打开根目录 build_ext.gradle，找到 task fastPackageTransform
3. 修改destPackage，如果需要忽略一些文件夹或者文件，请修改 ignoreDirs ignoreFiles applyFileTypes
4. 执行task

有时候需要创建一个新module，比如新业务feature_xxx  
一个快速创建module工具，使用步骤：
1. 注意备份
2. 打开根目录 build_ext.gradle，找到 task fastInitModuleBySame
3. 指定模板module路径srcDir，模板module package srcPackage。指定新module路径destDir，新module package destPackage。
4. 执行task
5. 配置module dynamic include config [dependencies_app.gradle](./z_gradle/dependencies_app.gradle)