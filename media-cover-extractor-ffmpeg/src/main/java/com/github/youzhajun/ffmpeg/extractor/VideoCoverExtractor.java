package com.github.youzhajun.ffmpeg.extractor;

import com.github.youzhajun.common.enums.FileType;
import com.github.youzhajun.common.enums.ImageProcessStrategy;
import com.github.youzhajun.common.exception.ExtractException;
import com.github.youzhajun.common.extractor.CoverExtractor;
import com.github.youzhajun.common.util.OutputDirectoryUtil;
import com.github.youzhajun.ffmpeg.executor.FfmpegExecutor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 视频封面抽取器
 */
@Slf4j
public class VideoCoverExtractor implements CoverExtractor {

    private final FfmpegExecutor ffmpegExecutor;
    private final String baseOutputDirectory;
    private final boolean useDateSubdirectory;
    private final String outputFormat;
    private final ImageProcessStrategy strategy;
    private final Integer width;
    private final Integer height;

    public VideoCoverExtractor(FfmpegExecutor ffmpegExecutor,
                               String baseOutputDirectory,
                               boolean useDateSubdirectory,
                               String outputFormat,
                               ImageProcessStrategy strategy,
                               Integer width,
                               Integer height) {
        this.ffmpegExecutor = ffmpegExecutor;
        this.baseOutputDirectory = baseOutputDirectory;
        this.useDateSubdirectory = useDateSubdirectory;
        this.outputFormat = outputFormat;
        this.strategy = strategy;
        this.width = width;
        this.height = height;
    }

    @Override
    public File extract(File sourceFile) {
        if (!sourceFile.exists() || !sourceFile.isFile()) {
            throw new ExtractException("源文件不存在或不是文件: " + sourceFile.getAbsolutePath());
        }

        // 获取实际输出目录（支持日期子目录）
        String actualDirectory = OutputDirectoryUtil.getActualDirectory(baseOutputDirectory, useDateSubdirectory);
        
        // 生成输出文件
        String outputFileName = UUID.randomUUID() + "." + outputFormat;
        File outputFile = new File(actualDirectory, outputFileName);

        try {
            // 构建FFmpeg命令
            List<String> args = new ArrayList<>();
            args.add("-i");
            args.add(sourceFile.getAbsolutePath());
            args.add("-ss");
            args.add("00:00:01"); // 从第1秒截取
            args.add("-vframes");
            args.add("1"); // 只截取1帧

            // 添加图片处理参数
            if (strategy != ImageProcessStrategy.NONE && width != null && height != null) {
                String filter = buildScaleFilter(strategy, width, height);
                args.add("-vf");
                args.add(filter);
            }

            args.add("-y"); // 覆盖已存在的文件
            args.add(outputFile.getAbsolutePath());

            // 执行FFmpeg命令
            ffmpegExecutor.execute(args.toArray(new String[0]));

            if (!outputFile.exists()) {
                throw new ExtractException("视频封面抽取失败，输出文件未生成");
            }

            log.info("视频封面抽取成功: {} -> {}", sourceFile.getName(), outputFile.getAbsolutePath());
            return outputFile;
        } catch (Exception e) {
            // 清理可能生成的文件
            if (outputFile.exists()) {
                outputFile.delete();
            }
            throw new ExtractException("视频封面抽取失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean support(FileType fileType) {
        return FileType.VIDEO.equals(fileType);
    }

    @Override
    public int getOrder() {
        return 100;
    }

    /**
     * 构建FFmpeg缩放过滤器
     */
    private String buildScaleFilter(ImageProcessStrategy strategy, int width, int height) {
        return switch (strategy) {
            case SCALE -> String.format("scale=%d:%d:force_original_aspect_ratio=decrease", width, height);
            case CROP -> String.format("scale=%d:%d", width, height);
            default -> "";
        };
    }
}

