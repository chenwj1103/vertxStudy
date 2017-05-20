package com.ifeng.chen.utils;

/**
 * Created by Chen Weijie on 2017/5/21.
 */
public enum UTCMatchFormat {
    UTC_FULL("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
    UTC_MS_TWO_BIT("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{2}Z", "yyyy-MM-dd'T'HH:mm:ss.SS'Z'"),
    UTC_MS_ONE_BIT("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{1}Z", "yyyy-MM-dd'T'HH:mm:ss.S'Z'"),
    UTC_NO_MS("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z", "yyyy-MM-dd'T'HH:mm:ss'Z'"),
    UTC_NO_MS1("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}", "yyyy-MM-dd HH:mm:ss"),
    UTC_NO_MS2("\\d{4}-\\d{2}-\\d{2}", "yyyy-MM-dd"),
    UTC_NO_MS3("\\d{14}", "yyyyMMddHHmmss");

    private String pattern;
    private String format;

    UTCMatchFormat(String pattern, String format) {
        this.pattern = pattern;
        this.format = format;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

}
