package com.github.youzhajun.autoconfigure.config;

import com.github.youzhajun.audio.extractor.DefaultAudioCoverExtractor;
import com.github.youzhajun.autoconfigure.service.MediaCoverExtractorService;
import com.github.youzhajun.common.extractor.CoverExtractor;
import com.github.youzhajun.common.properties.MediaCoverProperties;
import com.github.youzhajun.document.executor.LibreOfficeExecutor;
import com.github.youzhajun.document.executor.PdfBoxExecutor;
import com.github.youzhajun.document.extractor.DocumentCoverExtractor;
import com.github.youzhajun.ffmpeg.executor.FfmpegExecutor;
import com.github.youzhajun.ffmpeg.extractor.ImageCoverExtractor;
import com.github.youzhajun.ffmpeg.extractor.VideoCoverExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 媒体封面抽取自动配置类
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(MediaCoverProperties.class)
public class MediaCoverAutoConfiguration {

    private final MediaCoverProperties properties;

    public MediaCoverAutoConfiguration(MediaCoverProperties properties) {
        this.properties = properties;
        log.debug("媒体封面抽取启动器开始初始化...");
        validateConfiguration();
    }

    /**
     * 配置验证（启动时校验，仅打印错误日志，不阻断启动）
     */
    private void validateConfiguration() {
        // 验证FFmpeg配置
        if (properties.getFfmpeg().isEnabled()) {
            FfmpegExecutor ffmpegExecutor = new FfmpegExecutor(properties.getFfmpeg().getPath());
            if (!ffmpegExecutor.validate()) {
                log.error("FFmpeg配置错误：FFmpeg不可用，请检查配置项 media.cover.ffmpeg.path 或确保FFmpeg已加入系统环境变量");
                log.error("FFmpeg功能将不可用，调用时将抛出异常");
            } else {
                log.info("FFmpeg配置验证成功");
            }
        }

        // 验证LibreOffice配置
        if (properties.getLibreoffice().isEnabled()) {
            String libreOfficePath = properties.getLibreoffice().getPath();
            if (libreOfficePath == null || libreOfficePath.isEmpty()) {
                log.error("LibreOffice配置错误：未配置 media.cover.libreoffice.path，文档处理功能将不可用");
                log.error("文档处理功能将不可用，调用时将抛出异常");
            } else {
                // 不执行实际验证，避免Windows环境弹窗
                log.info("LibreOffice路径已配置: {}", libreOfficePath);
            }
        }
    }

    /**
     * FFmpeg执行器
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "com.github.youzhajun.ffmpeg.executor.FfmpegExecutor")
    @ConditionalOnProperty(prefix = "media.cover.ffmpeg", name = "enabled", havingValue = "true", matchIfMissing = true)
    public FfmpegExecutor ffmpegExecutor() {
        log.debug("创建FFmpeg执行器");
        return new FfmpegExecutor(properties.getFfmpeg().getPath());
    }

    /**
     * 视频封面抽取器
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "com.github.youzhajun.ffmpeg.extractor.VideoCoverExtractor")
    @ConditionalOnProperty(prefix = "media.cover.ffmpeg", name = "enabled", havingValue = "true", matchIfMissing = true)
    public VideoCoverExtractor videoCoverExtractor(FfmpegExecutor ffmpegExecutor) {
        log.debug("创建视频封面抽取器");
        return new VideoCoverExtractor(
                ffmpegExecutor,
                properties.getOutput().getDirectory(),
                properties.getOutput().isUseDateSubdirectory(),
                properties.getOutput().getFormat(),
                properties.getOutput().getStrategy(),
                properties.getOutput().getWidth(),
                properties.getOutput().getHeight()
        );
    }

    /**
     * 图片封面抽取器
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "com.github.youzhajun.ffmpeg.extractor.ImageCoverExtractor")
    @ConditionalOnProperty(prefix = "media.cover.ffmpeg", name = "enabled", havingValue = "true", matchIfMissing = true)
    public ImageCoverExtractor imageCoverExtractor(FfmpegExecutor ffmpegExecutor) {
        log.debug("创建图片封面抽取器");
        return new ImageCoverExtractor(
                ffmpegExecutor,
                properties.getOutput().getDirectory(),
                properties.getOutput().isUseDateSubdirectory(),
                properties.getOutput().getFormat(),
                properties.getOutput().getStrategy(),
                properties.getOutput().getWidth(),
                properties.getOutput().getHeight()
        );
    }

    /**
     * LibreOffice执行器
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "com.github.youzhajun.document.executor.LibreOfficeExecutor")
    @ConditionalOnProperty(prefix = "media.cover.libreoffice", name = "enabled", havingValue = "true", matchIfMissing = true)
    public LibreOfficeExecutor libreOfficeExecutor() {
        log.debug("创建LibreOffice执行器");
        String path = properties.getLibreoffice().getPath();
        if (path == null || path.isEmpty()) {
            log.warn("LibreOffice路径未配置，文档处理功能将不可用");
        }
        return new LibreOfficeExecutor(path);
    }

    /**
     * PDFBox执行器
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "com.github.youzhajun.document.executor.PdfBoxExecutor")
    @ConditionalOnProperty(prefix = "media.cover.libreoffice", name = "enabled", havingValue = "true", matchIfMissing = true)
    public PdfBoxExecutor pdfBoxExecutor() {
        log.debug("创建PDFBox执行器");
        return new PdfBoxExecutor();
    }

    /**
     * 文档封面抽取器
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "com.github.youzhajun.document.extractor.DocumentCoverExtractor")
    @ConditionalOnProperty(prefix = "media.cover.libreoffice", name = "enabled", havingValue = "true", matchIfMissing = true)
    public DocumentCoverExtractor documentCoverExtractor(LibreOfficeExecutor libreOfficeExecutor,
                                                         PdfBoxExecutor pdfBoxExecutor) {
        log.debug("创建文档封面抽取器");
        return new DocumentCoverExtractor(
                libreOfficeExecutor,
                pdfBoxExecutor,
                properties.getOutput().getDirectory(),
                properties.getOutput().isUseDateSubdirectory(),
                properties.getOutput().getFormat()
        );
    }

    /**
     * 默认音频封面抽取器
     */
    @Bean
    @ConditionalOnMissingBean(name = "audioCoverExtractor")
    @ConditionalOnClass(name = "com.github.youzhajun.audio.extractor.DefaultAudioCoverExtractor")
    public DefaultAudioCoverExtractor audioCoverExtractor() {
        log.debug("创建默认音频封面抽取器（用户可自定义实现替换）");
        return new DefaultAudioCoverExtractor(
                properties.getOutput().getDirectory(),
                properties.getOutput().isUseDateSubdirectory(),
                properties.getOutput().getFormat()
        );
    }

    /**
     * 媒体封面抽取服务（统一对外接口）
     */
    @Bean
    @ConditionalOnMissingBean
    public MediaCoverExtractorService mediaCoverExtractorService(List<CoverExtractor> extractors) {
        log.debug("创建媒体封面抽取服务");
        return new MediaCoverExtractorService(extractors);
    }
}

