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
    Byte LoginRequestMessage = 1;
    Byte LoginResponseMessage = 2;
    Byte AddFriendRequestMessage = 3;
    Byte AddFriendResponseMessage = 4;
    Byte SearchFriendRequestMessage = 5;
    Byte SearchFriendResponseMessage = 6;
    Byte DeleteDialogRequestMessage = 7;
    Byte DialogNoticeRequestMessage = 8;
    Byte DialogNoticeResponseMessage = 9;
    Byte ChatRequestMessage = 10;
    Byte ChatResponseMessage = 11;
    Byte FileUploadRequestMessage = 12;
    Byte FileUploadResponseMessage = 13;
    Byte RegisterRequestMessage = 14;
    Byte RegisterResponseMessage = 15;
    Byte ReconnectRequestMessage = 16;

    Byte TestRequestMessage = 19;
    Byte TestResponseMessage = 20;

}
