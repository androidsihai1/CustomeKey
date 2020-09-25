package com.yy.myapplication.entity

/**
 * 按键增，删，改 ,合并，拆散,增加组合键 编辑组合键
 * Created by pengsihai@yy.com on 2019/12/15.
 */
enum class ButtonEntityNoticeType(var type: Int) {
    Add(0),
    Change(1),
    Delete(2),
    Merge(3),
    TearDown(4),
    AddMultKey(5),
    EditMultKey(6)
}