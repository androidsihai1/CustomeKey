package com.yy.myapplication.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import com.yy.myapplication.utils.DimensionUtil
import java.math.BigDecimal

/**
 * Created by pengsihai@yy.com on 2020/5/25.
 */
class ScaleTextView : AppCompatTextView {

    var mMaxWidthDp: Int = 148
    var mDurWidthDp: Int = 8

    constructor(context: Context) : this(context, null, -1)

    constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, -1)

    constructor(context: Context, attributeSet: AttributeSet?, defaultStyle: Int) :
            super(context, attributeSet, defaultStyle) {
    }

    private fun adjustTextSize() {
        if (!text.isNullOrEmpty() && width > 0) {
            var durWidth = (mMaxWidthDp - DimensionUtil.pxToDip(context, width.toFloat()))
            val b1 = BigDecimal(durWidth.toDouble())
            val b2 = BigDecimal(mDurWidthDp.toDouble())
            var tmp = b1.divide(b2).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
            var n: Int = Math.round(tmp).toInt()
            var num: Int = countChineseNumber(text.toString())
            when (text.length) {
                1 -> {
                    var size = 32 - (n * 2)
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, DimensionUtil.sp2Px(size).toFloat())
                }
                2, 3 -> {
                    var size = 28 - (n * 2)
                    if (size < 11) {
                        size = 11
                    }
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, DimensionUtil.sp2Px(size).toFloat())
                }
                4 -> {
                    when (num) {
                        0 -> {
                            var size = 28 - (n * 2)
                            if (size < 11) {
                                size = 11
                            }
                            if (size >= 26) {
                                size = 26
                            }
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                DimensionUtil.sp2Px(size).toFloat()
                            )
                        }
                        1 -> {
                            var size = 27 - (n * 2)
                            if (size < 11) {
                                size = 11
                            }
                            if (size >= 25) {
                                size = 25
                            }
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                DimensionUtil.sp2Px(size).toFloat()
                            )
                        }
                        2 -> {
                            var size = 26 - (n * 2)
                            if (size < 11) {
                                size = 11
                            }
                            if (size >= 24) {
                                size = 24
                            }
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                DimensionUtil.sp2Px(size).toFloat()
                            )
                        }
                        3 -> {
                            var size = 24 - (n * 2)
                            if (size < 10) {
                                size = 10
                            }
                            if (size >= 20) {
                                size = 20
                            }
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                DimensionUtil.sp2Px(size).toFloat()
                            )
                        }
                        4 -> {
                            var size = 22 - (n * 2)
                            if (size < 9) {
                                size = 9
                            }
                            if (size >= 18) {
                                size = 18
                            }
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                DimensionUtil.sp2Px(size).toFloat()
                            )
                        }
                    }
                }
                5 -> {
                    when (num) {
                        0 -> {
                            var size = 26 - (n * 2)
                            if (size < 11) {
                                size = 11
                            }
                            if (size >= 24) {
                                size = 24
                            }
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                DimensionUtil.sp2Px(size).toFloat()
                            )
                        }
                        1 -> {
                            var size = 24 - (n * 2)
                            if (size < 10) {
                                size = 10
                            }
                            if (size >= 22) {
                                size = 22
                            }
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                DimensionUtil.sp2Px(size).toFloat()
                            )
                        }
                        2 -> {
                            var size = 22 - (n * 2)
                            if (size < 9) {
                                size = 9
                            }
                            if (size >= 20) {
                                size = 20
                            }
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                DimensionUtil.sp2Px(size).toFloat()
                            )
                        }
                        else -> {
                            var size = 20 - (n * 2)
                            if (size < 8) {
                                size = 8
                            }
                            if (size >= 18) {
                                size = 18
                            }
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                DimensionUtil.sp2Px(size).toFloat()
                            )
                        }
                    }
                }
                6 -> {
                    when (num) {
                        0 -> {
                            var size = 26 - (n * 2)
                            if (size < 11) {
                                size = 11
                            }
                            if (size >= 24) {
                                size = 24
                            }
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                DimensionUtil.sp2Px(size).toFloat()
                            )
                        }
                        1 -> {
                            var size = 24 - (n * 2)
                            if (size < 10) {
                                size = 10
                            }
                            if (size >= 22) {
                                size = 22
                            }
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                DimensionUtil.sp2Px(size).toFloat()
                            )
                        }
                        2 -> {
                            var size = 22 - (n * 2)
                            if (size < 9) {
                                size = 9
                            }
                            if (size >= 20) {
                                size = 20
                            }
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                DimensionUtil.sp2Px(size).toFloat()
                            )
                        }
                        3 -> {
                            var size = 20 - (n * 2)
                            if (size < 8) {
                                size = 8
                            }
                            if (size >= 18) {
                                size = 18
                            }
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                DimensionUtil.sp2Px(size).toFloat()
                            )
                        }
                        else -> {
                            var size = 18 - (n * 2)
                            if (size < 7) {
                                size = 7
                            }
                            if (size >= 16) {
                                size = 16
                            }
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                DimensionUtil.sp2Px(size).toFloat()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun isChinese(c: Char): Boolean {
        return c.toInt() in 0x4E00..0x9FA5 // 根据字节码判断
    }

    private fun countChineseNumber(str: String): Int {
        var num: Int = 0
        if (!str.isNullOrEmpty()) {
            str.toCharArray().forEach {
                if (isChinese(it)) {
                    num++
                }
            }
        }
        return num
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        adjustTextSize()
    }

    companion object {
        const val TAG = "ScaleTextView"
    }
}