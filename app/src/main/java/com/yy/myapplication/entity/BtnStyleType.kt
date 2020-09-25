package com.yy.myapplication.entity

/**
 * 1小按钮 2中按钮 3大按钮 4摇杆方向盘 5鼠标左右 6箭头方向盘 7轮盘 8组合键 9新轮盘键（支持组合键，后续7被弃用）
 * Created by pengsihai@yy.com on 2019/10/9.
 */
enum class BtnStyleType(var type: Int) {
    Small(1), Medium(2), Big(3), Rocket(4), MouseType(5),
    RocketDirection(6),
    Wheel(7), MultKey(8),
}