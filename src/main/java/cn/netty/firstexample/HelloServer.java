package cn.netty.firstexample;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *
 * 作用，接收到客户端的任何请求，返回一个hello world
 */
public class HelloServer {
    /**
     * 事件循环线程租，可以理解程一个死循环
     */
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();//老板，不断的从客户端接收连接，不做任何事情
        EventLoopGroup workerGroup = new NioEventLoopGroup();//工人，将连接交给工人，工人做具体的业务逻辑操作
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();//启动服务端的简单的封装类，可以看作开启服务的入口
            serverBootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class).
                    childHandler(new HelloServerInitializer());
            ChannelFuture future = serverBootstrap.bind(8090).sync();
            System.out.println("服务器启动成功，监听端口：" + 8090);
            future.channel().closeFuture().sync();
            System.out.println("服务关闭");
        }catch (Exception e){

        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
