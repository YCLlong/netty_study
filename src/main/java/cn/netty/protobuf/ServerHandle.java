package cn.netty.protobuf;

import cn.netty.protobuf.proto.DataInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandle extends SimpleChannelInboundHandler<DataInfo.myMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DataInfo.myMessage msg) throws Exception {
        DataInfo.myMessage.DataType dataType = msg.getDataType();
        if(dataType == DataInfo.myMessage.DataType.StudentType){
            DataInfo.Student student = msg.getStudent();
            System.out.println(student.getName());
            System.out.println(student.getAge());
            System.out.println(student.getAddress());
        }else if(dataType ==  DataInfo.myMessage.DataType.PersonType){
            DataInfo.Persion persion = msg.getPersion();
            System.out.println(persion.getName());
            System.out.println(persion.getCompany());
        }else if(dataType == DataInfo.myMessage.DataType.EngineerType){
            DataInfo.Engineer engineer = msg.getEngineer();
            System.out.println(engineer.getName());
            System.out.println(engineer.getCode());
        }
    }
}
