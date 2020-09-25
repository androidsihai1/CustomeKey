package com.yy.myapplication.entity

/**
 * 0点击 1长按  2状态模式　
 * Created by pengsihai@yy.com on 2019/10/31.
 */
enum class PressType(var pressType: Int) {
    Click(0), LongPress(1), StatePress(2)
}