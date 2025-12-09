package com.github.youzhajun.ffmpeg.executor;

import com.github.youzhajun.common.exception.ExtractException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * FFmpeg命令执行器
 */
@Slf4j
public class FfmpegExecutor {

    private final String ffmpegPath;

    public FfmpegExecutor(String ffmpegPath) {
        this.ffmpegPath = ffmpegPath != null ? ffmpegPath : "ffmpeg";
    }

    /**
     * 执行FFmpeg命令
     */
    public void execute(String... args) {
        CommandLine commandLine = new CommandLine(ffmpegPath);
        for (String arg : args) {
            commandLine.addArgument(arg);
        }

        DefaultExecutor executor = new DefaultExecutor();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
        executor.setStreamHandler(streamHandler);

        try {
            log.debug("执行FFmpeg命令: {}", commandLine);
            int exitCode = executor.execute(commandLine);
            if (exitCode != 0) {
                String error = errorStream.toString();
                log.error("FFmpeg执行失败，退出码: {}, 错误信息: {}", exitCode, error);
                throw new ExtractException("FFmpeg执行失败: " + error);
            }
            log.debug("FFmpeg执行成功");
        } catch (IOException e) {
            log.error("FFmpeg执行异常", e);
            throw new ExtractException("FFmpeg执行异常: " + e.getMessage(), e);
        }
    }

    /**
     * 验证FFmpeg是否可用
     */
    public boolean validate() {
        try {
            CommandLine commandLine = new CommandLine(ffmpegPath);
            commandLine.addArgument("-version");
            
            DefaultExecutor executor = new DefaultExecutor();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
            executor.setStreamHandler(streamHandler);
            
            int exitCode = executor.execute(commandLine);
            return exitCode == 0;
        } catch (Exception e) {
            log.error("FFmpeg验证失败: {}", e.getMessage());
            return false;
        }
    }
}

