package com.esayim.comm.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.esayim.comm.serialize.Serializer;

/**
 * Json 序列化实现类
 *
 * @author 单程车票
 */
public class JSONSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }

}
