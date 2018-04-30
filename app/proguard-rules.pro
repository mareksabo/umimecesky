# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/msabo/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

-dontwarn com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar
-assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** d(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# Firebase Crashlytics
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
# Exclude Crashlytics for faster builds
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**