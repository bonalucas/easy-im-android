package com.esayim.client.common;

/**
 * 唯一ID算法（雪花算法）
 */
public class SnowflakeIDGenerator {

    private static final long EPOCH = 1609459200000L;
    private static final long SEQUENCE_BITS = 12;
    private static final long MACHINE_ID_BITS = 10;
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);
    private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS;
    private static final long machineId = 1;
    private static long lastTimestamp = -1L;
    private static long sequence = 0L;

    public static synchronized String generateID() {
        long currentTimestamp = System.currentTimeMillis() - EPOCH;

        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate ID.");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                currentTimestamp = waitUntilNextMillis(currentTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = currentTimestamp;

        return String.valueOf(((currentTimestamp << TIMESTAMP_SHIFT) | (machineId << MACHINE_ID_SHIFT) | sequence));
    }

    private static long waitUntilNextMillis(long lastTimestamp) {
        long currentTimestamp;
        do {
            currentTimestamp = System.currentTimeMillis() - EPOCH;
        } while (currentTimestamp <= lastTimestamp);
        return currentTimestamp;
    }
}

