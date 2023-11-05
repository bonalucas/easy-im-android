package com.esayim;

import com.easyim.client.handler.ExceptionHandler;
import com.easyim.client.handler.biz.CreateMeetingHandler;
import com.easyim.comm.message.meeting.CreateMeetingRequestMessage;
import com.easyim.comm.protocol.MessageCodec;
import com.easyim.comm.protocol.ProtocolFrameDecoder;

import org.junit.Test;

import java.lang.reflect.Field;

import io.netty.channel.embedded.EmbeddedChannel;

/**
 * 单元测试
 */
public class UnitTest {

    @Test
    public void test() {
        EmbeddedChannel channel = new EmbeddedChannel(
                new ProtocolFrameDecoder(),
                new MessageCodec(),
                new CreateMeetingHandler(),
                new ExceptionHandler());

        channel.writeAndFlush(new CreateMeetingRequestMessage(376201686030487552L,
                "960875251", "yyy", "xxx"));
    }

    @Test
    public void testField() {
        CreateMeetingRequestMessage message = new CreateMeetingRequestMessage(376201686030487552L,
                "960875251", "yyy", "xxx");
        Field[] fields = message.getClass().getDeclaredFields();
        System.out.println(fields);
    }
}