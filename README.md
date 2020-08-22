> android-kit 致力于为android关键场景提供解决方案。  
> 知之非艰,行之惟艰。

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

### 场景

- [x] 规范（编码、commit msg、命名） [readme](./DEVELOPERS.md)

- [x] version code(name) v1R00S00I00  & app name [readme](app/build.gradle)

- [x] signingConfigs 生产keystore配置安全 [readme](https://juejin.im/post/6855939988715438088)

- [x] 项目分包结构 [readme](./DEVELOPERS.md#项目分包结构)

- [x] 模块source引入与maven aar引入动态切换
    - [android-kit](./)
    - [dagger2&room](https://medium.com/mindorks/writing-a-modular-project-on-android-304f3b09cb37) [翻译](https://blog.wangjiegulu.com/2018/02/13/writing_a_modular_project_on_android/)

- [ ] 模块独立初始化，性能监听（首屏时间、）
    - 使用androidx.startup:startup-runtime完全各个模块初始化
    - 懒加载使用Looper.myQueue().addIdleHandler(idleHandler); TODO 简单封装

- [ ] 环境切换（api、三方key）

- [ ] res管理 & 换肤  
    采用官方Theme方案，切换主题时recreate所有activity。  
    参考：
    - [总结&翻译](https://juejin.im/post/6844904200673968141)
    - [material](https://material.io/design/color/the-color-system.html#color-theme-creation)
    - [styling](https://medium.com/androiddevelopers/android-styling-themes-vs-styles-ebe05f917578)
    - [activity reCreate过渡](https://github.com/iKirby/ithomereader/blob/1f1b2ceac8c70305b37b24f13797af48e0e146d4/app/src/main/java/me/ikirby/ithomereader/ui/activity/ThemeSwitchTransitionActivity.kt)

- [x] 数据库组件化 & 数据库版本管理（跨多版本升级）
    - 组件化: [room feature module](https://github.com/android/architecture-components-samples/issues/274)
    - 数据库加密: 暂不考虑
    - room migration

- [ ] 用户数据存储分区 & 目录结构划分 & 权限受限降级处理

- [ ] 灰度、abtest（业务点、版本、）& app全局配置数据（api、缓存、更新策略）

- [ ] 页面route url & 统一外部入口（h5、推送通知栏、三方应用跳转）

- [ ] js bridge  & flutter MethodCallHandler： 对外统一&方法版本控制&权限管理

- [ ] 公共组件（toast、dialog、theme、） & 工具类

- [ ] 设计一个登录模块

- [ ] 设计一个RouterPortal

- [ ] 设计一个EventDispatcher EventHandler