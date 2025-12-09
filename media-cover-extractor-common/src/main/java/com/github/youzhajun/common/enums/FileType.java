package com.github.youzhajun.common.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 文件类型枚举
 */
@Getter
public enum FileType {

    // 视频类型
    VIDEO("video", new String[]{"mp4", "avi", "mov", "wmv", "flv", "mkv", "webm", "m4v", "rmvb", "3gp"}),

    // 图片类型
    IMAGE("image", new String[]{"jpg", "jpeg", "png", "gif", "bmp", "webp", "svg", "tiff", "ico"}),

    // 文档类型
    DOCUMENT("document", new String[]{"doc", "docx", "ppt", "pptx", "xls", "xlsx", "txt", "pdf", "pps"}),

    // 音频类型
    AUDIO("audio", new String[]{"mp3", "wav", "flac", "aac", "ogg", "wma", "m4a", "ape"}),

    // 未知类型
    UNKNOWN("unknown", new String[]{});

    private final String type;
    private final String[] extensions;

    FileType(String type, String[] extensions) {
        this.type = type;
        this.extensions = extensions;
    }

    /**
     * 根据文件扩展名判断文件类型
     */
    public static FileType fromExtension(String extension) {
        if (extension == null || extension.isEmpty()) {
            return UNKNOWN;
        }
        String ext = extension.toLowerCase().trim();
        if (ext.startsWith(".")) {
            ext = ext.substring(1);
        }
        
        for (FileType fileType : values()) {
            if (Arrays.asList(fileType.extensions).contains(ext)) {
                return fileType;
            }
        }
        return UNKNOWN;
    }

    /**
     * 根据文件名判断文件类型
     */
    public static FileType fromFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return UNKNOWN;
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return UNKNOWN;
        }
        String extension = fileName.substring(lastDotIndex + 1);
        return fromExtension(extension);
    }
}

