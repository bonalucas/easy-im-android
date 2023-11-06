package com.easyim.common;

import java.util.Random;

/**
 * 自定义唯一ID算法
 *
 * @author 单程车票
 */
public class FastUniqueIDGenerator {

    /**
     * ID长度
     */
    private static final int length = 9;

    public static String generateID() {
        Random random = new Random();
        StringBuilder uniqueID = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // 生成0到9之间的随机数字
            uniqueID.append(digit);
        }
        return uniqueID.toString();
    }

}
