package cn.netty.protobuf;

import cn.netty.protobuf.proto.DataInfo;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.Arrays;

public class UseTest {
    public static void main(String[] args) {
    //将student对象序列化成字节数组
        DataInfo.Student student = DataInfo.Student.newBuilder()
                .setName("燕成龙")
                .setAge(22)
                .setAddress("安徽省 安庆市")
                .build();
        byte[] studentByteArr = student.toByteArray();
        //测试加一个字节是否会序列化失败
        byte[] studebtCopy = new byte[studentByteArr.length + 1];
        studebtCopy = Arrays.copyOf(studentByteArr,studebtCopy.length);
        studebtCopy[studentByteArr.length]=10;
        for(byte bt : studebtCopy){
            System.out.print(bt + " ");
        }
        System.out.println();
        DataInfo.Student student1 = null;
        try {
            //将字节数组生成对象，这里用测试的字节数组，会抛出异常
            student1 = DataInfo.Student.parseFrom(studebtCopy);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        if(student1 != null){
            System.out.println(student1.getName());
            System.out.println(student1.getAge());
            System.out.println(student1.getAddress());
        }
    }
}
