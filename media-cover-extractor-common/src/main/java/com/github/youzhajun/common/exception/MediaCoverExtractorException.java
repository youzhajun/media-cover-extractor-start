package com.github.youzhajun.common.exception;

/**
 * 媒体封面抽取异常基类
 */
public class MediaCoverExtractorException extends RuntimeException {

    public MediaCoverExtractorException(String message) {
        super(message);
    }

    public MediaCoverExtractorException(String message, Throwable cause) {
        super(message, cause);
    }

    public MediaCoverExtractorException(Throwable cause) {
        super(cause);
    }
}

