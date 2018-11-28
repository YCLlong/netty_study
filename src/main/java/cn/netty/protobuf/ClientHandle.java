package cn.netty.protobuf;

import cn.netty.protobuf.proto.DataInfo;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.Random;

public class ClientHandle extends SimpleChannelInboundHandler<DataInfo.myMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DataInfo.myMessage msg) throws Exception {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //客户端连接时，向服务器发送一个Student对象
        int rnd = new Random().nextInt(3);
        DataInfo.myMessage message = null;
        if(rnd == 0){
            DataInfo.Student student = DataInfo.Student.newBuilder()
                    .setName("燕成龙")
                    .setAge(23)
                    .setAddress("安徽")
                    .build();
            message = DataInfo.myMessage.newBuilder().setDataType(DataInfo.myMessage.DataType.StudentType).setStudent(student).build();
        }else if(rnd == 1){
            DataInfo.Persion persion = DataInfo.Persion.newBuilder()
                    .setName("燕帅龙")
                   .setCompany("ZJCA")
                    .build();
            message = DataInfo.myMessage.newBuilder().setDataType(DataInfo.myMessage.DataType.PersonType).setPersion(persion).build();
        }else if(rnd == 2){
            DataInfo.Engineer engineer = DataInfo.Engineer.newBuilder()
                    .setName("燕帅龙")
                    .setCode("softwore_101")
                    .build();
            message = DataInfo.myMessage.newBuilder().setDataType(DataInfo.myMessage.DataType.EngineerType).setEngineer(engineer).build();
        }
        ctx.writeAndFlush(message);
    }
}
