package com.github.youzhajun.autoconfigure.service;

import com.github.youzhajun.common.enums.FileType;
import com.github.youzhajun.common.exception.ExtractException;
import com.github.youzhajun.common.extractor.CoverExtractor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Comparator;
import java.util.List;

/**
 * 媒体封面抽取服务（统一对外接口）
 */
@Slf4j
public class MediaCoverExtractorService {

    private final List<CoverExtractor> extractors;

    public MediaCoverExtractorService(List<CoverExtractor> extractors) {
        // 按优先级排序
        this.extractors = extractors.stream()
                .sorted(Comparator.comparingInt(CoverExtractor::getOrder))
                .toList();
    }

    /**
     * 抽取封面（统一对外接口）
     * 
     * @param sourceFilePath 源文件路径（绝对路径）
     * @return 封面文件
     */
    public File extractCover(String sourceFilePath) {
        if (sourceFilePath == null || sourceFilePath.isEmpty()) {
            throw new ExtractException("源文件路径不能为空");
        }

        File sourceFile = new File(sourceFilePath);
        return extractCover(sourceFile);
    }

    /**
     * 抽取封面（统一对外接口）
     * 
     * @param sourceFile 源文件
     * @return 封面文件
     */
    public File extractCover(File sourceFile) {
        if (sourceFile == null) {
            throw new ExtractException("源文件不能为null");
        }

        if (!sourceFile.exists()) {
            throw new ExtractException("源文件不存在: " + sourceFile.getAbsolutePath());
        }

        if (!sourceFile.isFile()) {
            throw new ExtractException("源文件不是一个文件: " + sourceFile.getAbsolutePath());
        }

        // 判断文件类型
        FileType fileType = FileType.fromFileName(sourceFile.getName());
        log.debug("检测文件类型: {} -> {}", sourceFile.getName(), fileType);

        if (fileType == FileType.UNKNOWN) {
            throw new ExtractException("不支持的文件类型: " + sourceFile.getName());
        }

        // 查找支持该文件类型的抽取器
        for (CoverExtractor extractor : extractors) {
            if (extractor.support(fileType)) {
                log.info("使用抽取器: {} 处理文件: {}", 
                        extractor.getClass().getSimpleName(), sourceFile.getName());
                return extractor.extract(sourceFile);
            }
        }

        throw new ExtractException("没有找到支持该文件类型的抽取器: " + fileType);
    }

    /**
     * 获取已加载的抽取器列表
     */
    public List<CoverExtractor> getExtractors() {
        return extractors;
    }
}

