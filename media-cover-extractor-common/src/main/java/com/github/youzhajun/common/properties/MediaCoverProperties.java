package com.github.youzhajun.common.properties;

import com.github.youzhajun.common.enums.ImageProcessStrategy;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 媒体封面抽取配置属性
 */
@Data
@ConfigurationProperties(prefix = "media.cover")
public class MediaCoverProperties {

    /**
     * FFmpeg配置
     */
    private FfmpegConfig ffmpeg = new FfmpegConfig();

    /**
     * LibreOffice配置
     */
    private LibreOfficeConfig libreoffice = new LibreOfficeConfig();

    /**
     * 输出配置
     */
    private OutputConfig output = new OutputConfig();

    @Data
    public static class FfmpegConfig {
        /**
         * FFmpeg环境路径（可选，不配置则使用系统环境变量）
         */
        private String path;

        /**
         * 是否启用FFmpeg功能
         */
        private boolean enabled = true;
    }

    @Data
    public static class LibreOfficeConfig {
        /**
         * LibreOffice安装路径（必填）
         */
        private String path;

        /**
         * 是否启用文档处理功能
         */
        private boolean enabled = true;
    }

    @Data
    public static class OutputConfig {
        /**
         * 展示图输出格式（默认jpg）
         */
        private String format = "jpg";

        /**
         * 图片处理策略（默认不处理）
         */
        private ImageProcessStrategy strategy = ImageProcessStrategy.NONE;

        /**
         * 图片宽度（当strategy为SCALE或CROP时有效）
         */
        private Integer width;

        /**
         * 图片高度（当strategy为SCALE或CROP时有效）
         */
        private Integer height;

        /**
         * 图片默认输出目录（默认为系统临时目录）
         */
        private String directory = System.getProperty("java.io.tmpdir");

        /**
         * 是否启用日期子目录（年/月/日），默认false
         * 启用后会在配置的目录下创建年/月/日层级，避免单目录文件过多
         * 例如：配置目录为 /tmp/covers，启用后实际目录为 /tmp/covers/2024/01/15/
         */
        private boolean useDateSubdirectory = false;
    }
}

