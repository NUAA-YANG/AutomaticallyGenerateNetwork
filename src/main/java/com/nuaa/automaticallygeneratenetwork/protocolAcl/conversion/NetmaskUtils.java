package com.nuaa.automaticallygeneratenetwork.protocolAcl.conversion;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Author YZX
 * @Create 2023-05-10 16:25
 * @Java-version jdk1.8
 */
//ip工具类
@Service
@Component
public class NetmaskUtils {

    //将ACL中点进制的子网掩码字符串转化为数字【ACL的子网掩码0表示匹配，1表示不匹配】
    //所以转化为子网掩码之后要用32减一下
    public String netmaskToNum(String netmask){
        int result = 0 ;
        //基于.进行拆分[注意要进行转义]
        String[] splitMask = netmask.split("\\.");
        for (int i = 0; i<splitMask.length;i++){
            String binaryMask = Integer.toBinaryString(Integer.parseInt(splitMask[i]));
            result+=calculateOne(binaryMask);
        }
        return String.valueOf((32-result));
    }





    //计算字符串中有多少个连续的1（用来判断子网掩码）
    public int calculateOne(String str){
        int one = 0 ;
        for (int i = 0 ;i<str.length();i++){
            if (str.charAt(i)=='0'){
                break;
            }else {
                one++;
            }
        }
        return one;
    }

}
