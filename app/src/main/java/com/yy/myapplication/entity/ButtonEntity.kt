package com.yy.myapplication.entity

import androidx.annotation.Keep
import java.io.Serializable

/**
 * Created by pengsihai@yy.com on 2019/10/16.
 */
@Keep
data class ButtonEntity(
    var btnStyleType: Int = BtnStyleType.Medium.type,
    var pos: List<Float> = listOf(50f, 50f),
    var baseWidth: Float = 70f,
    var baseHeight: Float = 70f,
    var multiple: Float = 1.35f,
    var mean: MutableList<Int> = mutableListOf(),
    var pressType: Int = 0, //0为单击  1为长按  2状态模式 对应PressType
    var contType: Int = 1, //按钮内容类型，1：文字，2：图片
    var text: String? = null,
    var icon: String? = null,
    var id: Long = 0L, //自己加的id,区分内容一样的情况下
    //btnStyle = 9新轮盘键使用(子集存放)
    var btns: MutableList<ButtonEntity> = mutableListOf()
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (other is ButtonEntity) {
            if (other.text == text && other.icon == icon && other.btnStyleType == btnStyleType &&
                other.mean == mean && other.contType == contType && other.id == id
            ) {
                return true
            }
        }
        return super.equals(other)
    }
}