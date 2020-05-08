package com.sptan.exec.framework.identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author liupeng
 * @version 1.0
 */
public class IdentifierGenerator {
    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(IdentifierGenerator.class);
    private static SnowflakeIdGenerator snowflakeIdGenerator = null;

    /**
     * 唯一键生成 .
     *
     * @return .
     */
    public static String identifier() {
        try {
            if (Objects.isNull(snowflakeIdGenerator)) {
                snowflakeIdGenerator = new SnowflakeIdGenerator();
            }
            return String.valueOf(snowflakeIdGenerator.generate());
        } catch (Exception e) {
            LOGGER.error("[唯一键生成异常]", e);
        }
        return String.valueOf(System.currentTimeMillis());
    }
}
