package config;

/**
 * @author Xiaoc
 * @since 2022-07-25
 *
 * 这是配置库
 */
public class Libs {

    // Android-Core库
    public static final String CORE_KTX = "androidx.core:core-ktx:1.8.0";
    // Android-Appcompat兼容库
    public static final String APPCOMPAT = "androidx.appcompat:appcompat:1.4.2";
    // 协程库
    public static final String KOTLIN_VERSION = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:" + Versions.KOTLIN;
    // 协程库
    public static final String KOTLIN_COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4";
    // 约束布局
    public static final String CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:2.1.4";
    // Fragment-Ktx
    public static final String FRAGMENT_KTX = "androidx.fragment:fragment-ktx:1.5.1";
    // Activity-Ktx
    public static final String ACTIVITY_KTX = "androidx.activity:activity-ktx:1.5.1";
    // Material Design
    public static final String MATERIAL_DESIGN = "com.google.android.material:material:1.6.1";

    private static final String LIFECYCLE_VERSION = "2.5.1";
    // ViewModel-Ktx
    public static final String VIEWMODEL_KTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:" + LIFECYCLE_VERSION;
    // LIVEDATA-Ktx
    public static final String LIVEDATA_KTX = "androidx.lifecycle:lifecycle-livedata-ktx:" + LIFECYCLE_VERSION;
    // LIVECYCLE_RUNTIME-Ktx
    public static final String LIFECYCLE_RUNTIME_KTX = "androidx.lifecycle:lifecycle-runtime-ktx:" + LIFECYCLE_VERSION;
    // LIVECYCLE_COMMON
    public static final String LIFECYCLE_COMMON_KTX = "androidx.lifecycle:lifecycle-common-java8:" + LIFECYCLE_VERSION;

    public static final String NAVIGATION_VERSION = "2.4.2";
    // NAVIGATION导航-Fragment-Ktx
    public static final String NAVIGATION_FRAGMENT_KTX = "androidx.navigation:navigation-fragment-ktx:" + NAVIGATION_VERSION;
    // NAVIGATION导航-UI-Ktx
    public static final String NAVIGATION_UI_KTX = "androidx.navigation:navigation-ui-ktx:" + NAVIGATION_VERSION;
    // smooth-navigation
    public static final String SMOOTH_NAVIGATION = "com.kunminx.arch:smooth-navigation:4.0.0";

    private static final String COIL_VERSION = "2.2.0";
    // Coil图片加载库
    public static final String COIL = "io.coil-kt:coil:" + COIL_VERSION;
    // Coil Gif支持
    public static final String COIL_GIF = "io.coil-kt:coil-gif:" + COIL_VERSION;
    // Coil 转换器
    public static final String COIL_TRANSFORMERS = "jp.wasabeef.transformers:coil:1.0.6";
    // BRAV RV适配器
    public static final String BRAV_ADAPTER = "com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.7";
    // FLEXBOX
    public static final String FLEX_BOX = "com.google.android.flexbox:flexbox:3.0.0";
    // 动态替换BaseUrl库，使用可参考 https://github.com/JessYanCoding/RetrofitUrlManager
    public static final String RETROFIT_URL_MANAGER = "me.jessyan:retrofit-url-manager:1.4.0";
    // 持久化Cookies
    public static final String RETROFIT_CookiesJar = "com.github.franmontiel:PersistentCookieJar:v1.0.1";
    // immersionbar-Ktx
    public static final String IMMERSIONBAR_BOX_KTX = "com.geyifeng.immersionbar:immersionbar-ktx:3.2.2";
    // immersionbar-Ktx
    public static final String IMMERSIONBAR_BOX = "com.geyifeng.immersionbar:immersionbar:3.2.2";
    // Timber日志
    public static final String TIMBER = "com.jakewharton.timber:timber:5.0.1";
    // MultiStatePage状态库
    public static final String MULTI_STATE_PAGE = "com.github.Zhao-Yan-Yan:MultiStatePage:2.0.5";
    // AutoSize库
    public static final String AUTO_SIZE = "com.github.JessYanCoding:AndroidAutoSize:v1.2.1";
    // MMKV
    public static final String MMKV = "com.tencent:mmkv:1.2.13";
    // AndroidUtils
    public static final String ANDROID_UTIL_CODEX = "com.blankj:utilcodex:1.31.1";
    // SwipeRefresh
    public static final String SWIPE_REFRESH = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0";

    private static final String SMART_REFRESH_VERSION = "2.0.5";
    // SwipeRefresh
    public static final String SMART_REFRESH = "io.github.scwang90:refresh-layout-kernel:" + SMART_REFRESH_VERSION;
    // SwipeRefresh 经典刷新头
    public static final String SMART_REFRESH_HEADER_CLASSICS = "io.github.scwang90:refresh-header-classics:" + SMART_REFRESH_VERSION;
    // SwipeRefresh 雷达刷新头
    public static final String SMART_REFRESH_HEADER_RADAR = "io.github.scwang90:refresh-header-radar:" + SMART_REFRESH_VERSION;
    // SwipeRefresh 虚拟刷新头
    public static final String SMART_REFRESH_HEADER_FALSIFY = "io.github.scwang90:refresh-header-falsify:" + SMART_REFRESH_VERSION;
    // SwipeRefresh 谷歌刷新头
    public static final String SMART_REFRESH_HEADER_MATERIAL = "io.github.scwang90:refresh-header-material:" + SMART_REFRESH_VERSION;
    // SwipeRefresh 二级刷新头
    public static final String SMART_REFRESH_HEADER_TWO_LEVEL = "io.github.scwang90:refresh-header-two-level:" + SMART_REFRESH_VERSION;
    // SwipeRefresh 球脉冲加载
    public static final String SMART_REFRESH_FOOTER_BALL = "io.github.scwang90:refresh-footer-ball:" + SMART_REFRESH_VERSION;
    // SwipeRefresh 球脉冲加载
    public static final String SMART_REFRESH_FOOTER_CLASSICS = "io.github.scwang90:refresh-footer-classics:" + SMART_REFRESH_VERSION;

    // DialogX
    public static final String DIALOG_X = "com.kongzue.dialogx:DialogX:0.0.47";

    // AGENT_CORE
    public static final String AGENT_WEB_CORE = "com.github.Justson.AgentWeb:agentweb-core:v5.0.6-androidx";
    // AGENT 文件选择器
    public static final String AGENT_WEB_FILE_CHOOSER = "com.github.Justson.AgentWeb:agentweb-filechooser:v5.0.6-androidx";
    // AGENT 下载器
    public static final String AGENT_WEB_DOWNLOADER = "com.github.Justson:Downloader:v5.0.4-androidx";

    // Banner
    public static final String BANNER_VIEWPAGER = "com.github.zhpanvip:bannerviewpager:3.5.11";
    // ShapeView
    public static final String SHAPE_VIEW = "com.github.getActivity:ShapeView:8.5";
    // DOCUMENT_FILE 文件树扩展
    public static final String DOCUMENT_FILE = "androidx.documentfile:documentfile:1.0.1";

    // Retrofit
    public static final String RETROFIT = "com.squareup.retrofit2:retrofit:2.9.0";
    // OKHTTP
    public static final String OKHTTP = "com.squareup.okhttp3:okhttp:4.9.3";
    // Kotlin 序列化
    public static final String KOTLIN_SERIALIZATION = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0";
    // OkHttp日志记录拦截器
    public static final String OKHTTP_LOGGING = "com.squareup.okhttp3:logging-interceptor:4.9.3";

    // Paging3
    public static final String PAGING3 = "androidx.paging:paging-runtime-ktx:3.1.1";

    // Legacy-Support
//    public static final String LEGACY_SUPPORT = "androidx.legacy:legacy-support-v4:1.0.0";

    // Media3
    public static final String MEDIA3_VERSION = "1.0.0-rc01";
    // Media3-Session会话
    public static final String MEDIA3_SESSION = "androidx.media3:media3-session:" + MEDIA3_VERSION;
    // Media3-ExoPlayer播放器
    public static final String MEDIA3_EXOPLAYER = "androidx.media3:media3-exoplayer:" + MEDIA3_VERSION;
    // FUTURES 异步调用的Ktx扩展
    public static final String CONCURRENT_FUTURES_KTX = "androidx.concurrent:concurrent-futures-ktx:1.1.0";

    public static final String VERTICAL_TAB_LAYOUT = "q.rorbin:VerticalTabLayout:1.2.5";

    // 权限申请PermissionX
    public static final String PERMISSION_X = "com.guolindev.permissionx:permissionx:1.6.4";

    // GSON
    public static final String GSON = "com.google.code.gson:gson:2.10";

    private static final String PREFERENCE_VERSION = "1.2.0";

    // 设置库
    public static final String PREFERENCE = "androidx.preference:preference:" + PREFERENCE_VERSION;
    public static final String PREFERENCE_KTX = "androidx.preference:preference-ktx:" + PREFERENCE_VERSION;


    // 测试库
    public static final String JUNIT = "junit:junit:4.13.2";
    // Android测试适用扩展
    public static final String JUNIT_EXT = "androidx.test.ext:junit:1.1.3";
    public static final String ESPRESSO_CORE = "androidx.test.espresso:espresso-core:3.4.0";

    // Lottie
    public static final String LOTTIE_CORE = "com.airbnb.android:lottie:6.0.0";

    // Lottie底部导航
    public static final String LOTTIE_BOTTOM_NAV = "com.github.wwdablu:lottiebottomnav:1.2.0";

    // 标题栏框架：https://github.com/getActivity/TitleBar
    public static final String TITLE_BAR = "com.github.getActivity:TitleBar:10.3";
}
