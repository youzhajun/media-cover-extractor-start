package com.github.youzhajun.document.extractor;

import com.github.youzhajun.common.enums.FileType;
import com.github.youzhajun.common.exception.ExtractException;
import com.github.youzhajun.common.extractor.CoverExtractor;
import com.github.youzhajun.common.util.OutputDirectoryUtil;
import com.github.youzhajun.document.executor.LibreOfficeExecutor;
import com.github.youzhajun.document.executor.PdfBoxExecutor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.UUID;

/**
 * 文档封面抽取器
 */
@Slf4j
public class DocumentCoverExtractor implements CoverExtractor {

    private final LibreOfficeExecutor libreOfficeExecutor;
    private final PdfBoxExecutor pdfBoxExecutor;
    private final String baseOutputDirectory;
    private final boolean useDateSubdirectory;
    private final String outputFormat;

    public DocumentCoverExtractor(LibreOfficeExecutor libreOfficeExecutor,
                                  PdfBoxExecutor pdfBoxExecutor,
                                  String baseOutputDirectory,
                                  boolean useDateSubdirectory,
                                  String outputFormat) {
        this.libreOfficeExecutor = libreOfficeExecutor;
        this.pdfBoxExecutor = pdfBoxExecutor;
        this.baseOutputDirectory = baseOutputDirectory;
        this.useDateSubdirectory = useDateSubdirectory;
        this.outputFormat = outputFormat;
    }

    @Override
    public File extract(File sourceFile) {
        if (!sourceFile.exists() || !sourceFile.isFile()) {
            throw new ExtractException("源文件不存在或不是文件: " + sourceFile.getAbsolutePath());
        }

        File pdfFile = null;
        boolean isPdfConverted = false;

        try {
            // 获取实际输出目录（支持日期子目录）
            String actualDirectory = OutputDirectoryUtil.getActualDirectory(baseOutputDirectory, useDateSubdirectory);

            // 判断是否为PDF文件
            String extension = getFileExtension(sourceFile.getName());
            if ("pdf".equalsIgnoreCase(extension)) {
                pdfFile = sourceFile;
            } else {
                // 非PDF文档，先转换为PDF（临时文件也放在实际目录中）
                pdfFile = libreOfficeExecutor.convertToPdf(sourceFile, actualDirectory);
                isPdfConverted = true;
            }

            // 从PDF提取第一页作为封面
            String outputFileName = UUID.randomUUID() + "." + outputFormat;
            File outputFile = new File(actualDirectory, outputFileName);
            pdfBoxExecutor.extractFirstPage(pdfFile, outputFile, outputFormat);

            log.info("文档封面抽取成功: {} -> {}", sourceFile.getName(), outputFile.getAbsolutePath());
            return outputFile;
        } catch (Exception e) {
            throw new ExtractException("文档封面抽取失败: " + e.getMessage(), e);
        } finally {
            // 清理临时PDF文件
            if (isPdfConverted && pdfFile != null && pdfFile.exists()) {
                boolean deleted = pdfFile.delete();
                if (deleted) {
                    log.debug("临时PDF文件已删除: {}", pdfFile.getAbsolutePath());
                }
            }
        }
    }

    @Override
    public boolean support(FileType fileType) {
        return FileType.DOCUMENT.equals(fileType);
    }

    @Override
    public int getOrder() {
        return 300;
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }
}

