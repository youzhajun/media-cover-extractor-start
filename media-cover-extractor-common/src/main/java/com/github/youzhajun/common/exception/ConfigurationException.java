package com.github.youzhajun.common.exception;

/**
 * 配置异常
 */
public class ConfigurationException extends MediaCoverExtractorException {

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}

