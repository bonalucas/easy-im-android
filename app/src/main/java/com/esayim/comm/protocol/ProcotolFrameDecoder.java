package com.esayim.comm.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 自定义长度字段解码器
 *
 * @author 单程车票
 */
public class ProcotolFrameDecoder extends LengthFieldBasedFrameDecoder {

    /**
     * 数据最大长度
     */
    private static final int maxFrameLength = Integer.MAX_VALUE;

    /**
     * 数据长度标识的起始偏移量（指明数据第几个字节开始是用于标识有用字节长度）
     */
    private static final int lengthFieldOffset = 6;

    /**
     * 数据长度标识所占字节数（表示有用数据长度的标识所占的字节数）
     */
    private static final int lengthFieldLength = 4;

    /**
     * 数据长度与有用数据的偏移量（指明数据长度标识和有用数据之间的距离）
     */
    private static final int lengthAdjustment = 0;

    /**
     * 数据读取起点
     */
    private static final int initialBytesToStrip = 0;

    public ProcotolFrameDecoder() {
        this(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    public ProcotolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

}
