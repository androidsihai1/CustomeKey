package com.yy.myapplication.inter

import android.view.View
import android.widget.TextView

/**
 * Created by pengsihai@yy.com on 2020/5/19.
 * @param isShowMark 是否展示数字标记
 */
interface IMarkListener {
    fun onNumMark(isShowMark: Boolean, view: View, numTv: TextView)
}