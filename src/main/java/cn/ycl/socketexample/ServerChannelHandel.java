package cn.ycl.socketexample;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Date;

/**
 * 服务器做的事情：
 * 1，接收到客户端连接之后，会提示别的客户端，ip + port 上线
 * 2,接收到客户端消息之后，将消息发送给别的客户端，同时也发一条消息给自己
 * 3，当客户端断开连接之后，将消息发送给别的客户端，提示 ip + port 离线
 *
 */
public class ServerChannelHandel extends SimpleChannelInboundHandler<String> {
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("读取到消息：" + msg);
        Channel channel = ctx.channel();
        //读取到某个客户端事件时，将读取到的消息发送到别的客户端
        //遍历ChannelGroup
        channelGroup.forEach(ch -> {
            if(ch == channel){
                //自己
                ch.writeAndFlush("来自【自己】的消息：" + msg + "\n");
            }else {
                ch.writeAndFlush("来自【"+ ch.remoteAddress() +"】的消息：" + msg + "\n");
            }
        });
    }

    //当又新的客户端连接时，会触发这个事件
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //这个会向Channel组内所有的Channel发送消息
        channelGroup.writeAndFlush(channel.remoteAddress() + "上线" + new Date() +"\n" );
        //将当前新的连接加入到Channel中
        channelGroup.add(channel);
        System.out.println("channelRegistered -> channelGroup size:" + channelGroup.size());
    }

    //当有客户端断开连接时
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        //触发这个事件时，当前channel 已经不在 Channel组内
        channelGroup.writeAndFlush(ctx.channel().remoteAddress() + "下线" + new Date() +"\n" );
        System.out.println("channelUnregistered -> channelGroup size:" + channelGroup.size());
    }

    /**
     * 用户事件触发
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //这个事件就是 IdleStateHandler 这个通道处理器触发的，同时把事件对象传到这个方法中
        Channel channel = ctx.channel();
        if(evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent)evt;
            if(idleStateEvent.isFirst()){
                System.out.println(channel.remoteAddress() + "第一次被调用");
            }
            IdleState state = idleStateEvent.state();
            switch (state){
                case READER_IDLE:
                    System.out.println(channel.remoteAddress() + "读空闲");
                    break;
                case WRITER_IDLE:
                    System.out.println(channel.remoteAddress() + "写空闲");
                    break;
                case ALL_IDLE:
                    System.out.println(channel.remoteAddress() + "读写空闲");
            }
            //对应的空闲状态下做一些义务处理，比如读写空闲时，可以发送心跳包，检测客户端是否断开连接
        }
    }

    //发生异常时，触发
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
