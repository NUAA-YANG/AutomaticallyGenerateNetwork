package com.nuaa.automaticallygeneratenetwork.protocolXml.finalHandle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * @Author YZX
 * @Create 2023-05-04 20:09
 * @Java-version jdk1.8
 */
public class test {
    public static void main(String[] args) {
        System.out.println(String.format("%04d", 1));

        //复制列表
        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("哼哼");
        arrayList1.add("小哈");
        arrayList1.add("开心");
        System.out.println(arrayList1);
        arrayList1.remove("小哈");
        System.out.println(arrayList1);
//        ArrayList<String> arrayList2 = new ArrayList<>(Arrays.asList(new String[arrayList1.size()]));
//        Collections.copy(arrayList2,arrayList1);
//        System.out.println(arrayList2);

        String[] split = ";192.168.81.1/24;192.168.83.1/24;".split(";");
        System.out.println(split.length);
        System.out.println(Arrays.toString(split));


        System.out.println("echo \""+"#!/bin/bash"+"\" >> /root/AutoNetwork/LxdBash.sh");

    }
}
