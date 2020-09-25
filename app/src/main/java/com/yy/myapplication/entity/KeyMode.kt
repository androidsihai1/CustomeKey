package com.yy.myapplication.entity

/**
 * @param mode 0:默认模式 1：自定义按键模式 2：隐藏模式 3按键编辑状态
 * Created by pengsihai@yy.com on 2019/12/5.
 */
enum class KeyMode(var mode: Int) {
    Default(0), CustomeKey(1), Hide(2), KeyEdit(3);

    companion object {
        open fun getMode(mode: Int): KeyMode {
            return when (mode) {
                Default.mode -> Default
                CustomeKey.mode -> CustomeKey
                Hide.mode -> Hide
                KeyEdit.mode -> KeyEdit
                else -> Default
            }
        }
    }
}