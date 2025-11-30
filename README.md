# MyTikTok高仿抖音经验频道
一个模仿抖音经验频道展示的应用，采用瀑布流布局，支持单双列切换，支持用户交互体验。
## 核心功能
1. 可切换的单双列瀑布流展示
2. 实时点赞收藏交互
3. 上拉加载更多内容
4. 下拉刷新内容
5. 图片预加载，提升滑动流畅度
6. Mock数据模拟，配置网络图片资源
## 技术栈
- 语言：JAVA
- 图片加载：Glide
- 架构模式：MVC+Respository
## 项目结构
~~~
app/
├── java/com.example.mytiktok/
│   ├── MainActivity.java          # 主界面
│   ├── ExperienceAdapter.java     # 列表适配器
│   ├── ExperienceItem.java        # 数据模型
│   ├── InteractionManager.java    # 交互管理
│   ├── MockApiService.java        # 模拟数据服务
│   ├── DataRepository.java        # 数据仓库
│   └── ImagePreloadManager.java   # 图片预加载管理
├── res/
│   ├── layout/                    # 布局文件
│   ├── drawable/                  # 图标资源
│   └── values/                    # 资源文件
~~~
## 使用方法
1. 克隆项目到本地
2. 使用 Android Studio 打开项目
3. 同步 Gradle 依赖
4. 连接设备或启动模拟器
5. 运行应用
#### Gradle依赖
~~~
dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}   
~~~
#### 网络权限（在AndroidMainfest.xml中添加）
~~~
<uses-permission android:name="android.permission.INTERNET" />
~~~
### 基本操作
1. 浏览内容-上下滑动查看瀑布流内容
2. 刷新内容-下拉列表刷新最新数据
3. 加载更多-滚动到底部自动加载
4. 点赞收藏-点击对应图标即可
5. 布局切换-点击右上角图标切换单双列

## 联系方式
netizen66@foxmail.com