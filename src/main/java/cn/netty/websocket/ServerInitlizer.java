package cn.netty.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ServerInitlizer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());//websocket基于http协议
        pipeline.addLast(new ChunkedWriteHandler());//不知道
        pipeline.addLast(new HttpObjectAggregator(8192));//不知道
        //ws:host:port/webSocketPath
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));//websocket协议处理


    }
}
