package com.nuaa.automaticallygeneratenetwork.protocolXml.finalHandle;

import com.nuaa.automaticallygeneratenetwork.pojo.Iptables;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * @Author YZX
 * @Create 2023-05-04 20:09
 * @Java-version jdk1.8
 */
public class test {
    public static void main(String[] args) throws IOException {
        File file = new File("src/main/java/com/nuaa/automaticallygeneratenetwork/protocolAcl/acl/QR3_HuaWei.txt");
        //创建读取流
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line = null;
        while ((line=reader.readLine())!=null && !"".equals(line)){
            System.out.println("内容："+line);
        }

    }
}
