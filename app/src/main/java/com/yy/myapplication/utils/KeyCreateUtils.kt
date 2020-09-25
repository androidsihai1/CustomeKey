package com.yy.myapplication.utils

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.yy.mobile.plugin.dreamerchannel.ui.cloudgame.map.inter.IDragClickListener
import com.yy.mobile.plugin.dreamerchannel.ui.cloudgame.map.inter.IDragPosChangeListener
import com.yy.myapplication.R
import com.yy.myapplication.entity.*
import com.yy.myapplication.inter.IDragView
import com.yy.myapplication.view.CircleWheelView
import com.yy.myapplication.view.DragRelativeSpecial
import com.yy.myapplication.view.ScaleTextView

/**
 * Created by andy on 2020/9/13.
 */
object KeyCreateUtils {
    const val TAG = "KeyCreateUtils"
    var mViewList = mutableListOf<View>()
    private var screenH = 0
    private var realShowWidth = 0
    var mNumMarkViewList = mutableListOf<View>() //标记数字的集合

    private fun setLp(
        childView: View,
        lp: RelativeLayout.LayoutParams,
        dp: Int,
        screenW: Int,
        screenH: Int,
        xMargin1: Float,
        yMargin1: Float
    ) {
        lp.width = dp
        lp.height = dp
        var leftMargin = (screenW * xMargin1 / 100).toInt()
        var topMargin = (screenH * yMargin1 / 100).toInt()
        topMargin -= dp / 2
        leftMargin -= dp / 2
        if (screenW - leftMargin < dp) {
            leftMargin = screenW - dp
        }
        if (screenH - topMargin < dp) {
            topMargin = screenH - dp
        }
        if (leftMargin < 0) {
            leftMargin = 0
        }
        if (topMargin < 0) {
            topMargin = 0
        }
        lp.leftMargin = leftMargin
        lp.topMargin = topMargin
        childView.layoutParams = lp
    }

    /**
     * 自定义按键增加按键
     * @param needShowMark 是否需要显示数字标记
     */
    fun addSingleView(
        btn: ButtonEntity,
        context: Context,
        rootView: ViewGroup,
        needShowMark: Boolean
    ) {
        val btnStyleType = btn.btnStyleType
        val contType = btn.contType
        val text = btn.text
        val mean = btn.mean
        var childView: View?
        Log.d(TAG, "addSingleView btn=$btn")
        when (btnStyleType) {
            BtnStyleType.Wheel.type -> {
                childView = CircleWheelView(context)
                val textList = mutableListOf<String>()
                val meansList = mutableListOf<List<Int>>()
                btn.btns.forEach {
                    var text1 = it.text
                    if (text1.isNullOrEmpty()) {
                        if (mean.size > 1) {
                            text1 = "组合键"
                        }
                    }
                    if (text1.isNullOrEmpty()) {
                        text1 = GameUtils.getPicTextByBean(mean[0])
                    }
                    textList.add(text1)
                    meansList.add(it.mean)
                }
                childView.mTextList.addAll(textList)
                childView.mMeanList.addAll(meansList)
                childView.mCenterStr = btn.text ?: "轮盘"
                rootView.addView(childView)
                val lp = (childView.layoutParams) as RelativeLayout.LayoutParams
                val dp = DensityUtil.dip2px(context, (btn.baseWidth * btn.multiple))
                setLp(
                    childView, lp, dp, realShowWidth, screenH,
                    btn.pos[0], btn.pos[1]
                )
                checkDragState(childView, KeyMode.Default.mode, context, btn)
                mViewList.add(childView)
            }
            BtnStyleType.Big.type, BtnStyleType.Medium.type, BtnStyleType.Small.type -> {
                when (contType) {
                    ContType.Text.type -> {
                        val text = btn.text
                        val mean = btn.mean
                        childView =
                            LayoutInflater.from(context).inflate(R.layout.drag_text_layout, null)
                        rootView.addView(childView)
                        realShowWidth = context.resources.displayMetrics.widthPixels
                        screenH = context.resources.displayMetrics.heightPixels
                        val lp = (childView.layoutParams) as RelativeLayout.LayoutParams
                        val dp = DensityUtil.dip2px(context, (btn.baseWidth * btn.multiple))
                        //childView.gravity = Gravity.CENTER
                        //childView.text = text
                        childView?.background =
                            context.resources.getDrawable(R.drawable.btn_ten_selector)
                        setLp(
                            childView, lp, dp, realShowWidth, screenH,
                            btn.pos[0], btn.pos[1]
                        )
                        checkDragState(childView, KeyMode.KeyEdit.mode, context, btn)
                        addDragText(childView, text, dp, needShowMark)
                        mViewList.add(childView)
                    }
                }
            }
        }
    }

    private fun addDragText(
        childView: View, text: String?, dp: Int,
        needShowMark: Boolean
    ) {
        val lp = childView.layoutParams as RelativeLayout.LayoutParams
        lp.width = dp
        lp.height = dp
        childView?.findViewById<ScaleTextView>(R.id.drag_tv)?.text = text
        val markNumTv = childView?.findViewById<TextView>(R.id.mark_num_tv)
        val numTvLp = markNumTv?.layoutParams as RelativeLayout.LayoutParams
        numTvLp?.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        numTvLp?.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        numTvLp.topMargin = dp / 6
        numTvLp.rightMargin = dp / 6
        if (needShowMark) {
            mNumMarkViewList.add(childView)
            markNumTv.text = mNumMarkViewList.size.toString()
            markNumTv.visibility = View.VISIBLE
            if (childView is DragRelativeSpecial) {
                childView.mIsMarkAble = true
                childView.mIsShowNum = true
            }
            childView.setBackgroundResource(R.mipmap.btn_press_bg)
        }
    }

    private fun checkDragState(view: View, keyMode: Int, context: Context, it: ButtonEntity) {
        view.tag = it
        if (view is IDragView) {
            if (keyMode == KeyMode.KeyEdit.mode) {
                view.setDragState(true)
            } else {
                view.setDragState(false)
            }
            view.setDragClickListener(object : IDragClickListener {
                override fun onClickListener(v: View) {
                    val tag = v.tag as ButtonEntity
                    val mean = tag.mean
                    if (!GameUtils.isFastClick(500)) {
                        //skipToKeyEditUi(tag, context)
                    }
                }
            })
            view.setDragPosChangeListener(object : IDragPosChangeListener {
                override fun onPosChangeListener(view: View, posx: Float, posy: Float) {
                    val tag = view.tag as ButtonEntity
                    var marginLeft = posx * 100 / realShowWidth
                    var marginTop = posy * 100 / screenH
                    if (marginLeft > 100f) {
                        marginLeft = 100f
                    }
                    if (marginTop > 100f) {
                        marginTop = 100f
                    }
                    tag.pos = mutableListOf(marginLeft, marginTop)
                    view.tag = tag
                    Log.d(
                        TAG,
                        "marginLeft=$marginLeft  marginTop=$marginTop  posx=$posx posY=$posy" +
                                "  screnH=$screenH  scrrenW=$realShowWidth"
                    )
                    RxBus.getInstance().post(
                        ButtonEntityChangeEvent(
                            tag, ButtonEntityNoticeType.Change,
                            true
                        )
                    )
                }
            })
        }
    }

    fun getMergeButtonEntity(): ButtonEntity {
        var entity = getMergeBaseEntity()
        for (i in mNumMarkViewList.indices) {
            val tag = mNumMarkViewList[i].tag
            if (tag is ButtonEntity) {
                if (tag.text.isNullOrEmpty() && tag.contType == ContType.Pic.type && tag.mean.size > 0) {
                    tag.text = GameUtils.getPicTextByBean(tag.mean[0])
                }
                entity.btns.add(tag)
            }
        }
        return entity
    }

    private fun getMergeBaseEntity(): ButtonEntity {
        return ButtonEntity(
            btnStyleType = BtnStyleType.Wheel.type,
            baseWidth = GameConstant.baseWheelWidth,
            baseHeight = GameConstant.baseWheelWidth, pos = mutableListOf(50f, 50f),
            multiple = GameConstant.baseWheelCenterMultipe,
            pressType = PressType.Click.pressType, text = "轮盘",
            id = System.currentTimeMillis()
        )
    }

    fun getBaseTearEntity(): ButtonEntity {
        return ButtonEntity(
            btnStyleType = BtnStyleType.Medium.type,
            baseWidth = GameConstant.baseWidth,
            baseHeight = GameConstant.baseWidth, pos = listOf(50f, 50f),
            multiple = 1.35f, pressType = PressType.LongPress.pressType,
            id = System.currentTimeMillis()
        )
    }


    fun deleteViewAfterMerge(rtView: ViewGroup) {
        mNumMarkViewList?.forEach {
            rtView.removeView(it)
            mViewList.remove(it)
        }
    }

    fun getTearDownPos(pos: Int, listSize: Int): List<Float> {
        return when {
            listSize == 2 -> {
                listOf(43 + pos * 20f, 50f)
            }
            listSize == 3 -> {
                listOf(36 + pos * 20f, 50f)
            }
            listSize == 4 -> {
                listOf(29 + pos * 14f, 50f)
            }
            else -> {
                if (pos < 4) {
                    listOf(29 + pos * 14f, 35f)
                } else {
                    listOf(29 + (pos - 4) * 14f, 65f)
                }
            }
        }
    }

    /**
     * 删除轮盘
     */
    fun deleteWheel(buttonEntity: ButtonEntity, rootView: ViewGroup) {
        var delete = -1
        for (i in mViewList.indices) {
            if (mViewList[i].tag is ButtonEntity && mViewList[i].tag == buttonEntity) {
                delete = i
            }
        }
        if (delete != -1) {
            if (mViewList.size > delete) {
                rootView?.removeView(mViewList[delete])
                val removeAt = mViewList.removeAt(delete)
            }
        }
    }

    fun deleteView(rootView: ViewGroup) {
        mViewList.forEach {
            Log.d(TAG, "deleteView 删除view")
            rootView?.removeView(it)
        }
    }

    /**
     * 生成拆分后的图形
     */
    fun buildTearDown(list: List<ButtonEntity>, context: Context, rootV: ViewGroup) {
        list.forEach {
            addSingleView(it, context, rootV, true)
        }
    }


    fun tearDownWheel(buttonEntity: ButtonEntity): List<ButtonEntity> {
        val tearDownList = mutableListOf<ButtonEntity>()
        if (buttonEntity.btnStyleType == BtnStyleType.Wheel.type) {
            for (i in buttonEntity.btns.indices) {
                val btn = buttonEntity.btns[i]
                btn.multiple = 1.35f
                btn.pos = getTearDownPos(i, buttonEntity.btns.size)
                btn.id = System.currentTimeMillis()
                btn.baseWidth = GameConstant.baseWidth
                btn.baseHeight = GameConstant.baseHeight
                tearDownList.add(btn)
            }
        }
        return tearDownList
    }
}