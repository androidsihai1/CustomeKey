package com.yy.myapplication.utils

import android.app.Service
import android.content.Context
import android.os.Vibrator
import com.yy.myapplication.entity.ContType

/**
 * Created by andy on 2020/9/14.
 */
object GameUtils {
    private var mVibrator: Vibrator? = null
    private var mLastClickTime = 0L
    fun isFastClick(divideTime: Long): Boolean {
        val currentTimeMillis = System.currentTimeMillis()
        return if (currentTimeMillis - mLastClickTime > divideTime) {
            mLastClickTime = currentTimeMillis
            false
        } else {
            true
        }
    }

    fun isChinese(c: Char): Boolean {
        return c.toInt() in 0x4E00..0x9FA5 // 根据字节码判断
    }

    fun containChinese(str: String): Boolean {
        str.toCharArray().forEach {
            if (isChinese(c = it)) {
                return true
            }
        }
        return false
    }

    fun shark(context: Context) {
        if (mVibrator == null) {
            mVibrator = context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        }
        mVibrator?.vibrate(GameConstant.VibrateTime)

    }


    fun sp2px(sp: Float, context: Context): Float {
        val scale: Float = context.resources.displayMetrics.scaledDensity
        return (sp * scale + 0.5f)
    }


    /**
     * 从志伟后台拷贝的图片地址（为了保持统一）
     */
    fun getPicByBean(mean: Int): String? {
        return when (mean) {
            GameConstant.LeftMouse -> "https://peiwan.bs2dl-ssl.huanjuyun.com/04ab3bea53a3491592d25fb2f628f36f.png"
            GameConstant.RightMouse -> "https://peiwan.bs2dl-ssl.huanjuyun.com/418c543a2e2b46578b9c4e0f9d55a60b.png"
            GameConstant.CenterMouse -> "https://peiwan.bs2dl-ssl.huanjuyun.com/1beac372b989468b861d28bc361df5f7.png"
            GameConstant.PageUp -> "https://peiwan.bs2dl-ssl.huanjuyun.com/373ad359d20c4a28b189a80471013cfc.png"
            GameConstant.PageDown -> "https://peiwan.bs2dl-ssl.huanjuyun.com/c3dcc9b02d024d6c8cc470663ddf059a.png"
            GameConstant.Keyboard -> "https://peiwan.bs2dl-ssl.huanjuyun.com/4131abf7d9fd4e49883bc9deb91e11eb.png"
            else -> null
        }
    }

    fun getPicTextByBean(mean: Int): String {
        return when (mean) {
            GameConstant.LeftMouse -> GameConstant.Left_Key_Str
            GameConstant.RightMouse -> GameConstant.right_Key_Str
            GameConstant.CenterMouse -> GameConstant.center_Key_Str
            GameConstant.PageUp -> GameConstant.scroll_up_Key_Str
            GameConstant.PageDown -> GameConstant.scroll_down_Key_Str
            GameConstant.Keyboard -> GameConstant.keyboard_Key_Str
            else -> "无"
        }
    }

    /**
     * 仅用于区分 text 和 pic类型
     */
    fun getConType(mean: Int): ContType {
        return when (mean) {
            GameConstant.LeftMouse,
            GameConstant.RightMouse,
            GameConstant.CenterMouse,
            GameConstant.PageUp,
            GameConstant.PageDown -> ContType.Pic
            else -> ContType.Text
        }
    }


}