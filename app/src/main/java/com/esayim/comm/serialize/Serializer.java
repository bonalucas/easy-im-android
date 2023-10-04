package com.esayim.comm.serialize;

/**
 * 序列化接口
 *
 * @author 单程车票
 */
public interface Serializer {

    /**
     * 反序列化算法
     */
    <T> byte[] serialize(T object);

    /**
     * 二进制转换成 java 对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);

}
