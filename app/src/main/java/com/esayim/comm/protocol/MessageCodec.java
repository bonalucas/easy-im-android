package com.esayim.comm.protocol;

import android.util.Log;

import com.esayim.comm.message.Message;
import com.esayim.comm.serialize.Serializer;
import com.esayim.comm.serialize.SerializerAlgorithmConstants;
import com.esayim.comm.serialize.impl.JSONSerializer;
import com.esayim.comm.serialize.impl.ProtobufSerializer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

/**
 * 自定义编解码器
 *
 * @author 单程车票
 */
@ChannelHandler.Sharable
public class MessageCodec extends MessageToMessageCodec<ByteBuf, Message> {

    /**
     * 魔数
     */
    private static final int MAGIC_NUMBER = 0xbacaedfd;

    /**
     * 协议序列化算法
     */
    private static final Byte SERIALIZER_ALGORITHM = SerializerAlgorithmConstants.PROTOBUF;

    /**
     * 序列化算法类型表
     */
    private final static Map<Byte, Serializer> serializerAlgorithmMap = new ConcurrentHashMap<>();

    static {
        serializerAlgorithmMap.put(SerializerAlgorithmConstants.PROTOBUF, new ProtobufSerializer());
        serializerAlgorithmMap.put(SerializerAlgorithmConstants.JSON, new JSONSerializer());
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        // 构造编码后的数据
        ByteBuf buffer = ctx.alloc().buffer();
        // 写入魔数（4 字节）
        buffer.writeInt(MAGIC_NUMBER);
        // 写入消息类型（1 字节）
        buffer.writeByte(msg.getConstant());
        // 写入序列化算法类型并获取对应序列化类（1 字节）
        buffer.writeByte(SERIALIZER_ALGORITHM);
        Serializer serializer = serializerAlgorithmMap.get(SERIALIZER_ALGORITHM);
        // 写入序列化消息的长度与内容
        byte[] msg_bytes = serializer.serialize(msg);
        buffer.writeInt(msg_bytes.length);
        buffer.writeBytes(msg_bytes);
        // 输出
        out.add(buffer);
        Log.i(MessageCodec.class.getSimpleName(), "encode: 编码成功");
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 获取魔数并校验协议
        int magic_number = in.readInt();
        if (magic_number != MessageCodec.MAGIC_NUMBER) {
            Log.e(MessageCodec.class.getSimpleName(),"协议不匹配，忽略消息内容");
            return;
        }
        // 获取解码后的消息类型
        byte msg_type = in.readByte();
        // 获取序列化算法类型并获取对应序列化类
        byte serializerAlgorithm = in.readByte();
        Serializer serializer = serializerAlgorithmMap.get(serializerAlgorithm);
        // 读取解码后的消息长度
        int msg_len = in.readInt();
        // 构造数据字节数组
        byte[] msg_bytes = new byte[msg_len];
        // 获取解码后的消息数据
        in.readBytes(msg_bytes);
        // 通过反序列化获取消息数据
        Message msg = serializer.deserialize(Message.get(msg_type), msg_bytes);
        // 输出
        out.add(msg);
        Log.i(MessageCodec.class.getSimpleName(), "decode: 解码成功");
    }

}
