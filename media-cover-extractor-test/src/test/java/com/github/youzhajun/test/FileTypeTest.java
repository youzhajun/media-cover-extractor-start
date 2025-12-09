package com.github.youzhajun.test;

import com.github.youzhajun.common.enums.FileType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 文件类型枚举单元测试
 */
@Slf4j
class FileTypeTest {

    @Test
    void testVideoFileType() {
        assertEquals(FileType.VIDEO, FileType.fromExtension("mp4"));
        assertEquals(FileType.VIDEO, FileType.fromExtension(".mp4"));
        assertEquals(FileType.VIDEO, FileType.fromExtension("MP4"));
        assertEquals(FileType.VIDEO, FileType.fromFileName("test.mp4"));
        assertEquals(FileType.VIDEO, FileType.fromFileName("video.avi"));
        log.info("视频文件类型测试通过");
    }

    @Test
    void testImageFileType() {
        assertEquals(FileType.IMAGE, FileType.fromExtension("jpg"));
        assertEquals(FileType.IMAGE, FileType.fromExtension(".png"));
        assertEquals(FileType.IMAGE, FileType.fromExtension("JPEG"));
        assertEquals(FileType.IMAGE, FileType.fromFileName("photo.jpg"));
        assertEquals(FileType.IMAGE, FileType.fromFileName("image.png"));
        log.info("图片文件类型测试通过");
    }

    @Test
    void testDocumentFileType() {
        assertEquals(FileType.DOCUMENT, FileType.fromExtension("pdf"));
        assertEquals(FileType.DOCUMENT, FileType.fromExtension(".docx"));
        assertEquals(FileType.DOCUMENT, FileType.fromExtension("XLSX"));
        assertEquals(FileType.DOCUMENT, FileType.fromFileName("document.pdf"));
        assertEquals(FileType.DOCUMENT, FileType.fromFileName("spreadsheet.xls"));
        log.info("文档文件类型测试通过");
    }

    @Test
    void testAudioFileType() {
        assertEquals(FileType.AUDIO, FileType.fromExtension("mp3"));
        assertEquals(FileType.AUDIO, FileType.fromExtension(".wav"));
        assertEquals(FileType.AUDIO, FileType.fromExtension("FLAC"));
        assertEquals(FileType.AUDIO, FileType.fromFileName("music.mp3"));
        assertEquals(FileType.AUDIO, FileType.fromFileName("sound.wav"));
        log.info("音频文件类型测试通过");
    }

    @Test
    void testUnknownFileType() {
        assertEquals(FileType.UNKNOWN, FileType.fromExtension("xyz"));
        assertEquals(FileType.UNKNOWN, FileType.fromExtension(""));
        assertEquals(FileType.UNKNOWN, FileType.fromExtension(null));
        assertEquals(FileType.UNKNOWN, FileType.fromFileName("noextension"));
        assertEquals(FileType.UNKNOWN, FileType.fromFileName(""));
        assertEquals(FileType.UNKNOWN, FileType.fromFileName(null));
        log.info("未知文件类型测试通过");
    }
}

