package com.nuaa.automaticallygeneratenetwork.protocolXml.createXML.ftlReplace;

import com.nuaa.automaticallygeneratenetwork.protocolAcl.conversion.NetmaskUtils;
import com.nuaa.automaticallygeneratenetwork.vo.InterfaceFtl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sun.net.util.IPAddressUtil;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author YZX
 * @Create 2023-05-18 10:38
 * @Java-version jdk1.8
 */
//匹配不同厂商的网关信息，将其转化为Map键值对
@Service
@Component
public class TextMatch {

//    @Autowired
//    NetmaskUtils netmaskUtils;

    public static void main(String[] args) throws IOException {
        File file = new File("src/main/java/com/nuaa/automaticallygeneratenetwork/protocolXml/createXML/manufactureText/QR1_Router_HuaWei.txt");
        TextMatch textMatch = new TextMatch();
        Map<String, Object> map = textMatch.MatchFtl(file);
        Set<String> keySet = map.keySet();
        for (String key:keySet){
            Object o = map.get(key);
            System.out.println(key+":"+o);
            System.out.println("=========================");
        }
    }

    //正则匹配，将不同厂商的网关信息转化为Map键值对
    public Map<String, Object> MatchFtl(File file) throws IOException {

        NetmaskUtils netmaskUtils = new NetmaskUtils();

        //存储最后的结果
        Map<String, Object> dataMap = new HashMap<>();
        //拆分来获取容器名称和厂商名称
        String nameAndManufacture = file.getName().split("\\.")[0];
        //获得容器名称和厂商类型和容器类型
        String[] lxdInfo = nameAndManufacture.split("_");
        String lxdName = lxdInfo[0];
        String type = lxdInfo[1];
        String manufacture = lxdInfo[2];

        //读取文本文件
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line = null;

        //存储网卡的接口
        List<InterfaceFtl> interfaceFtlList = new ArrayList<>();
        //存储ospf的ip地址
        List<String> ospfIp = new ArrayList<>();
        //存储bgp的入口流量
        List<String> bgpInterIp = new ArrayList<>();
        //存储bgp的出口流量
        List<String> bgpExterIp = new ArrayList<>();
        //存储bgp的ip地址
        List<String> bgpIp = new ArrayList<>();
        //bgp的自治域
        String bgpAns = null;

        //定义匹配
        Matcher matcher = null;
        //定义关键字
        String keyWord = null;

        //开始循环读取匹配
        while ((line = reader.readLine())!=null){

            //实别关键字，用来获得网卡名称、bgp自治域及区分ospf和bgp
            if (line.startsWith("interface") || line.startsWith("ospf")){
                keyWord = line;
            }else if (line.startsWith("bgp")){
                bgpAns = line.split(" ")[1];//获得bgp自治域
                keyWord = line;
            }

            //匹配IP地址，表示先出现三遍【数字.】的形式，再出现一遍【数字】的形式来构成ip
            String interfaceFtlRegex = "ip address" +
                    "\\s+((?:\\d{1,3}\\.){3}\\d{1,3})"+
                    "\\s+((?:\\d{1,3}\\.){3}\\d{1,3})";
            matcher = Pattern.compile(interfaceFtlRegex).matcher(line);
            if (matcher.find()){
                //判断上一次获取的关键字是网卡接口
                if (keyWord!=null && keyWord.startsWith("interface")){
                    String name = keyWord.split(" ")[1];//获得接口名称
                    String ipAddress = matcher.group(1);//获得ip地址
                    String subnetMask = matcher.group(2);//获得子网掩码
                    InterfaceFtl interfaceFtl = new InterfaceFtl(name, ipAddress, 32-Integer.parseInt(netmaskUtils.netmaskToNum(subnetMask)));
                    interfaceFtlList.add(interfaceFtl);
                }
            }

            //匹配bgp协议的ip地址
            String bgpIpRegex = "network" +
                    "\\s+((?:\\d{1,3}\\.){3}\\d{1,3})" +
                    "\\s+((?:\\d{1,3}\\.){3}\\d{1,3})"+
                    ".*";
            matcher = Pattern.compile(bgpIpRegex).matcher(line);
            if (matcher.find()){
                //判断上一次获取的关键字bgp接口
                if (keyWord!=null && keyWord.startsWith("bgp")){
                    String ipAddress = matcher.group(1);//获得ip地址
                    String subnetMask = matcher.group(2);//获得子网掩码
                    //拼接成为ip
                    String ip = ipAddress+"/"+(32-Integer.parseInt(netmaskUtils.netmaskToNum(subnetMask)));
                    bgpIp.add(ip);
                }
            }

            //匹配bgp协议的入口流量
            String bgpInterIpRegex = "peer" +
                    "\\s+((?:\\d{1,3}\\.){3}\\d{1,3})" +
                    "\\s+last-hop-local";
            matcher = Pattern.compile(bgpInterIpRegex).matcher(line);
            if (matcher.find()){
                //判断上一次获取的关键字bgp接口
                if (keyWord!=null && keyWord.startsWith("bgp")){
                    String ip = matcher.group(1);//获得ip地址
                    bgpInterIp.add(ip);
                }
            }

            //匹配bgp协议的出口流量
            String bgpExterIpRegex = "peer" +
                    "\\s+((?:\\d{1,3}\\.){3}\\d{1,3})" +
                    "\\s+next-hop-local";
            matcher = Pattern.compile(bgpExterIpRegex).matcher(line);
            if (matcher.find()){
                //判断上一次获取的关键字bgp接口
                if (keyWord!=null && keyWord.startsWith("bgp")){
                    String ip = matcher.group(1);//获得ip地址
                    bgpExterIp.add(ip);
                }
            }

            //匹配ospf协议的ip地址
            String ospfIpRegex = "network" +
                    "\\s+((?:\\d{1,3}\\.){3}\\d{1,3})" +
                    "\\s+((?:\\d{1,3}\\.){3}\\d{1,3})"+
                    "(.*)";
            matcher = Pattern.compile(ospfIpRegex).matcher(line);
            if (matcher.find()){
                //判断上一次获取的关键字ospf接口
                if (keyWord!=null && keyWord.startsWith("ospf")){
                    String ipAddress = matcher.group(1);//获得ip地址
                    String subnetMask = matcher.group(2);//获得子网掩码
                    //拼接成为ip
                    String ip = ipAddress+"/"+(32-Integer.parseInt(netmaskUtils.netmaskToNum(subnetMask)));
                    ospfIp.add(ip);
                }
            }

        }

        dataMap.put("lxd_name",lxdName);
        dataMap.put("type",type);
        dataMap.put("interfaceFtlList",interfaceFtlList);
        dataMap.put("ospfIp",ospfIp);
        dataMap.put("ospfRedis","bgp");
        dataMap.put("bgpAns",bgpAns);
        dataMap.put("bgpInterIp",bgpInterIp);
        dataMap.put("bgpExterIp",bgpExterIp);
        dataMap.put("bgpIp",bgpIp);
        dataMap.put("bgpRedis","ospf");

        return dataMap;
    }
}
