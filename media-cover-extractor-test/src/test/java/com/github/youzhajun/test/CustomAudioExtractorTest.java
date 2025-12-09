package com.github.youzhajun.test;

import com.github.youzhajun.audio.extractor.AbstractAudioCoverExtractor;
import com.github.youzhajun.common.enums.FileType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 自定义音频封面抽取器测试
 */
@Slf4j
class CustomAudioExtractorTest {

    /**
     * 自定义音频封面抽取器示例
     */
    static class CustomAudioCoverExtractor extends AbstractAudioCoverExtractor {
        
        private final String outputDirectory;

        public CustomAudioCoverExtractor(String outputDirectory) {
            this.outputDirectory = outputDirectory;
        }

        @Override
        protected File doExtract(File sourceFile) {
            // 自定义实现：返回一个自定义的音频封面
            log.info("使用自定义音频封面抽取器处理: {}", sourceFile.getName());
            
            try {
                File customCover = new File(outputDirectory, "custom-audio-cover.jpg");
                if (!customCover.exists()) {
                    customCover.createNewFile();
                }
                return customCover;
            } catch (IOException e) {
                throw new RuntimeException("创建自定义音频封面失败", e);
            }
        }

        @Override
        public int getOrder() {
            return 350;  // 优先级高于默认实现
        }
    }

    @Test
    void testCustomAudioExtractor() {
        String outputDir = System.getProperty("java.io.tmpdir");
        CustomAudioCoverExtractor extractor = new CustomAudioCoverExtractor(outputDir);

        // 测试支持的文件类型
        assertTrue(extractor.support(FileType.AUDIO), "应该支持音频文件类型");
        assertFalse(extractor.support(FileType.VIDEO), "不应该支持视频文件类型");

        // 测试优先级
        assertEquals(350, extractor.getOrder(), "优先级应该是350");

        log.info("自定义音频抽取器测试通过");
    }

    @Test
    void testAbstractAudioExtractorExtension() {
        // 演示如何继承AbstractAudioCoverExtractor
        AbstractAudioCoverExtractor customExtractor = new AbstractAudioCoverExtractor() {
            @Override
            protected File doExtract(File sourceFile) {
                log.info("这是一个匿名自定义音频抽取器");
                return new File(System.getProperty("java.io.tmpdir"), "temp-cover.jpg");
            }
        };

        assertNotNull(customExtractor);
        assertTrue(customExtractor.support(FileType.AUDIO));
        log.info("抽象音频抽取器扩展测试通过");
    }
}

