# ScreenshotFix

a xposed module that fix 4k screenshot black border on xperia pie devices

screenshot of a 4k checkerboard, as you can see, after using this module checkerboard showed correctly(not just gray blank) in the screenshot, it means that this module take a "real" 4k screenshot rather than simply scale&crop 1k screenshot to 4k
![pic](compare.png)
<div align="center">before/after</div>

## usage

1. install edxposed and this module
2. active the module
3. if you are using whitelist mode in xposed, add `com.android.systemui` to your whitelist

## how it works

i just simply replaced `Rect(0, 0, width, height)` with `Rect()` (as what used in android oreo), and it works

## about opensource

for convenience, im using a part code(`KotlinXposedHelper.kt` and `Log.kt`) from [BiliRoaming](https://github.com/yujincheng08/BiliRoaming), if you mind this plesse tell me, i will rewrite this part of code by myself
