package com.github.youzhajun.common.exception;

/**
 * 封面抽取异常
 */
public class ExtractException extends MediaCoverExtractorException {

    public ExtractException(String message) {
        super(message);
    }

    public ExtractException(String message, Throwable cause) {
        super(message, cause);
    }
}

