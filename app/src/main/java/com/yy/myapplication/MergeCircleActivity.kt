package com.yy.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yy.myapplication.entity.ButtonEntity
import com.yy.myapplication.entity.ButtonEntityChangeEvent
import com.yy.myapplication.entity.ButtonEntityNoticeType
import com.yy.myapplication.utils.KeyCreateUtils
import com.yy.myapplication.utils.RxBus
import kotlinx.android.synthetic.main.merge_circle_activity.*

/**
 * 轮盘合并和拆分
 * Created by andy on 2020/9/25.
 */
class MergeCircleActivity : AppCompatActivity() {
    lateinit var mButtonEntity: ButtonEntity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.merge_circle_activity)
        initData()
        initListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        KeyCreateUtils.mNumMarkViewList.clear()
        KeyCreateUtils.mViewList.clear()
    }

    private fun initData() {
        val testData = getTestData()
        for (i in testData.indices) {
            val btn = testData[i]
            KeyCreateUtils.addSingleView(btn, baseContext, root_view, true)
        }
    }

    private fun initListener() {
        btn_finsih?.setOnClickListener {
            finish()
        }
        btn_merge_circle_view?.setOnClickListener {
            if (KeyCreateUtils.mViewList.size == 1) {
                Toast.makeText(baseContext, "已经合并了", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val mergeButtonEntity = KeyCreateUtils.getMergeButtonEntity()
            KeyCreateUtils.deleteViewAfterMerge(root_view)
            RxBus.getInstance().post(
                ButtonEntityChangeEvent(
                    mergeButtonEntity,
                    ButtonEntityNoticeType.Merge
                )
            )
        }

        btn_charge_circle_view?.setOnClickListener {
            if (KeyCreateUtils.mViewList.size == 8) {
                Toast.makeText(baseContext, "已经拆分了", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mButtonEntity?.let {
                RxBus.getInstance()
                    .post(ButtonEntityChangeEvent(it, ButtonEntityNoticeType.TearDown))
            }
        }
        val subscribe =
            RxBus.getInstance().register(ButtonEntityChangeEvent::class.java).subscribe {
                when (it.buttonEntityNoticeType) {
                    ButtonEntityNoticeType.Merge -> {
                        mButtonEntity = it.buttonEntity
                        KeyCreateUtils.addSingleView(it.buttonEntity, baseContext, root_view, false)
                    }

                    ButtonEntityNoticeType.TearDown -> {
                        mButtonEntity?.let {
                            KeyCreateUtils.deleteView(root_view)
                            KeyCreateUtils.mViewList.clear()
                            KeyCreateUtils.mNumMarkViewList.clear()
                            val tearDownWheel = KeyCreateUtils.tearDownWheel(it) //获取拆解值
                            KeyCreateUtils.buildTearDown(tearDownWheel, baseContext, root_view)
                        }
                    }
                }
            }
    }

    private fun getTestData(): MutableList<ButtonEntity> {
        val mutableList = mutableListOf<ButtonEntity>()
        for (i in 0..7) {
            val baseTearEntity = KeyCreateUtils.getBaseTearEntity()
            baseTearEntity.text = "按键$i"
            baseTearEntity.id = baseTearEntity.id + i
            baseTearEntity.pos = KeyCreateUtils.getTearDownPos(i, 8)
            mutableList.add(baseTearEntity)
        }
        return mutableList
    }
}