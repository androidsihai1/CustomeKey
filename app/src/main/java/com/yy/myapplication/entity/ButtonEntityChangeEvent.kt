package com.yy.myapplication.entity

import androidx.annotation.Keep

/**
 * Created by pengsihai@yy.com on 2019/12/15.
 */
@Keep
data class ButtonEntityChangeEvent(
    var buttonEntity: ButtonEntity,
    var buttonEntityNoticeType: ButtonEntityNoticeType,
    var needFlushUi: Boolean = true
)