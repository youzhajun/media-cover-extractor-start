package com.github.youzhajun.common.enums;

/**
 * 图片处理策略枚举
 */
public enum ImageProcessStrategy {

    /**
     * 等比例缩放（保持宽高比）
     */
    SCALE,

    /**
     * 强制裁剪（按指定宽高裁剪）
     */
    CROP,

    /**
     * 不处理
     */
    NONE
}

