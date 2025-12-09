package com.github.youzhajun.document.executor;

import com.github.youzhajun.common.exception.ExtractException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * PDFBox执行器
 */
@Slf4j
public class PdfBoxExecutor {

    /**
     * 从PDF提取第一页作为图片
     * @param pdfFile PDF文件
     * @param outputFile 输出图片文件
     * @param format 图片格式
     */
    public void extractFirstPage(File pdfFile, File outputFile, String format) {
        if (!pdfFile.exists() || !pdfFile.isFile()) {
            throw new ExtractException("PDF文件不存在或不是文件: " + pdfFile.getAbsolutePath());
        }

        PDDocument document = null;
        try {
            // 加载PDF文档
            document = Loader.loadPDF(pdfFile);
            
            if (document.getNumberOfPages() == 0) {
                throw new ExtractException("PDF文档没有页面");
            }

            // 创建PDF渲染器
            PDFRenderer renderer = new PDFRenderer(document);
            
            // 渲染第一页（索引为0），DPI设置为300以获得高质量图片
            BufferedImage image = renderer.renderImageWithDPI(0, 300);

            // 保存图片
            ImageIO.write(image, format, outputFile);
            
            log.debug("PDF第一页提取成功: {} -> {}", pdfFile.getName(), outputFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("PDF页面提取失败", e);
            throw new ExtractException("PDF页面提取失败: " + e.getMessage(), e);
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    log.warn("关闭PDF文档失败", e);
                }
            }
        }
    }
}

