package com.github.youzhajun.test;

import com.github.youzhajun.autoconfigure.service.MediaCoverExtractorService;
import com.github.youzhajun.common.exception.ExtractException;
import com.github.youzhajun.common.extractor.CoverExtractor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 媒体封面抽取服务集成测试
 */
@Slf4j
@SpringBootTest
class MediaCoverExtractorServiceTest {

    @Autowired
    private MediaCoverExtractorService mediaCoverExtractorService;

    /**
     * 测试自动注入
     */
    @Test
    void testAutoConfiguration() {
        assertNotNull(mediaCoverExtractorService, "MediaCoverExtractorService应该被自动注入");
        
        List<CoverExtractor> extractors = mediaCoverExtractorService.getExtractors();
        assertNotNull(extractors, "抽取器列表不应为null");
        assertFalse(extractors.isEmpty(), "至少应该有一个抽取器被加载");
        
        log.info("已加载的抽取器数量: {}", extractors.size());
        extractors.forEach(extractor -> 
            log.info("抽取器: {} (优先级: {})", 
                    extractor.getClass().getSimpleName(), 
                    extractor.getOrder())
        );
    }

    /**
     * 测试不支持的文件类型
     */
    @Test
    void testUnsupportedFileType() {
        File testFile = new File("test.unknown");
        
        ExtractException exception = assertThrows(
            ExtractException.class,
            () -> mediaCoverExtractorService.extractCover(testFile),
            "应该抛出ExtractException"
        );
        
        log.info("预期的异常信息: {}", exception.getMessage());
        assertTrue(exception.getMessage().contains("不支持") || exception.getMessage().contains("不存在"));
    }

    /**
     * 测试null参数
     */
    @Test
    void testNullFile() {
        ExtractException exception = assertThrows(
            ExtractException.class,
            () -> mediaCoverExtractorService.extractCover((File) null),
            "应该抛出ExtractException"
        );
        
        log.info("预期的异常信息: {}", exception.getMessage());
        assertTrue(exception.getMessage().contains("不能为null"));
    }

    /**
     * 测试空路径
     */
    @Test
    void testEmptyPath() {
        ExtractException exception = assertThrows(
            ExtractException.class,
            () -> mediaCoverExtractorService.extractCover(""),
            "应该抛出ExtractException"
        );
        
        log.info("预期的异常信息: {}", exception.getMessage());
        assertTrue(exception.getMessage().contains("不能为空"));
    }

    /**
     * 测试不存在的文件
     */
    @Test
    void testNonExistentFile() {
        String nonExistentPath = "D:/test/nonexistent.mp4";
        
        ExtractException exception = assertThrows(
            ExtractException.class,
            () -> mediaCoverExtractorService.extractCover(nonExistentPath),
            "应该抛出ExtractException"
        );
        
        log.info("预期的异常信息: {}", exception.getMessage());
        assertTrue(exception.getMessage().contains("不存在"));
    }

    // 注意：以下测试需要实际的测试文件，请根据实际情况启用

    /**
     * 测试视频封面抽取（需要实际视频文件）
     */
     @Test
     void testVideoExtraction() {
         String videoPath = "D:/temp/2025-10-24_155232_700.mp4";  // 替换为实际的视频文件路径
         File coverFile = mediaCoverExtractorService.extractCover(videoPath);

         assertNotNull(coverFile);
         assertTrue(coverFile.exists());
         log.info("视频封面抽取成功: {}", coverFile.getAbsolutePath());
     }

    /**
     * 测试图片处理（需要实际图片文件）
     */
    // @Test
    // void testImageExtraction() {
    //     String imagePath = "D:/test/sample.jpg";  // 替换为实际的图片文件路径
    //     File coverFile = mediaCoverExtractorService.extractCover(imagePath);
    //     
    //     assertNotNull(coverFile);
    //     assertTrue(coverFile.exists());
    //     log.info("图片处理成功: {}", coverFile.getAbsolutePath());
    // }

    /**
     * 测试文档封面抽取（需要实际文档文件）
     */
     @Test
     void testDocumentExtraction() {
         String docPath = "D:/temp/测试文档.docx";  // 替换为实际的文档文件路径
         File coverFile = mediaCoverExtractorService.extractCover(docPath);

         assertNotNull(coverFile);
         assertTrue(coverFile.exists());
         log.debug("文档封面抽取成功: {}", coverFile.getAbsolutePath());
     }

    /**
     * 测试音频封面抽取（需要实际音频文件）
     */
    // @Test
    // void testAudioExtraction() {
    //     String audioPath = "D:/test/sample.mp3";  // 替换为实际的音频文件路径
    //     File coverFile = mediaCoverExtractorService.extractCover(audioPath);
    //     
    //     assertNotNull(coverFile);
    //     assertTrue(coverFile.exists());
    //     log.info("音频封面抽取成功: {}", coverFile.getAbsolutePath());
    // }
}

