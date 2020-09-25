package com.yy.myapplication.inter

/**
 * Created by pengsihai@yy.com on 2020/5/18.
 */
interface OnWheelClickListener {
    fun onWheelClickDown(pos: Int, mean: List<Int>)
    fun onWheelClickUp(pos: Int, mean: List<Int>)
}