package com.github.youzhajun.audio.extractor;

import com.github.youzhajun.common.util.OutputDirectoryUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * 默认音频封面抽取器
 * <p>
 * 返回默认的音频封面图片
 * </p>
 */
@Slf4j
public class DefaultAudioCoverExtractor extends AbstractAudioCoverExtractor {

    private final String baseOutputDirectory;
    private final boolean useDateSubdirectory;
    private final String outputFormat;

    public DefaultAudioCoverExtractor(String baseOutputDirectory, boolean useDateSubdirectory, String outputFormat) {
        this.baseOutputDirectory = baseOutputDirectory;
        this.useDateSubdirectory = useDateSubdirectory;
        this.outputFormat = outputFormat;
    }

    @Override
    protected File doExtract(File sourceFile) {
        // 获取实际输出目录（支持日期子目录）
        String actualDirectory = OutputDirectoryUtil.getActualDirectory(baseOutputDirectory, useDateSubdirectory);
        
        // 返回默认音频封面图片
        // 这里可以根据需求返回一个默认的音频图标
        // 示例：从资源文件中复制默认图片
        
        String outputFileName = UUID.randomUUID() + "." + outputFormat;
        File outputFile = new File(actualDirectory, outputFileName);

        try {
            // 尝试从classpath加载默认音频图标
            InputStream defaultCoverStream = getClass().getResourceAsStream("/default-audio-cover.png");
            
            if (defaultCoverStream != null) {
                Files.copy(defaultCoverStream, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                defaultCoverStream.close();
                log.debug("使用默认音频封面: {}", outputFile.getAbsolutePath());
            } else {
                // 如果没有默认图片，创建一个空的占位文件
                // 实际使用时建议提供一个默认的音频图标图片
                outputFile.createNewFile();
                log.warn("未找到默认音频封面资源，创建空文件: {}", outputFile.getAbsolutePath());
            }
            
            return outputFile;
        } catch (IOException e) {
            log.error("创建默认音频封面失败", e);
            throw new RuntimeException("创建默认音频封面失败: " + e.getMessage(), e);
        }
    }
}

