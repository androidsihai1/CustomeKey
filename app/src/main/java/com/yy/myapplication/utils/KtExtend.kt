package com.yy.myapplication.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

/**
 * Created by andy on 2020/9/25.
 */
object KtExtend {
    fun Int.dp(context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        )
    }


    val Float.dp
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
        )


    fun Int.px(context: Context): Int {
        return DensityUtil.dip2px(context, this.toFloat())
    }

    fun Float.px(context: Context): Int {
        return DensityUtil.dip2px(context, this)
    }
}