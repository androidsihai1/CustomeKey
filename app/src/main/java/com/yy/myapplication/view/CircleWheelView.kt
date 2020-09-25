package com.yy.myapplication.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.yy.mobile.plugin.dreamerchannel.ui.cloudgame.map.inter.IDragClickListener
import com.yy.mobile.plugin.dreamerchannel.ui.cloudgame.map.inter.IDragPosChangeListener
import com.yy.myapplication.R
import com.yy.myapplication.inter.IDragView
import com.yy.myapplication.inter.OnWheelClickListener
import com.yy.myapplication.utils.GameConstant
import com.yy.myapplication.utils.GameUtils
import com.yy.myapplication.utils.KtExtend.px
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * Created by pengsihai@yy.com on 2020/5/23.
 */
class CircleWheelView : View, IDragView {

    var mTextList = mutableListOf<String>() //扇形中文字
    var mMeanList = mutableListOf<List<Int>>()

    private lateinit var mSelectPaint: Paint //扇形选中区域绘制笔
    private lateinit var mTextPaint: Paint //扇形文字绘制笔
    private lateinit var mCenterPaint: Paint //中心文字绘制笔
    private lateinit var mArcPaint: Paint //扇形绘制笔
    private lateinit var mBgPaint: Paint
    private lateinit var mspratePaint: Paint //扇形间隔线条笔
    private lateinit var mCenterNormalBitmap: Bitmap //中心未点击下背景
    private lateinit var mCenterPressBitmap: Bitmap  //中心按下背景
    private lateinit var mBg: Bitmap //扇形展开时候背景
    var mWRadius = 0f //外圆形半径
    var mCenterRadiu = 0f //中心圆半径
    var mArcRadiu = 0f //扇形半径

    var mRectOut = RectF() //最外层
    var mRectIn = RectF() //中心
    var mArcRect = RectF() //扇形区域
    var mChangeAngel = 0f //角度增加值
    var mIsEditing = false //是否编辑状态
    var mIsPress = false //是否中心被按住
    var mPressAndDrag = false //可拖拽状态且按住
    var mRegionList = mutableListOf<Region>() //扇形区域位置保存图
    var mOnWheelClickListener: OnWheelClickListener? = null //选中区域内回调
    var mLastArea = -1 //手指最后按下区域位置
    var mCenterStr = "轮盘"

    /**
     * 和拖拽有关的参数
     */
    private var mLastX = 0
    private var mLastY = 0
    private var mScreenW = 0
    private var mscrenH = 0
    private var mIsNeedPosChange = false
    var mClickListener: IDragClickListener? = null
    var mPosChangeList: IDragPosChangeListener? = null
    private var mCanDrag = true
    var mLastPos = -1

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet, -1) {
        initView()
    }

    private fun initView() {
        mSelectPaint = Paint()
        mSelectPaint.isAntiAlias = true
        mSelectPaint.style = Paint.Style.FILL
        mSelectPaint.color = Color.parseColor("#805977c6")

        mTextPaint = Paint()
        mTextPaint.isAntiAlias = true
        mTextPaint.color = Color.WHITE
        mTextPaint.textAlign = Paint.Align.CENTER

        mCenterPaint = Paint()
        mCenterPaint.isAntiAlias = true
        mCenterPaint.color = Color.WHITE
        mCenterPaint.textAlign = Paint.Align.CENTER

        mBgPaint = Paint()
        mBgPaint.isAntiAlias = true

        mArcPaint = Paint()
        mArcPaint.isAntiAlias = true
        mArcPaint.color = Color.parseColor("#46000000")

        mspratePaint = Paint()
        mspratePaint.isAntiAlias = true
        mspratePaint.style = Paint.Style.STROKE
        mspratePaint.strokeWidth = 3f
        mspratePaint.color = Color.parseColor("#4dd8d8d8")

        mCenterNormalBitmap =
            BitmapFactory.decodeResource(context.resources, R.mipmap.btn_normal_bg)
        mCenterPressBitmap =
            BitmapFactory.decodeResource(context.resources, R.mipmap.btn_press_bg)
        mBg = BitmapFactory.decodeResource(context.resources, R.mipmap.wheel_open_bg)

        mscrenH = context.resources.displayMetrics.heightPixels
        mScreenW = context.resources.displayMetrics.widthPixels
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val modeW = MeasureSpec.getMode(widthMeasureSpec)
        val sizeW = MeasureSpec.getSize(widthMeasureSpec)
        var widht = if (modeW == MeasureSpec.EXACTLY) {
            sizeW
        } else {
            GameConstant.baseWheelWidth.px(context)
        }
        mWRadius = widht / 2f
        mCenterRadiu = 3 * mWRadius / 7
        mArcRadiu = 142 * mWRadius / GameConstant.baseWheelWidth
        mRectOut.set(-mWRadius, -mWRadius, mWRadius, mWRadius)
        mRectIn.set(
            -mCenterRadiu * 2 / 3,
            -mCenterRadiu * 2 / 3,
            mCenterRadiu * 2 / 3,
            mCenterRadiu * 2 / 3
        )
        mArcRect.set(
            -mArcRadiu, -mArcRadiu, mArcRadiu, mArcRadiu
        )

        setMeasuredDimension(widht, widht)
        Log.i(TAG, "mWRadius=$mWRadius")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(mWRadius, mWRadius)
        mChangeAngel = 360f / mTextList.size
        if (mIsPress || mIsEditing) {
            canvas.drawBitmap(
                mBg,
                Rect(0, 0, mBg.width, mBg.height),
                RectF(-mWRadius, -mWRadius, mWRadius, mWRadius),
                mBgPaint
            )
            mRegionList.clear()
            for (i in mTextList.indices) {
                val startAngel = i * mChangeAngel
                val arcPath = getArcPath(mRectIn, mArcRect, startAngel, mChangeAngel)
                canvas.drawPath(arcPath, mArcPaint)
                drawText(
                    canvas,
                    startAngel + mChangeAngel / 2,
                    getHanlerChinese(mTextList[i]),
                    mTextPaint,
                    mArcRadiu,
                    mCenterRadiu * 2 / 3
                )
                mRegionList.add(getRegion(path = arcPath))
                drawLinePath(
                    canvas,
                    mArcRadiu,
                    mCenterRadiu * 2 / 3,
                    (startAngel + mChangeAngel).toDouble()
                )
                if (mLastArea == i) {
                    canvas.drawPath(arcPath, mSelectPaint)
                }
            }
        }
        mArcPaint.isAntiAlias = true
        if (mIsPress || mIsEditing || mPressAndDrag) {
            canvas.drawBitmap(
                mCenterPressBitmap,
                Rect(0, 0, mCenterPressBitmap.width, mCenterPressBitmap.height),
                RectF(-mCenterRadiu, -mCenterRadiu, mCenterRadiu, mCenterRadiu),
                mBgPaint
            )
        } else {
            canvas.drawBitmap(
                mCenterNormalBitmap,
                Rect(0, 0, mCenterNormalBitmap.width, mCenterNormalBitmap.height),
                RectF(-mCenterRadiu, -mCenterRadiu, mCenterRadiu, mCenterRadiu),
                mBgPaint
            )
        }
        val textSize = getTextSize(mCenterStr)
        mCenterPaint.textSize = textSize
        drawCenterText(canvas, mCenterPaint, mCenterStr, textSize)
    }

    /**
     * 包含中文就取前2个
     */
    private fun getHanlerChinese(txt: String): String {
        if (GameUtils.containChinese(txt) && txt.length > 2) {
            return txt.substring(0, 2)
        } else if (!GameUtils.containChinese(txt) && txt.length > 5) {
            return txt.substring(0, 5)
        }
        return txt
    }

    private fun setCenterPressStatu(isPress: Boolean) {
        mPressAndDrag = isPress
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mCanDrag) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (!isInCenter(event)) {
                        return super.onTouchEvent(event)
                    } else {
                        setCenterPressStatu(true)
                        mLastX = event.rawX.toInt()
                        mLastY = event.rawY.toInt()
                        mIsNeedPosChange = false
                    }
                    //setBackgroundResource(R.drawable.btn_press_bg)
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
                    mLastPos = -1
                    setCenterPressStatu(false)
                    //setBackgroundResource(R.drawable.btn_normal_bg)
                }
            }
        } else {
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    if (isInCenter(event) && !mIsEditing) {
                        checkShark()
                        showRect(true)
                    } else {
                        return super.onTouchEvent(event)
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    val inAreaPos = inAreaPos(event, mRegionList)
                    if (inAreaPos == -1) {
                        if (mLastArea > -1 && mLastArea < mMeanList.size) {
                            mOnWheelClickListener?.onWheelClickUp(mLastArea, mMeanList[mLastArea])
                            mLastArea = -1
                            showRect(true)
                        }
                    } else {
                        if (inAreaPos != mLastArea) {
                            checkShark()
                            if (mLastArea > -1 && mLastArea < mMeanList.size) {
                                mOnWheelClickListener?.onWheelClickUp(
                                    mLastArea,
                                    mMeanList[mLastArea]
                                )
                            }
                            if (inAreaPos < mMeanList.size) {
                                mOnWheelClickListener?.onWheelClickDown(
                                    inAreaPos,
                                    mMeanList[inAreaPos]
                                )
                            }
                            invalidate()
                            mLastArea = inAreaPos
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    clearData()
                    showRect(false)
                }
            }
        }
        return true
    }

    private fun checkShark() {
        if (!mIsEditing) {
            GameUtils.shark(context)
        }
    }

    /**
     * 是否手指范围在中心圆内
     */
    private fun isInCenter(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        Log.i(TAG, "x=$x  y=$y  mWRadius=$mWRadius  mCenterRadiu=$mCenterRadiu")
        return (x >= mWRadius - mCenterRadiu && x <= mCenterRadiu + mWRadius &&
                y >= mWRadius - mCenterRadiu && y <= mCenterRadiu + mWRadius)
    }


    /**
     * 扇形是否显示
     */
    private fun showRect(isShow: Boolean) {
        mIsPress = isShow
        invalidate()
    }

    //扇形画文字
    private fun drawText(
        mCanvas: Canvas,
        textAngle: Float,
        kinds: String,
        mPaint: Paint,
        mRadius: Float,
        mCenTer: Float
    ) {
        val rect = Rect()
        mPaint.textSize = GameUtils.sp2px(13f, context)
        mPaint.getTextBounds(kinds, 0, kinds.length, rect)
        var fontMetrics = mPaint.fontMetrics
        val dy = (fontMetrics.bottom - fontMetrics.top) / 2f - fontMetrics.bottom
        mPaint.getTextBounds(kinds, 0, kinds.length, rect)
        val pointEnd = PointF()
        val pointStart = PointF()
        val pointCengter = PointF()
        var x = 0.0
        var y = 0.0
        if (textAngle in 0.0..90.0) { //画布坐标系第一象限(数学坐标系第四象限)
            x = cos(Math.toRadians(textAngle.toDouble()))
            y = sin(Math.toRadians(textAngle.toDouble()))
        } else if (textAngle > 90 && textAngle <= 180) { //画布坐标系第二象限(数学坐标系第三象限)
            x = (-cos(Math.toRadians(180 - textAngle.toDouble())))
            y = (sin(Math.toRadians(180 - textAngle.toDouble())))
        } else if (textAngle > 180 && textAngle <= 270) { //画布坐标系第三象限(数学坐标系第二象限)
            x = (-cos(Math.toRadians(textAngle - 180.toDouble())))
            y = (-sin(Math.toRadians(textAngle - 180.toDouble())))
        } else { //画布坐标系第四象限(数学坐标系第一象限)
            x = (cos(Math.toRadians(360 - textAngle.toDouble())))
            y = (-sin(Math.toRadians(360 - textAngle.toDouble())))
        }
        pointStart.set((x * mCenTer).toFloat(), (y * mCenTer).toFloat())
        pointEnd.set((x * mRadius).toFloat(), (y * mRadius).toFloat())
        pointCengter.set((pointEnd.x + pointStart.x) / 2, (pointEnd.y + pointStart.y) / 2)

        mCanvas.drawText(
            kinds,
            pointCengter.x,
            pointCengter.y + dy,
            mPaint
        )
    }

    /**
     * 绘制弧度的间隔线条
     */
    private fun drawLinePath(
        canvas: Canvas,
        radius: Float,
        radiusN: Float,
        angele: Double
    ) {
        var linePath = Path()
        when (angele) {
            in 0.0f..90.0f -> {
                linePath.moveTo(
                    radiusN * cos(Math.toRadians(angele).toFloat()),
                    -radiusN * sin(Math.toRadians(angele).toFloat())
                )
                linePath.lineTo(
                    radius * cos(Math.toRadians(angele).toFloat()),
                    -radius * sin(Math.toRadians(angele).toFloat())
                )
            }
            in 90.0f..180f -> {
                linePath.moveTo(
                    -radiusN * sin(Math.toRadians(angele - 90f).toFloat()),
                    -radiusN * cos(Math.toRadians(angele - 90).toFloat())
                )
                linePath.lineTo(
                    -radius * sin(Math.toRadians(angele - 90).toFloat()),
                    -radius * cos(Math.toRadians(angele - 90).toFloat())
                )
            }
            in 180.0f..270f -> {
                linePath.moveTo(
                    -radiusN * cos(Math.toRadians(angele - 180).toFloat()),
                    radiusN * sin(Math.toRadians(angele - 180).toFloat())
                )
                linePath.lineTo(
                    -radius * cos(Math.toRadians(angele - 180).toFloat()),
                    radius * sin(Math.toRadians(angele - 180).toFloat())
                )
            }
            in 270.0f..360f -> {
                linePath.moveTo(
                    radiusN * sin(Math.toRadians(angele - 270).toFloat()),
                    radiusN * cos(Math.toRadians(angele - 270).toFloat())
                )
                linePath.lineTo(
                    radius * sin(Math.toRadians(angele - 270).toFloat()),
                    radius * cos(Math.toRadians(angele - 270).toFloat())
                )
            }
        }
        canvas.drawPath(linePath, mspratePaint)
    }


    /**
     * 获取绘制弧度所需要的path
     *
     * @param in
     * @param out
     * @param startAngle
     * @param angle
     * @return
     */
    private fun getArcPath(
        inSide: RectF,
        out: RectF,
        startAngle: Float,
        angle: Float
    ): Path {
        val path1 = Path()
        path1.moveTo(inSide.centerX(), inSide.centerY())
        path1.arcTo(inSide, startAngle, angle - 0.5f)
        val path2 = Path()
        path2.moveTo(out.centerX(), out.centerY())
        path2.arcTo(out, startAngle, angle - 0.5f)
        val path = Path()
        path.op(path2, path1, Path.Op.DIFFERENCE)
        return path
    }

    private fun getRegion(path: Path): Region {
        val re = Region()
        val rectF = RectF()
        path.computeBounds(rectF, true)
        re.setPath(
            path,
            Region(rectF.left.toInt(), rectF.top.toInt(), rectF.right.toInt(), rectF.bottom.toInt())
        )
        return re
    }

    /**
     * 判断扇形区域
     */
    private fun inAreaPos(event: MotionEvent, regions: MutableList<Region>): Int {
        val x: Float = event.x - mWRadius
        val y: Float = event.y - mWRadius
        for (i in mRegionList.indices) {
            if (regions[i].contains(x.toInt(), y.toInt())) {
                return i
            }
        }
        return -1
    }

    private fun drawCenterText(canvas: Canvas, paint: Paint, centerTitle: String, textSize: Float) {
        paint.color = Color.WHITE
        paint.textSize = textSize
        //val rect = Rect()
        //paint.getTextBounds(centerTitle, 0, centerTitle.length, rect)
        var fontMetrics = paint.fontMetrics
        val dy = (fontMetrics.bottom - fontMetrics.top) / 2f - fontMetrics.bottom

        canvas.drawText(
            centerTitle,
            0f,
            dy,
            paint
        )
    }

    private fun getTextSize(text: String): Float {
        return when {
            text.length >= 4 -> {
                GameUtils.sp2px(10f, context)
            }
            text.length == 3 -> {
                GameUtils.sp2px(13f, context)
            }
            text.length <= 2 -> {
                GameUtils.sp2px(15f, context)
            }
            else -> {
                GameUtils.sp2px(15f, context)
            }
        }
    }

    private fun clearData() {
        if (mLastArea != -1 && mMeanList.size > mLastArea) {
            mOnWheelClickListener?.onWheelClickUp(mLastArea, mMeanList[mLastArea])
        }
        mLastArea = -1
        mRegionList.clear()
    }

    companion object {
        const val TAG = "CirCleWheelView"
    }

    override fun setDragState(canDrag: Boolean) {
        mCanDrag = canDrag
    }

    override fun setDragClickListener(dragClickListener: IDragClickListener) {
        this.mClickListener = dragClickListener
    }

    override fun setDragPosChangeListener(dragPosListener: IDragPosChangeListener) {
        this.mPosChangeList = dragPosListener
    }

    fun changeCentextText(text: String) {
        mCenterStr = text
        invalidate()
    }
}