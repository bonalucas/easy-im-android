package com.esayim.comm.message;

/**
 * 消息类型
 *
 * @author 单程车票
 */
public interface MessageTypeConstants {

    // 握手消息
    Byte HandShakeRequestMessage = 1;
    Byte HandShakeResponseMessage = 2;
    // 心跳消息
    Byte PingMessage = 3;
    Byte PongMessage = 4;
    // 业务消息
    Byte ErrorResponseMessage = 5;
    Byte TestRequestMessage = 101;
    Byte TestResponseMessage = 102;

}
