package com.victor.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Test {
    public static void main(String[] args) {
        /*List<String> list = new ArrayList<>(Arrays.asList("a","b","c"));
        list.add("aa");
        System.out.println(list.size());
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if(next.equals("aa")) {
                it.remove();
            }
        }

        System.out.println("List size: " + list.size());

        int i = 0xdd;
        System.out.println(i);
        System.out.println(Integer.toBinaryString(i));
        int i1 = i >>> 1;
        System.out.println(Integer.toBinaryString(i1));
        System.out.println(i1);

        System.out.println("-----------------");
        byte j = (byte)0xff;
        System.out.println(j);
        byte j1 = (byte)(j >> 1);
        System.out.println(j1);*/

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);


    }
}
