package com.example.screenshotfix

import android.app.AndroidAppHelper
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.Display
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class XposedInit : IXposedHookLoadPackage {
	override fun handleLoadPackage(lpparam: LoadPackageParam) {
		if (lpparam.packageName == "com.android.systemui" && lpparam.processName == "com.android.systemui:screenshot") {
			"com.android.systemui.screenshot.GlobalScreenshot".replaceMethod(lpparam.classLoader,"takeScreenshot",Runnable::class.java,Boolean::class.java,Boolean::class.java,Rect::class.java) { param->
				val finisher = param.args[0] as Runnable
				val mDisplayMetrics = param.thisObject.getObjectFieldAs<DisplayMetrics>("mDisplayMetrics")
				val mScale = param.thisObject.getObjectFieldAs<Float>("mScale")
				val mScreenBitmap = lpparam.classLoader.loadClass("android.view.SurfaceControl").callStaticMethodAs<Bitmap?>("screenshot", Rect(), (mDisplayMetrics.widthPixels/mScale).toInt(), (mDisplayMetrics.heightPixels/mScale).toInt(), param.thisObject.getObjectFieldAs<Display>("mDisplay").rotation)
				param.thisObject.setObjectField("mScreenBitmap", mScreenBitmap)
				if (mScreenBitmap == null) {
					param.thisObject.callMethod("notifyScreenshotError", param.thisObject.getObjectFieldAs<Context>("mContext"), param.thisObject.getObjectFieldAs<NotificationManager>("mNotificationManager"), AndroidAppHelper.currentApplication().resources.getIdentifier("screenshot_failed_to_capture_text", "string", "com.android.systemui"))
					finisher.run()
				} else {
					mScreenBitmap.setHasAlpha(false)
					mScreenBitmap.prepareToDraw()
					param.thisObject.callMethod("startAnimation", finisher, mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels, param.args[1], param.args[2])
				}
			}
		}
	}
}