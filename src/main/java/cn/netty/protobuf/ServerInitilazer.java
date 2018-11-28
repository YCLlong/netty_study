package cn.netty.protobuf;

import cn.netty.protobuf.proto.DataInfo;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class ServerInitilazer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //添加protobuf 需要的处理器
        ChannelPipeline pipeline = ch.pipeline();
        //解码器，将看不懂的字节数组转换成 看的懂的对象就是解码，传入的参数也就是解码后的对象
        pipeline.addLast(new ProtobufDecoder(DataInfo.myMessage.getDefaultInstance()));
        pipeline.addLast(new ProtobufEncoder());
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());

        //添加自己的处理器
        pipeline.addLast(new ServerHandle());
    }
}
