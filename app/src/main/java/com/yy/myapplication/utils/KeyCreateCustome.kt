package com.yy.myapplication.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.yy.myapplication.R
import com.yy.myapplication.entity.BtnStyleType
import com.yy.myapplication.entity.ButtonEntity
import com.yy.myapplication.entity.ContType

/**
 * 按键增删改工具
 */
object KeyCreateCustome {
    const val TAG = "KeyCreateCustome"
    private var mNavigationBar = 0
    fun changeView(btn: ButtonEntity, rootView: ViewGroup, context: Context, isChangText: Boolean) {
        var realShowWidth = context.resources.displayMetrics.widthPixels
        var screenH = context.resources.displayMetrics.heightPixels
        val width = rootView.width
        if (screenH > realShowWidth) {
            val temp = screenH
            screenH = realShowWidth
            realShowWidth = temp
        }
        if (width in 1 until realShowWidth) {
            realShowWidth = width
        }
        val childCount = rootView.childCount
        for (i in 0..childCount) {
            val child = rootView.getChildAt(i)
            val tag = child.tag
            if (tag is ButtonEntity && tag == btn) {
                tag.multiple = btn.multiple
                tag.pos = btn.pos
                tag.pressType = btn.pressType
                child.tag = tag
                when (btn.btnStyleType) {
                    BtnStyleType.RocketDirection.type, BtnStyleType.Rocket.type -> {
                        val lp = child.layoutParams as RelativeLayout.LayoutParams
                        var x = GameConstant.DirectionWh
                        var y = GameConstant.DirectionWh
                        setLp(child, lp, x, y, realShowWidth, screenH, btn.pos[0], btn.pos[1])
                    }
                    BtnStyleType.MouseType.type -> {
                        val lp = child.layoutParams as RelativeLayout.LayoutParams
                        var x = GameConstant.MouseLeftRightW
                        var y = GameConstant.MouseLeftRightH
                        setLp(child, lp, x, y, realShowWidth, screenH, btn.pos[0], btn.pos[1])
                    }
                    BtnStyleType.Big.type, BtnStyleType.Medium.type, BtnStyleType.Small.type -> {
                        when (btn.contType) {
                            ContType.Text.type -> {
                                val lp = child.layoutParams as RelativeLayout.LayoutParams
                                var x = btn.baseWidth * btn.multiple
                                var y = btn.baseWidth * btn.multiple
                                setLp(
                                    child,
                                    lp,
                                    x,
                                    y,
                                    realShowWidth,
                                    screenH,
                                    btn.pos[0],
                                    btn.pos[1]
                                )
                            }
                            ContType.Pic.type -> {
                                val lp = child.layoutParams as RelativeLayout.LayoutParams
                                var x = btn.baseWidth * btn.multiple
                                var y = btn.baseWidth * btn.multiple
                                setLp(
                                    child,
                                    lp,
                                    x,
                                    y,
                                    realShowWidth,
                                    screenH,
                                    btn.pos[0],
                                    btn.pos[1]
                                )
                            }
                        }
                    }
//                    BtnStyleType.Wheel.type, BtnStyleType.WheelNew.type -> {
//                        val lp = child.layoutParams as RelativeLayout.LayoutParams
//                        var x = btn.baseWidth * btn.multiple
//                        var y = btn.baseHeight * btn.multiple
//                        setLp(child, lp, x, y, realShowWidth, screenH, btn.pos[0], btn.pos[1])
//                        if (isChangText) {
//                            val circleWheelView = child as CircleWheelView
//                            circleWheelView.changeCentextText(btn.text ?: "轮盘")
//                        }
//                    }
                    BtnStyleType.MultKey.type -> {
                        val lp = child.layoutParams as RelativeLayout.LayoutParams
                        var x = btn.baseWidth * btn.multiple
                        var y = btn.baseHeight * btn.multiple
                        setLp(child, lp, x, y, realShowWidth, screenH, btn.pos[0], btn.pos[1])
                        val textView = child.findViewById<TextView>(R.id.drag_tv)
                        textView?.text = btn.text
                    }
                }
                break
            }
        }
    }

    private fun setLp(
        childView: View,
        lp: RelativeLayout.LayoutParams,
        xdp: Float,
        ydp: Float,
        screenW: Int,
        screenH: Int,
        xMargin1: Float,
        yMargin1: Float
    ) {
        val xdpReal = DensityUtil.dip2px(childView.context, xdp)
        val yDpReal = DensityUtil.dip2px(childView.context, ydp)
        lp.width = xdpReal
        lp.height = yDpReal
        var leftMargin = (screenW * xMargin1 / 100).toInt()
        var topMargin = (screenH * yMargin1 / 100).toInt()
        topMargin -= yDpReal / 2
        leftMargin -= xdpReal / 2
        if (screenW - leftMargin < xdpReal) {
            leftMargin = screenW - xdpReal
        }
        if (screenH - topMargin < yDpReal) {
            topMargin = screenH - yDpReal
        }
        if (leftMargin < 0) {
            leftMargin = 0
        }
        if (topMargin < 0) {
            topMargin = 0
        }
        Log.d(
            TAG,
            "leftMargin=$leftMargin  topMargin=$topMargin scrrenw=$screenW  screenH =$screenH"
        )
        lp.leftMargin = leftMargin
        lp.topMargin = topMargin
        childView.layoutParams = lp
    }
}