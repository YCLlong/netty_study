package cn.ycl.socketexample;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MyClient {
    public static void main(String[] args) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap clientServer = new Bootstrap();
        try {
            clientServer.group(workerGroup).channel(NioSocketChannel.class).
                    handler(new LoggingHandler(LogLevel.INFO)).
                    handler(new ClientInitilizer());
            Channel channel = clientServer.connect("127.0.0.1",8090).sync().channel();
            //开启服务之后，死循环写
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (true){
                channel.writeAndFlush( br.readLine() + "\r\n");
                System.out.println("发送成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            workerGroup.shutdownGracefully();
        }
    }
}
