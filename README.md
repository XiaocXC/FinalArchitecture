### 简介

这是一个基于MVVM/MVI的玩安卓架构

### 项目结构

- app：APP客户端
- library_base：基础模块
- library_base：网络模块



### 基本组件使用规范

#### 资源文件命名规范

##### 状态

暂将状态分为以下情况（如果图标等内容只有一个状态，则可以进行状态命名）：

- n：normal正常状态
- s：selected选中状态
- d：disabled禁用状态

##### 图标类

```xml
矢量图标：ic_描述_状态
例如：ic_me_n

png等图标：img_描述_状态
例如：image_me_n
```

##### 各类Shape文件

以其具体形式作为文件名开头

```xml
各类形式_描述_状态
例如：shape_me_n
例如：selector_me_n
```

##### 布局文件 XML命名

```xml
activity_描述
例如：activity_me

fragment_描述
例如：fragment_me

dialog_描述
例如：dialog_me

view_描述（自定义控件）
例如：view_me_card

popup_描述（弹出气泡类）
例如：popup_home_select_type

item_描述（RecyclerView列表项）
例如：item_home_card

对于整个模块都可以复用的内容可以在前加global字段
例如：global_item_home_card
```



#### Coil图片加载框架

1. 为什么选择Coil？

    - 使用Kotlin与协程进行图片加载，三级缓存，且Google官方极力推荐
    - 操作简单无需额外操作
    - 无侵入，能够配合其他库

2. 使用：具体详情可见Coil官方指南

   ```kotlin
   val imageView = findViewById(R.id.imageView)
   imageView.load(uri) // 使用load()进行图片加载即可
   ```




#### ViewModel中基本的网络请求

对于一般请求，我们只需要调用内置的不同请求即可达到快速处理数据和状态的目的

- 例如：你想要获取列表数据，然后把数据显示在界面上

```kotlin
// 启动协程作用域
viewModelScope.launch {
    // 基本的网络请求
    launchRequestByNormal({
        // 第一个lambda书写请求的协程方法
        ApiRepository.requestBanner()
    }, successBlock = {
        // 请求成功，我们把数据填入到对应StateFlow中
         _bannerList.value = it
    }, failureBlock = {
        // 失败做其他处理
        ...
    })
}
```

- 上面基本的网络请求会把 ApiResult 通过一些处理暴露出具体错误和数据，但有时你可能希望请求回来的数据不要脱壳，保留UiModel类型包装，那么可以使用 `launchRequestByNormalOnlyResult()`方法进行网络请求
- 有时你需要多个请求一起异步操作，那么你可以在同一个协程作用下，使用`async`来进行异步请求




#### 数据模型层的请求方式（可选，基于MVI）

根据不同的请求需求，在领域层目前有三种基本请求方式（待完善）



#### 视图状态UiModel

UI层的视图需要了解具体的请求状态，例如加载中、加载完成、加载失败时的错误信息。UiModel数据类型则包含了这几种状态。

BaseViewModel中有一个`rootViewState`的变量，它维护着对应整个界面或者对应模块的总状态，它默认是没有任何状态的。当你调用ViewModel中的`initData()`方法进行数据初始化或者重新请求时，如果你传入的`resetState`值为true，那么`rootViewState`将会更改为Loading状态。

我们默认不会将ViewModel中的`rootViewState`和界面整个布局进行绑定，因为有些界面不需要这些状态或者说有的界面需要自行灵活处理，所以如果你需要此状态来更改界面，你可以自己收集该状态做对应处理。

- Fragment或Activity中收集整个界面状态

  ```kotlin
  // 整个界面状态我们使用MultiState进行界面的更改，无侵入
  launchAndRepeatWithViewLifecycle {
      // 监听整个界面的
      launch {
          systemViewModel.rootViewState.collectLatest {
               it.onSuccess {
                   // 成功展示成功界面
                  uiRootState.show(SuccessState())
               }.onLoading {
                   // 加载展示加载界面
                  uiRootState.show(LoadingState())
               }.onFailure { _, _ ->
                   // 失败展示失败界面
                  uiRootState.show(ErrorState())
               }
           }
      }
  }
  ```

  


#### 点击事件防抖

为了防止频繁触发点击事件带来的不必要的资源损耗，如果你的点击事件需要设置防抖（仅支持点击事件，长点击事件没有此功能的必要）



#### 与Paging3联动

待完善



### 常用工具集的使用

- ##### ViewExt 视图相关扩展集

- ##### ViewPager2Ext 视图相关扩展集

- ##### MaterialColor相关取色扩展集



#### AutoCleared的使用

Fragment在与Navigation配合时，在进行导航时，Fragment的生命周期只会走到 `onDestroyView`，而不会彻底摧毁Fragment。所以我们的状态和数据都需要放置在ViewModel中保持。

但是由于这种特性，视图销毁时可能会有需要视图操作的对象没有被及时销毁导致内存泄漏，所以你可以用 `autoCleared` 在Fragment中委托，让`autoCleared`自动帮你在对应生命周期时进行对象的销毁。

Fragment中：

```kotlin
// 声明 bannerAdapter 是会被自动销毁的
private var mBannerAdapter by autoCleared<ArticleBannerWrapperAdapter>()

// 这样使用即可
```

`autoCleared`还支持在不同的Fragment生命周期销毁时清理对象，默认情况下，是在Fragment调用`onDestroyView`时销毁，如果你需要在Fragment彻底销毁时才清理对象，则进行如下操作：

```kotlin
// 传入false，代表不使用viewLifecycle生命周期销毁，而使用Fragment的LifeCycle生命周期进行销毁
private var mBannerAdapter by autoCleared<ArticleBannerWrapperAdapter>(false)
```





