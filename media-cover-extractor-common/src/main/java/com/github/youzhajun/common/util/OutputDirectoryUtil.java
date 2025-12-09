package com.github.youzhajun.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 输出目录工具类
 */
@Slf4j
public class OutputDirectoryUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    /**
     * 获取实际输出目录
     * 
     * @param baseDirectory 基础目录
     * @param useDateSubdirectory 是否使用日期子目录
     * @return 实际输出目录
     */
    public static String getActualDirectory(String baseDirectory, boolean useDateSubdirectory) {
        if (!useDateSubdirectory) {
            return baseDirectory;
        }

        // 生成日期子目录：年/月/日
        String datePath = LocalDate.now().format(DATE_FORMATTER);
        String actualDirectory = baseDirectory + File.separator + datePath;

        // 确保目录存在
        File dir = new File(actualDirectory);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                log.debug("创建日期子目录: {}", actualDirectory);
            } else {
                log.warn("创建日期子目录失败: {}", actualDirectory);
                // 如果创建失败，返回基础目录
                return baseDirectory;
            }
        }

        return actualDirectory;
    }

    /**
     * 获取实际输出目录（File对象）
     * 
     * @param baseDirectory 基础目录
     * @param useDateSubdirectory 是否使用日期子目录
     * @return 实际输出目录File对象
     */
    public static File getActualDirectoryFile(String baseDirectory, boolean useDateSubdirectory) {
        String actualDirectory = getActualDirectory(baseDirectory, useDateSubdirectory);
        return new File(actualDirectory);
    }
}

