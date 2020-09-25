package com.yy.myapplication.inter

import com.yy.mobile.plugin.dreamerchannel.ui.cloudgame.map.inter.IDragClickListener
import com.yy.mobile.plugin.dreamerchannel.ui.cloudgame.map.inter.IDragPosChangeListener

/**
 * Created by pengsihai@yy.com on 2019/12/17.
 */
interface IDragView {
    fun setDragState(canDrag: Boolean)
    fun setDragClickListener(dragClickListener: IDragClickListener)
    fun setDragPosChangeListener(dragPosListener: IDragPosChangeListener)
}