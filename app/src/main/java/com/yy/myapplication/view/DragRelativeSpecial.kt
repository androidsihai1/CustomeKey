package com.yy.myapplication.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.yy.mobile.plugin.dreamerchannel.ui.cloudgame.map.inter.IDragClickListener
import com.yy.mobile.plugin.dreamerchannel.ui.cloudgame.map.inter.IDragPosChangeListener
import com.yy.myapplication.R
import com.yy.myapplication.inter.IDragView
import com.yy.myapplication.inter.IMarkListener
import kotlin.math.abs

/**
 * Created by pengsihai@yy.com on 2019/12/13.
 */
open class DragRelativeSpecial : RelativeLayout, IDragView {

    private var mLastX = 0
    private var mLastY = 0
    private var mScreenW = 0
    private var mscrenH = 0
    private var mIsNeedPosChange = false
    var mClickListener: IDragClickListener? = null
    var mPosChangeList: IDragPosChangeListener? = null
    var mCanDrag = true
    var mEnableEdit = true //是否支持编辑点击（部分不支持，如方向盘和左右键）
    var mIsNeedDragBg = true //拖拽或者点击是否支持特效
    var mIsMarkAble = false //是否是标记状态，主要使用于合并轮盘和轮盘编辑状态
    var mIsShowNum = false //是否显示数字
    var mIMarkListener: IMarkListener? = null

    constructor(context: Context?) : this(context, null, -1)

    constructor(context: Context?, attributeSet: AttributeSet?) : this(context, attributeSet, -1)

    constructor(context: Context?, attributeSet: AttributeSet?, defaultStyle: Int) :
            super(context, attributeSet, defaultStyle) {
        initView(context)
    }

    /**
     * 非轮盘编辑状态是，重置
     */
    fun resetStatus() {
        mIsMarkAble = false
        mIsShowNum = false
        var numTv = findViewById<TextView>(R.id.mark_num_tv)
        numTv?.visibility = View.INVISIBLE
        setBackgroundResource(R.mipmap.btn_normal_bg)
    }

    fun setNumTv(num: String) {
        var numTv = findViewById<TextView>(R.id.mark_num_tv)
        numTv?.text = num
    }

    fun initView(context: Context?) {
        mscrenH = context?.resources?.displayMetrics?.heightPixels ?: 0
        mScreenW = context?.resources?.displayMetrics?.widthPixels ?: 0
        //LayoutInflater.from(context).inflate(R.layout.drag_text_layout, this)
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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!mCanDrag) {
            return super.onTouchEvent(event)
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = event.rawX.toInt()
                mLastY = event.rawY.toInt()
                mIsNeedPosChange = false
                if (mIsNeedDragBg) {
                    setBackgroundResource(R.mipmap.btn_press_bg)
                }
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
                if (!mIsMarkAble && (abs(changeX) >= 2 || abs(changeY) >= 2)) {
                    layout(left, top, right, bottom)
                    mIsNeedPosChange = true
                }
                mLastX = event.rawX.toInt()
                mLastY = event.rawY.toInt()
            }
            MotionEvent.ACTION_UP -> {
                Log.d(
                    TAG, "mIsNeedPosChange=$mIsNeedPosChange marginLeft=${left + width / 2}" +
                            "  margintop=${top + height / 2})}"
                )
                if (mIsMarkAble) {
                    switchNumStatus()
                } else {
                    if (mIsNeedPosChange) {
                        mPosChangeList?.onPosChangeListener(
                            this, (left + width / 2).toFloat(),
                            (top + height / 2).toFloat()
                        )
                    } else {
                        if (mEnableEdit) {
                            mClickListener?.onClickListener(this)
                        } else {
                            //ToastUtil.showToast(R.string.spicial_tip)
                        }
                    }
                    if (mIsNeedDragBg) {
                        setBackgroundResource(R.mipmap.btn_normal_bg)
                    }
                }
            }
        }
        return true
    }

    private fun switchNumStatus() {
        mIsShowNum = !mIsShowNum
        var numTv = findViewById<TextView>(R.id.mark_num_tv)
        mIMarkListener?.onNumMark(mIsShowNum, this, numTv)
    }

    companion object {
        const val TAG = "DragRelativeSpecial"
    }
}