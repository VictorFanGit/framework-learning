package com.victor.demo;

import com.victor.demo.thrift.QryResult;
import com.victor.demo.thrift.TestQry;
import org.apache.thrift.TException;

public class QueryImp implements TestQry.Iface {
    @Override
    public QryResult qryTest(int qryCode) throws TException {
        QryResult result = new QryResult();
        if (1 == qryCode) {
            result.code = 1;
            result.msg = "success";
        } else {
            result.code = 0;
            result.msg = "fail";
        }
        return result;
    }
}
