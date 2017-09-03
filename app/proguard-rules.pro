# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class com.just.library.** {
    *;
}
-dontwarn com.just.library.**
# 沉浸状态栏
-keep class com.gyf.barlibrary.* {*;}

#-libraryjars libs/alipaySingle-20170510.jar
-keep class com.alipay.** {
    *;
}
-dontwarn com.alipay.**
#微信

-keep class com.tencent.mm.**{*;}
-dontwarn com.tencent.mm.**

#底部弹出分享对话框
-keep class me.curzbin.library.** {
    *;
}
-dontwarn me.curzbin.library.**



