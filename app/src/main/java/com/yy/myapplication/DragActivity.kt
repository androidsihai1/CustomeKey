package com.yy.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yy.myapplication.entity.BtnStyleType
import com.yy.myapplication.entity.ButtonEntity
import com.yy.myapplication.entity.ButtonEntityChangeEvent
import com.yy.myapplication.entity.ButtonEntityNoticeType
import com.yy.myapplication.utils.KeyCreateCustome
import com.yy.myapplication.utils.KeyCreateUtils
import com.yy.myapplication.utils.RxBus
import kotlinx.android.synthetic.main.drag_activity.*

/**
 * 按键生成，移除，缩放，拖拽
 * Created by andy on 2020/9/13.
 */
class DragActivity : AppCompatActivity() {

    companion object {
        const val TAG = "DragActivity"
    }

    var buttonEntity: ButtonEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drag_activity)
        initData()
        initListener()
    }

    private fun initData() {
        KeyCreateUtils.mViewList.clear()
        KeyCreateUtils.mNumMarkViewList.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        KeyCreateUtils.mViewList.clear()
        KeyCreateUtils.mNumMarkViewList.clear()
    }

    private fun initListener() {
        btn_finsih?.setOnClickListener {
            finish()
        }
        btn_add?.setOnClickListener {
            if (KeyCreateUtils.mViewList.size >= 1) {
                Toast.makeText(it.context, "暂时只支持添加一个控件,后续期待 ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            buttonEntity = ButtonEntity()
            buttonEntity?.btnStyleType = BtnStyleType.Big.type
            buttonEntity?.multiple = 1f
            buttonEntity?.text = "你好"
            KeyCreateUtils.addSingleView(buttonEntity!!, it.context, root_view, false)
        }
        btn_remove?.setOnClickListener {
            KeyCreateUtils.mViewList.forEach {
                root_view?.removeView(it)
            }
            KeyCreateUtils.mViewList.clear()
        }
        progress_bar?.max = 10
        progress_bar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    buttonEntity?.let {
                        val last = (progress / 10f) + 1
                        Log.d(TAG, "last=$last progress=$progress")
                        it.multiple = last
                        if (KeyCreateUtils.mViewList.size > 0) {
                            val view = KeyCreateUtils.mViewList[0]
                            KeyCreateCustome.changeView(it, root_view, seekBar.context, false)
                        }
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        val subscribe =
            RxBus.getInstance().register(ButtonEntityChangeEvent::class.java).subscribe {
                Log.d(TAG, "ButtonEntityChangeEvent= $it")
                when (it.buttonEntityNoticeType) {
                    ButtonEntityNoticeType.Change -> {
                        val change = it.buttonEntity
                        buttonEntity?.let {
                            it.pos = change.pos
                            it.multiple = change.multiple
                            KeyCreateCustome.changeView(
                                it,
                                root_view,
                                root_view.context,
                                false
                            )
                        }
                    }
                }
            }
    }

}