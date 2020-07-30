package com.victor.demo;

import com.victor.demo.thrift.QryResult;
import com.victor.demo.thrift.TestQry;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class ThriftClientDemo {
    private final static int DEFAULT_QRY_CODE = 1;

    public static void main(String[] args) {
        try {
            TTransport tTransport = getTTransport();
            TProtocol protocol = new TBinaryProtocol(tTransport);
            TestQry.Client client = new TestQry.Client(protocol);
            System.out.println("try to invoke the remote method...");
            long invokeNum = 0;
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                QryResult result = client.qryTest(i);
//                if (result.code == 1) {
//                    invokeNum++;
//                }
//                System.out.println("code=" + result.code + ";msg=" + result.msg);
            }
            long endTime = System.currentTimeMillis();
//            System.out.println("invoke num is:" + invokeNum);
            System.out.println("time:" + (endTime - startTime));
            System.out.println("num peer sec:" + 10000000/(endTime - startTime));
            tTransport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TTransport getTTransport() throws Exception {
        try {
            TTransport tTransport = createTTransport("127.0.0.1", 30001, 5000);
            if (!tTransport.isOpen()) {
                tTransport.open();
            }
            return tTransport;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static TTransport createTTransport(String host, int port, int timeout) {
        final TSocket tSocket = new TSocket(host, port, timeout);
        final TTransport transport = new TFramedTransport(tSocket);
        return transport;
    }
}
