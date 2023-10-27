package com.esayim.comm.protocol;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.esayim.comm.message.Message;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

/**
 * Easy-IM 编解码器
 *
 * @author 单程车票
 */
@ChannelHandler.Sharable
public class MessageCodec extends MessageToMessageCodec<ByteBuf, Message> {

    /**
     * 魔数
     */
    private static final int CRC_CODE = 0xEAEA2023;

    /**
     * 静态内部类（单例模式）
     */
    private static class MessageCodecInstance {
        private static final MessageCodec INSTANCE = new MessageCodec();
    }

    /**
     * 获取单例模式下的实例
     */
    public static MessageCodec getInstance() {
        return MessageCodecInstance.INSTANCE;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        // 构造编码后的数据
        ByteBuf buffer = ctx.alloc().buffer();
        // 写入校验码
        buffer.writeInt(CRC_CODE);
        // 写入消息类型
        buffer.writeByte(msg.getConstant());
        // 写入占位符
        buffer.writeByte(0);
        // 序列化消息内容
        byte[] msg_bytes = ProtobufSerializer.serialize(msg);
        // 写入消息长度以及消息内容
        buffer.writeInt(msg_bytes.length);
        buffer.writeBytes(msg_bytes);
        // 输出
        out.add(buffer);
        // 记录日志
        Log.d("MessageCodec", String.format("消息：{%s}编码成功", JSON.toJSONString(msg)));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 获取校验码并校验协议
        int crcCode = in.readInt();
        if (crcCode != MessageCodec.CRC_CODE) return;
        // 获取解码后的消息类型
        byte msg_type = in.readByte();
        // 取出占位符
        in.readByte();
        // 获取解码后的消息长度
        int msg_len = in.readInt();
        // 构造数据字节数组
        byte[] msg_bytes = new byte[msg_len];
        // 获取解码后的消息内容
        in.readBytes(msg_bytes);
        // 消息内容反序列化
        Message msg = ProtobufSerializer.deserialize(Message.get(msg_type), msg_bytes);
        // 输出
        out.add(msg);
        // 记录日志
        Log.d("MessageCodec", String.format("消息：{%s}解码成功", JSON.toJSONString(msg)));
    }

}
