# Media Cover Extractor Starter

> SpringBoot 3 自定义启动器，用于多类型文件（视频、文档、图片、音频）的封面/展示图抽取

## 📋 项目简介

本项目是一个可复用的 SpringBoot 启动器，提供了统一的接口来抽取不同类型文件的封面/展示图。支持模块化引入、灵活配置、易于扩展。

## ✨ 核心特性

- **多文件类型支持**：视频、图片、文档（doc/docx/ppt/pptx/xls/xlsx/txt/pdf/pps）、音频
- **模块化设计**：按需引入依赖，减少项目体积
- **统一接口**：对外只暴露一个服务接口，自动识别文件类型并选择对应的处理器
- **灵活配置**：支持多种配置选项，满足不同场景需求
- **易于扩展**：提供抽象类，支持自定义实现
- **自动装配**：基于 SpringBoot 3 的自动配置机制

## 🏗️ 项目结构

```
media-cover-extractor-start/
├── media-cover-extractor-common          # 公共模块（异常、枚举、接口定义）
├── media-cover-extractor-ffmpeg          # FFmpeg模块（视频+图片处理）
├── media-cover-extractor-document        # 文档模块（LibreOffice+PDFBox）
├── media-cover-extractor-audio           # 音频模块（提供抽象基类）
├── media-cover-extractor-autoconfigure   # 自动配置模块
└── media-cover-extractor-test            # 测试模块
```

## 🚀 快速开始

### 1. 前置要求

- JDK 17+
- SpringBoot 3.2.0+
- Maven 3.6+

### 2. 安装依赖工具

#### FFmpeg（视频和图片处理）

**Windows:**
1. 下载 FFmpeg：https://ffmpeg.org/download.html
2. 解压到任意目录（如 `D:/ffmpeg`）
3. 添加 `D:/ffmpeg/bin` 到系统环境变量 PATH，或在配置文件中指定路径

**Linux:**
```bash
# Ubuntu/Debian
sudo apt-get install ffmpeg

# CentOS/RHEL
sudo yum install ffmpeg
```

**验证安装:**
```bash
ffmpeg -version
```

#### LibreOffice（文档处理）

**Windows:**
1. 下载 LibreOffice：https://www.libreoffice.org/download/
2. 安装到默认路径（如 `C:/Program Files/LibreOffice`）
3. 记录 `soffice.exe` 的完整路径用于配置

**Linux:**
```bash
# Ubuntu/Debian
sudo apt-get install libreoffice

# CentOS/RHEL
sudo yum install libreoffice
```

**验证安装:**
```bash
soffice --version
```

### 3. 添加 JitPack 仓库

本项目已发布到 JitPack，使用前需要先添加 JitPack 仓库到你的 `pom.xml`：

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

**完整 pom.xml 示例：**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>your-project</artifactId>
    <version>1.0.0</version>

    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependencies>
        <!-- 添加依赖 -->
    </dependencies>
</project>
```

**JitPack 页面：** [https://jitpack.io/#youzhajun/media-cover-extractor-start](https://jitpack.io/#youzhajun/media-cover-extractor-start)

### 4. Maven 依赖引入

根据需要选择性引入模块依赖：

#### 方案一：引入所有功能（完整版）

```xml
<dependency>
    <groupId>com.github.youzhajun</groupId>
    <artifactId>media-cover-extractor-autoconfigure</artifactId>
    <version>1.0.0</version>
</dependency>
<dependency>
    <groupId>com.github.youzhajun</groupId>
    <artifactId>media-cover-extractor-ffmpeg</artifactId>
    <version>1.0.0</version>
</dependency>
<dependency>
    <groupId>com.github.youzhajun</groupId>
    <artifactId>media-cover-extractor-document</artifactId>
    <version>1.0.0</version>
</dependency>
<dependency>
    <groupId>com.github.youzhajun</groupId>
    <artifactId>media-cover-extractor-audio</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### 方案二：仅引入文档处理功能

```xml
<dependency>
    <groupId>com.github.youzhajun</groupId>
    <artifactId>media-cover-extractor-autoconfigure</artifactId>
    <version>1.0.0</version>
</dependency>
<dependency>
    <groupId>com.github.youzhajun</groupId>
    <artifactId>media-cover-extractor-document</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### 方案三：仅引入视频和图片处理功能

```xml
<dependency>
    <groupId>com.github.youzhajun</groupId>
    <artifactId>media-cover-extractor-autoconfigure</artifactId>
    <version>1.0.0</version>
</dependency>
<dependency>
    <groupId>com.github.youzhajun</groupId>
    <artifactId>media-cover-extractor-ffmpeg</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 5. 配置文件

在 `application.yml` 或 `application.properties` 中配置：

#### application.yml 示例

```yaml
media:
  cover:
    # FFmpeg配置（视频和图片处理）
    ffmpeg:
      enabled: true                                    # 是否启用FFmpeg功能，默认true
      path: D:/ffmpeg/bin/ffmpeg.exe                  # FFmpeg路径（可选，不配置则使用环境变量）
    
    # LibreOffice配置（文档处理）
    libreoffice:
      enabled: true                                    # 是否启用文档处理功能，默认true
      path: C:/Program Files/LibreOffice/program/soffice.exe  # LibreOffice路径（必填）
    
    # 输出配置
    output:
      format: jpg                                      # 输出格式，默认jpg，可选png/jpg等
      strategy: SCALE                                  # 图片处理策略：SCALE-等比例缩放，CROP-强制裁剪，NONE-不处理
      width: 800                                       # 图片宽度（当strategy为SCALE或CROP时有效）
      height: 600                                      # 图片高度（当strategy为SCALE或CROP时有效）
      directory: ${java.io.tmpdir}                    # 输出目录，默认系统临时目录
      use-date-subdirectory: true                      # 是否启用日期子目录（年/月/日），默认false
```

#### application.properties 示例

```properties
# FFmpeg配置
media.cover.ffmpeg.enabled=true
media.cover.ffmpeg.path=D:/ffmpeg/bin/ffmpeg.exe

# LibreOffice配置
media.cover.libreoffice.enabled=true
media.cover.libreoffice.path=C:/Program Files/LibreOffice/program/soffice.exe

# 输出配置
media.cover.output.format=jpg
media.cover.output.strategy=SCALE
media.cover.output.width=800
media.cover.output.height=600
media.cover.output.directory=${java.io.tmpdir}
```

#### Linux 配置示例

```yaml
media:
  cover:
    ffmpeg:
      enabled: true
      path: /usr/bin/ffmpeg                          # Linux下FFmpeg路径
    libreoffice:
      enabled: true
      path: /usr/bin/soffice                         # Linux下LibreOffice路径
    output:
      format: jpg
      strategy: SCALE
      width: 800
      height: 600
      directory: /tmp/covers                         # Linux临时目录
```

### 6. 使用示例

```java
import com.github.youzhajun.autoconfigure.service.MediaCoverExtractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class YourService {

    @Autowired
    private MediaCoverExtractorService mediaCoverExtractorService;

    /**
     * 抽取封面示例
     */
    public void extractCoverExample() {
        // 方式1：通过文件路径抽取
        String filePath = "D:/test/video.mp4";
        File coverFile = mediaCoverExtractorService.extractCover(filePath);
        System.out.println("封面文件路径: " + coverFile.getAbsolutePath());

        // 方式2：通过File对象抽取
        File sourceFile = new File("D:/test/document.pdf");
        File cover = mediaCoverExtractorService.extractCover(sourceFile);
        System.out.println("封面生成成功: " + cover.getAbsolutePath());
    }
}
```

## 📖 配置详解

### FFmpeg 配置

| 配置项                    | 类型    | 必填 | 默认值 | 说明                                          |
|------------------------|-------|----|----|---------------------------------------------|
| media.cover.ffmpeg.enabled | boolean | 否  | true | 是否启用FFmpeg功能                              |
| media.cover.ffmpeg.path | String | 否  | ffmpeg | FFmpeg可执行文件路径，不配置时使用环境变量中的ffmpeg      |

**注意事项：**
- 如果FFmpeg已加入系统环境变量，可以不配置 `path`
- 启动时会自动验证FFmpeg是否可用（执行 `ffmpeg -version`）
- 验证失败仅打印ERROR日志，不会阻断项目启动
- 调用FFmpeg功能时，如果配置错误会抛出 `ExtractException`

### LibreOffice 配置

| 配置项                         | 类型    | 必填 | 默认值 | 说明                    |
|-----------------------------|-------|----|----|---------------------------|
| media.cover.libreoffice.enabled | boolean | 否  | true | 是否启用文档处理功能          |
| media.cover.libreoffice.path | String | 是  | 无  | LibreOffice的soffice可执行文件路径 |

**注意事项：**
- `path` 配置必须指向 `soffice` 或 `soffice.exe` 的完整路径
- 启动时会自动验证LibreOffice是否可用（执行 `soffice --version`）
- 验证失败仅打印ERROR日志，不会阻断项目启动
- 调用文档处理功能时，如果配置错误会抛出 `ExtractException`

### 输出配置

| 配置项                      | 类型    | 必填 | 默认值              | 说明                                  |
|--------------------------|-------|----|--------------------|-------------------------------------|
| media.cover.output.format | String | 否  | jpg            | 输出图片格式（jpg/png等）                  |
| media.cover.output.strategy | Enum | 否  | NONE           | 图片处理策略：SCALE/CROP/NONE           |
| media.cover.output.width | Integer | 否  | 无                | 图片宽度（strategy为SCALE或CROP时必填）     |
| media.cover.output.height | Integer | 否  | 无                | 图片高度（strategy为SCALE或CROP时必填）     |
| media.cover.output.directory | String | 否  | java.io.tmpdir | 输出目录，默认系统临时目录                     |
| media.cover.output.use-date-subdirectory | boolean | 否  | false | 是否启用日期子目录（年/月/日），启用后会在配置目录下创建日期层级 |

**图片处理策略说明：**

1. **SCALE（等比例缩放）**：保持原图宽高比，缩放到指定的宽高范围内
   - 示例：原图1920x1080，设置width=800, height=600
   - 结果：缩放为800x450（保持16:9比例）

2. **CROP（强制裁剪）**：强制调整为指定的宽高，可能会变形
   - 示例：原图1920x1080，设置width=800, height=600
   - 结果：强制调整为800x600

3. **NONE（不处理）**：不进行任何处理，保持原图
   - 对于图片文件：直接复制原文件
   - 对于视频/文档：抽取原始尺寸的封面

## 🎯 功能模块说明

### 1. 视频封面抽取

- **技术实现**：FFmpeg
- **支持格式**：mp4, avi, mov, wmv, flv, mkv, webm, m4v, rmvb, 3gp
- **处理逻辑**：
  1. 从视频第1秒位置截取一帧
  2. 根据配置的 strategy 进行图片处理
  3. 输出为指定格式的图片文件

### 2. 图片处理

- **技术实现**：FFmpeg
- **支持格式**：jpg, jpeg, png, gif, bmp, webp, svg, tiff, ico
- **处理逻辑**：
  1. 如果 strategy 为 NONE，直接复制原图
  2. 否则根据配置的 strategy 进行缩放或裁剪
  3. 输出为指定格式的图片文件

### 3. 文档封面抽取

- **技术实现**：LibreOffice + PDFBox
- **支持格式**：doc, docx, ppt, pptx, xls, xlsx, txt, pdf, pps
- **处理逻辑**：
  1. 非PDF文档：先通过LibreOffice转换为PDF
  2. PDF文档：使用PDFBox渲染第一页为图片（DPI=300）
  3. 清理临时PDF文件
  4. 输出为指定格式的图片文件

### 4. 音频封面抽取

- **技术实现**：提供抽象基类，支持自定义扩展
- **支持格式**：mp3, wav, flac, aac, ogg, wma, m4a, ape
- **默认实现**：返回默认音频图标（可自定义替换）
- **扩展方式**：继承 `AbstractAudioCoverExtractor` 类

## 🔧 自定义扩展

### 自定义音频封面处理器

```java
import com.github.youzhajun.audio.extractor.AbstractAudioCoverExtractor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * 自定义音频封面抽取器
 */
@Component
public class CustomAudioCoverExtractor extends AbstractAudioCoverExtractor {

    @Override
    protected File doExtract(File sourceFile) {
        // 方式1：返回固定的默认图片
        return new File("D:/covers/default-audio-cover.png");
        
        // 方式2：根据音频文件生成封面（例如读取音频元数据中的封面）
        // ... 自定义实现逻辑
        
        // 方式3：调用第三方API生成封面
        // ... 自定义实现逻辑
    }

    @Override
    public int getOrder() {
        return 350;  // 优先级高于默认实现（400），数值越小优先级越高
    }
}
```

### 自定义其他类型处理器

```java
import com.github.youzhajun.common.enums.FileType;
import com.github.youzhajun.common.extractor.CoverExtractor;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 自定义封面抽取器
 */
@Component
public class CustomCoverExtractor implements CoverExtractor {

    @Override
    public File extract(File sourceFile) {
        // 实现自定义抽取逻辑
        return null;
    }

    @Override
    public boolean support(FileType fileType) {
        // 定义支持的文件类型
        return fileType == FileType.VIDEO;
    }

    @Override
    public int getOrder() {
        // 设置优先级（数值越小优先级越高）
        return 50;
    }
}
```

## 🐛 异常处理

### 配置验证

- **时机**：项目启动时
- **行为**：仅打印 ERROR 级别日志，不阻断项目启动
- **日志示例**：
  ```
  ERROR - FFmpeg配置错误：FFmpeg不可用，请检查配置项 media.cover.ffmpeg.path
  ERROR - LibreOffice配置错误：未配置 media.cover.libreoffice.path
  ```

### 功能调用异常

- **时机**：调用具体功能时
- **行为**：抛出 `ExtractException` 异常
- **异常信息**：包含具体的错误原因

```java
try {
    File cover = mediaCoverExtractorService.extractCover(filePath);
} catch (ExtractException e) {
    // 处理异常
    System.err.println("封面抽取失败: " + e.getMessage());
}
```

### 常见异常

1. **配置错误**
   - `ConfigurationException`: FFmpeg或LibreOffice配置不正确
   
2. **文件错误**
   - `ExtractException`: 源文件不存在
   - `ExtractException`: 不支持的文件类型
   - `ExtractException`: 文件读取失败

3. **执行错误**
   - `ExtractException`: FFmpeg执行失败
   - `ExtractException`: LibreOffice转换失败
   - `ExtractException`: PDF页面提取失败

## 📊 支持的文件类型

| 类型 | 扩展名                                                     | 所需模块                    |
|----|-----------------------------------------------------------|---------------------------|
| 视频 | mp4, avi, mov, wmv, flv, mkv, webm, m4v, rmvb, 3gp      | media-cover-extractor-ffmpeg |
| 图片 | jpg, jpeg, png, gif, bmp, webp, svg, tiff, ico          | media-cover-extractor-ffmpeg |
| 文档 | doc, docx, ppt, pptx, xls, xlsx, txt, pdf, pps          | media-cover-extractor-document |
| 音频 | mp3, wav, flac, aac, ogg, wma, m4a, ape                 | media-cover-extractor-audio |

## 🧪 测试

项目包含完整的单元测试和集成测试：

```bash
# 运行所有测试
mvn test

# 运行指定模块测试
cd media-cover-extractor-test
mvn test
```

**测试覆盖：**
- ✅ 自动注入测试
- ✅ 文件类型识别测试
- ✅ 参数验证测试
- ✅ 异常处理测试
- ✅ 自定义扩展测试

## 📝 注意事项

### 1. 依赖工具安装

- FFmpeg 和 LibreOffice 需要在操作系统中正确安装
- 确保可执行文件路径正确配置
- Windows 路径使用反斜杠或双反斜杠：`C:/Program Files/` 或 `C:\\Program Files\\`

### 2. 文件权限

- 确保应用有读取源文件的权限
- 确保应用有写入输出目录的权限
- Linux 系统注意文件权限设置

### 3. 临时文件

- 文档转换过程中会生成临时PDF文件，处理完成后会自动清理
- 输出的封面文件不会自动清理，需要业务层自行管理
- 建议定期清理输出目录中的旧文件

### 4. 性能考虑

- 视频和文档处理是 CPU 密集型操作
- 建议使用异步处理或队列机制处理大批量文件
- 可以配置线程池来控制并发处理数量

### 5. 文件大小

- 大文件处理可能耗时较长
- 建议对文件大小进行限制
- 可以考虑对大文件进行分片处理

## 🔗 项目信息

- **项目名称**：media-cover-extractor-start
- **版本**：1.0.0
- **作者**：com.github.youzhajun
- **JDK版本**：17+
- **SpringBoot版本**：3.2.0
- **打包方式**：Maven

## 📄 License

本项目采用 MIT License，详见 LICENSE 文件。

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 📧 联系方式

如有问题或建议，请通过以下方式联系：

- 提交 GitHub Issue
- 发送邮件到项目维护者

---

**祝使用愉快！** 🎉

