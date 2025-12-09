package com.github.youzhajun.common.extractor;

import com.github.youzhajun.common.enums.FileType;

import java.io.File;

/**
 * 封面抽取器接口
 */
public interface CoverExtractor {

    /**
     * 抽取封面
     * @param sourceFile 源文件
     * @return 封面文件
     */
    File extract(File sourceFile);

    /**
     * 判断是否支持该文件类型
     * @param fileType 文件类型
     * @return 是否支持
     */
    boolean support(FileType fileType);

    /**
     * 获取抽取器优先级（数值越小优先级越高）
     * @return 优先级
     */
    default int getOrder() {
        return Integer.MAX_VALUE;
    }
}

