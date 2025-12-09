package com.github.youzhajun.document.executor;

import com.github.youzhajun.common.exception.ExtractException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * LibreOffice命令执行器
 */
@Slf4j
public class LibreOfficeExecutor {

    private final String libreOfficePath;

    public LibreOfficeExecutor(String libreOfficePath) {
        this.libreOfficePath = libreOfficePath;
    }

    /**
     * 将文档转换为PDF
     * @param sourceFile 源文件
     * @param outputDir 输出目录
     * @return PDF文件
     */
    public File convertToPdf(File sourceFile, String outputDir) {
        if (!sourceFile.exists() || !sourceFile.isFile()) {
            throw new ExtractException("源文件不存在或不是文件: " + sourceFile.getAbsolutePath());
        }

        try {
            // 构建LibreOffice命令
            CommandLine commandLine = new CommandLine(libreOfficePath);
            commandLine.addArgument("--headless");
            commandLine.addArgument("--convert-to");
            commandLine.addArgument("pdf");
            commandLine.addArgument("--outdir");
            commandLine.addArgument(outputDir);
            commandLine.addArgument(sourceFile.getAbsolutePath());

            DefaultExecutor executor = new DefaultExecutor();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
            executor.setStreamHandler(streamHandler);

            log.debug("执行LibreOffice命令: {}", commandLine);
            int exitCode = executor.execute(commandLine);
            
            if (exitCode != 0) {
                String error = errorStream.toString();
                log.error("LibreOffice转换失败，退出码: {}, 错误信息: {}", exitCode, error);
                throw new ExtractException("LibreOffice转换失败: " + error);
            }

            // 获取生成的PDF文件
            String pdfFileName = getFileNameWithoutExtension(sourceFile.getName()) + ".pdf";
            File pdfFile = new File(outputDir, pdfFileName);
            
            if (!pdfFile.exists()) {
                throw new ExtractException("PDF文件生成失败: " + pdfFileName);
            }

            log.debug("文档转换成功: {} -> {}", sourceFile.getName(), pdfFile.getAbsolutePath());
            return pdfFile;
        } catch (IOException e) {
            log.error("LibreOffice执行异常", e);
            throw new ExtractException("LibreOffice执行异常: " + e.getMessage(), e);
        }
    }

    /**
     * 验证LibreOffice是否可用
     */
    public boolean validate() {
        try {
            CommandLine commandLine = new CommandLine(libreOfficePath);
            commandLine.addArgument("--version");
            
            DefaultExecutor executor = new DefaultExecutor();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
            executor.setStreamHandler(streamHandler);
            
            int exitCode = executor.execute(commandLine);
            return exitCode == 0;
        } catch (Exception e) {
            log.error("LibreOffice验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取不含扩展名的文件名
     */
    private String getFileNameWithoutExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(0, lastDotIndex);
        }
        return fileName;
    }
}

