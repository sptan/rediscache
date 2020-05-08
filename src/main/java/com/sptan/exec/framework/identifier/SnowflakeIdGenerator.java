package com.sptan.exec.framework.identifier;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Objects;

/**
 * @author liupeng
 * @version 1.0
 */
public class SnowflakeIdGenerator implements IdentifierGenerator {
    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SnowflakeIdGenerator.class);
    private static final long INSTANCE_ID =
        (String.format("%d%d", getHardwareAddress(), getJvmPid()).hashCode()) & 0xffff;
    private final long epoch = 1483200000000L;
    private final long sequenceBits = 9L;
    private final long sequenceMax = -1L ^ -1L << this.sequenceBits;
    private final long instanceIdBits = 16L;
    private final long instanceIdLeftShift = this.sequenceBits;
    private final long timestampLeftShift = this.sequenceBits + this.instanceIdBits;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    /**
     * Fetch MAC address.
     *
     * @return MAC Integer MAC address
     */
    private static long getHardwareAddress() {
        byte[] hardwareAddress = new byte[0];
        try {
            final InetAddress inetAddress = InetAddress.getLocalHost();
            LOGGER.info("[InetAddress:{}]", inetAddress);
            if (Objects.nonNull(inetAddress)) {
                NetworkInterface networkInterface = NetworkInterface.getByInetAddress(inetAddress);
                if (Objects.isNull(networkInterface)) {
                    Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
                    networkInterface = enumeration.nextElement();
                }
                if (Objects.nonNull(networkInterface)) {
                    hardwareAddress = networkInterface.getHardwareAddress();
                }
            }
        } catch (Exception e) {
            LOGGER.warn("[网卡信息异常] - [BadHost]", e);
        }
        StringBuilder builder = new StringBuilder();
        if (Objects.nonNull(hardwareAddress)) {
            for (byte item : hardwareAddress) {
                builder.append(String.format("%02X", item));
            }
        }
        if (builder.length() == 0) {
            builder.append(0);
        }
        return Long.parseLong(builder.toString(), 16);
    }

    /**
     * Fetch PID.
     *
     * @return pid JVM process ID
     */
    private static long getJvmPid() {
        return Long.parseLong(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
    }

    /**
     * Wait for next millis, make sure the return value more than lastTimestamp.
     *
     * @param lastTimestamp The last time stamp
     */
    private long waitingNextMillis(long lastTimestamp) {
        long timestamp = this.getCurrentTime();
        while (timestamp <= lastTimestamp) {
            timestamp = this.getCurrentTime();
        }
        return timestamp;
    }

    /**
     * Get system time.
     */
    private long getCurrentTime() {
        return System.currentTimeMillis();
    }


    public synchronized long generate() throws Exception {
        long timestamp = this.getCurrentTime();
        if (this.lastTimestamp == timestamp) {
            this.sequence = this.sequence + 1L & this.sequenceMax;
            if (this.sequence == 0L) {
                timestamp = this.waitingNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0L;
        }

        if (timestamp < this.lastTimestamp) {
            throw new Exception(String.format("clock moved backwards. Refusing to generate id for %d milliseconds", this.lastTimestamp - timestamp));
        } else {
            this.lastTimestamp = timestamp;
            this.getClass();
            return timestamp - 1483200000000L << (int)this.timestampLeftShift | INSTANCE_ID << (int)this.instanceIdLeftShift | this.sequence;
        }
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        try {
            return String.valueOf(this.generate());
        } catch (Exception var4) {
            LOGGER.error("[生成序列号失败]", var4);
            return null;
        }
    }

}
