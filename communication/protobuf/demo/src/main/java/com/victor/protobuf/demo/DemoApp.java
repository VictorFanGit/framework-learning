package com.victor.protobuf.demo;

import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * 可在测试类DemoAppTest 进行相关操作
 */
public class DemoApp {
    public static void main(String[] args) {

        LoginProto.Login.Builder builder = LoginProto.Login.newBuilder();

        System.out.println("=========demo1=========");
        builder.setId(1);
        builder.setName("victor");
        builder.setPws("123");
        builder.setEmail("123@mail.com");

        LoginProto.Login login = builder.build();
        System.out.println("=====original======");
        System.out.println(login);

        System.out.println("=====encode=======");
        byte[] bytes = login.toByteArray();
        System.out.println("length: " + bytes.length);
        System.out.println(Arrays.toString(bytes));

        try {
            LoginProto.Login parsedLogin = LoginProto.Login.parseFrom(bytes);
            System.out.println("=====parsed======");
            System.out.println(parsedLogin);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        System.out.println("=========demo2===========");
        try {
            //encode
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            login.writeDelimitedTo(outputStream);
            //decode
            //the first data is the length of the buffer
            //这种方式有效的解决了socket传输时粘包、拆包的问题
            byte[] buf = outputStream.toByteArray();
            System.out.println("=============encode bytes========");
            System.out.println("length: " + buf.length);
            System.out.println(Arrays.toString(buf));
            ByteArrayInputStream inputStream = new ByteArrayInputStream(buf);
            LoginProto.Login parsedLogin = LoginProto.Login.parseDelimitedFrom(inputStream);
            System.out.println(parsedLogin);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
