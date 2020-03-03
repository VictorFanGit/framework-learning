package com.victor.cglib;

import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibTest {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Student.class);
        Callback interceptor = new MethodInterceptor() {

            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("original method name: " + method.getName());
                System.out.println("my name is " + (String) methodProxy.invokeSuper(o, args));
                System.out.println("invoke end");
                return null;
            }
        };
        enhancer.setCallback(interceptor);
        Student student = (Student) enhancer.create();
        student.getName();
    }
}
