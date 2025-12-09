package com.github.youzhajun.audio.extractor;

import com.github.youzhajun.common.enums.FileType;
import com.github.youzhajun.common.exception.ExtractException;
import com.github.youzhajun.common.extractor.CoverExtractor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 音频封面抽取器抽象类
 * <p>
 * 用户可以继承此类，自定义实现音频封面处理逻辑
 * 例如：返回默认图片、生成自定义音频封面等
 * </p>
 */
@Slf4j
public abstract class AbstractAudioCoverExtractor implements CoverExtractor {

    @Override
    public File extract(File sourceFile) {
        if (!sourceFile.exists() || !sourceFile.isFile()) {
            throw new ExtractException("源文件不存在或不是文件: " + sourceFile.getAbsolutePath());
        }

        log.info("开始处理音频文件: {}", sourceFile.getName());
        
        try {
            File coverFile = doExtract(sourceFile);
            
            if (coverFile == null || !coverFile.exists()) {
                throw new ExtractException("音频封面抽取失败，未生成封面文件");
            }
            
            log.info("音频封面处理成功: {} -> {}", sourceFile.getName(), coverFile.getAbsolutePath());
            return coverFile;
        } catch (Exception e) {
            log.error("音频封面处理失败: {}", e.getMessage(), e);
            throw new ExtractException("音频封面处理失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean support(FileType fileType) {
        return FileType.AUDIO.equals(fileType);
    }

    @Override
    public int getOrder() {
        return 400;
    }

    /**
     * 子类实现具体的音频封面抽取逻辑
     * 
     * @param sourceFile 源音频文件
     * @return 封面文件
     */
    protected abstract File doExtract(File sourceFile);

    /**
     * 获取文件扩展名
     */
    protected String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }
}

