package com.yy.myapplication.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import com.yy.mobile.plugin.dreamerchannel.ui.cloudgame.map.inter.IDragClickListener
import com.yy.mobile.plugin.dreamerchannel.ui.cloudgame.map.inter.IDragPosChangeListener
import com.yy.myapplication.R
import com.yy.myapplication.inter.IDragView
import com.yy.myapplication.utils.DimensionUtil
import java.math.BigDecimal
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.abs

/**
 * Created by pengsihai@yy.com on 2019/12/13.
 */
class DragView : AppCompatTextView, IDragView {


    private var mLastX = 0
    private var mLastY = 0
    private var mScreenW = 0
    private var mscrenH = 0
    private var mIsNeedPosChange = false
    var mClickListener: IDragClickListener? = null
    var mPosChangeList: IDragPosChangeListener? = null
    private var mCanDrag = true
    var mMaxWidthDp: Int = 148
    var mDurWidthDp: Int = 8
    var mIsFirstVisible: AtomicBoolean = AtomicBoolean(false)

    constructor(context: Context) : this(context, null, -1)

    constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, -1)

    constructor(context: Context, attributeSet: AttributeSet?, defaultStyle: Int) :
            super(context, attributeSet, defaultStyle) {
        initView(context)
    }

    fun initView(context: Context) {
        mIsFirstVisible.getAndSet(false)
        mscrenH = context.resources.displayMetrics.heightPixels
        mScreenW = context.resources.displayMetrics.widthPixels
        post {
            adjustTextSize()
            mIsFirstVisible.getAndSet(true)
        }
    }

    override fun setDragClickListener(dragClickListener: IDragClickListener) {
        mClickListener = dragClickListener
    }

    override fun setDragPosChangeListener(dragPosListener: IDragPosChangeListener) {
        mPosChangeList = dragPosListener
    }

    override fun setDragState(canDrag: Boolean) {
        mCanDrag = canDrag
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
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
        if (mIsFirstVisible.get()) {
            adjustTextSize()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!mCanDrag) {
            return super.onTouchEvent(event)
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = event.rawX.toInt()
                mLastY = event.rawY.toInt()
                mIsNeedPosChange = false
                setBackgroundResource(R.mipmap.btn_press_bg)
            }
            MotionEvent.ACTION_MOVE -> {
                val changeX = (event.rawX - mLastX).toInt()
                val changeY = (event.rawY - mLastY).toInt()
                var left = left + changeX
                var right = right + changeX
                var top = top + changeY
                var bottom = bottom + changeY
                if (left < 0) {
                    left = 0
                    right = left + width
                }
                if (top < 0) {
                    top = 0
                    bottom = top + height
                }
                if (right > mScreenW) {
                    right = mScreenW
                    left = right - width
                }
                if (bottom > mscrenH) {
                    bottom = mscrenH
                    top = bottom - height
                }
                if (abs(changeX) >= 2 || abs(changeY) >= 2) {
                    layout(left, top, right, bottom)
                    mIsNeedPosChange = true
                }
                mLastX = event.rawX.toInt()
                mLastY = event.rawY.toInt()
            }
            MotionEvent.ACTION_UP -> {
                if (mIsNeedPosChange) {
                    mPosChangeList?.onPosChangeListener(
                        this, (left + width / 2).toFloat(),
                        (top + height / 2).toFloat()
                    )
                } else {
                    mClickListener?.onClickListener(this)
                }
                setBackgroundResource(R.mipmap.btn_normal_bg)
            }
        }
        return true
    }

    companion object {
        const val TAG = "DragView"
    }
}