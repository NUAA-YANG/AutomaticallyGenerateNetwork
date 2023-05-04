package com.nuaa.automaticallygeneratenetwork.protocolXml.preHandle;

import com.nuaa.automaticallygeneratenetwork.pojo.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author YZX
 * @Create 2023-04-27 17:09
 * @Java-version jdk1.8
 */
@Service
@Component
//用来拆分出接口、OSPF和BGP信息
public class HandleData {

    @Autowired
    HandleXml handleXml;

    public static void main(String[] args) {
        String pathName = "src/main/java/com/nuaa/automaticallygeneratenetwork/protocolXml/xml/QR1.xml";
        HandleData handleData = new HandleData();

        //测试获取接口
        List<NetInterfaces> list = handleData.HandleInter(pathName);
        list.forEach(x-> System.out.println(x));

        //测试获得ospf协议
//        OSPF ospf = handleData.HandleOspf(pathName);
//        System.out.println(ospf);
//
//        //测试获得bgp协议
//        BGP bgp = handleData.HandleBgp(pathName);
//        System.out.println(bgp);

    }


    //处理接口
    public List<NetInterfaces> HandleInter(String pathName){
        //存放结果
        List<NetInterfaces> result = new ArrayList<>();
        //获取处理的xml文件
        Map<String, String> xmlFile = handleXml.readXMLFile(pathName);
        //获取接口所属容器名称
        String lxd_name = xmlFile.get("lxd_name");
        //获取接口的处理信息
        String interfaces = xmlFile.get("interfaces");
        //拆分完的接口信息，第一个是空格，所以i从1开始循环
        String[] split = interfaces.split("\\s+");
        for (int i = 1 ; i<=split.length; i++){
            //每三个词条封装一次接口(因为从0开始，所以先加一)
            if (i%3==0){
                String name = split[i-2];//接口名称
                String ipAddress = split[i-1];//接口ip地址
                Integer subnetMask = Integer.parseInt(split[i]);//接口子网掩码
                NetInterfaces inter = new NetInterfaces(0,name,ipAddress,subnetMask,lxd_name,0);
                result.add(inter);
            }
        }
        return result;
    }

    //处理ospf协议
    public OSPF HandleOspf(String pathName){
        //获取处理的xml文件
        Map<String, String> xmlFile = handleXml.readXMLFile(pathName);
        //获取协议所属容器名称
        String lxd_name = xmlFile.get("lxd_name");
        //如果是主机配置文件，则没有协议
        if (xmlFile.get("ospf")==null){
            return null;
        }
        //获取存储网络邻接点信息->将信息中的空格全部替换为";"
        String network = xmlFile.get("ospf_network").replaceAll("\\s+",";");
        OSPF ospf = new OSPF(0,network,lxd_name);
        return ospf;
    }

    //处理bgp协议
    public BGP HandleBgp(String pathName){
        //获取处理的xml文件
        Map<String, String> xmlFile = handleXml.readXMLFile(pathName);
        //获取协议所属容器名称
        String lxd_name = xmlFile.get("lxd_name");
        //如果是主机配置文件，则没有协议
        if (xmlFile.get("bgp")==null){
            return null;
        }
        //获取协议所属自治域
        Integer ans = Integer.parseInt(xmlFile.get("ans"));
        //获取协议所属重转发网络
        String redistribute = xmlFile.get("redistribute");
        //获取入口流量ip信息->将信息中的空格全部替换为";"
        String internal = xmlFile.get("internal").replaceAll("\\s+",";");
        //获取出口流量ip信息->将信息中的空格全部替换为";"
        String external = xmlFile.get("external").replaceAll("\\s+",";");
        //获取存储网络邻接点信息->将信息中的空格全部替换为";"
        String network = xmlFile.get("bgp_network").replaceAll("\\s+",";");
        BGP bgp = new BGP(0, ans, internal, external, redistribute, network, lxd_name);
        return bgp;
    }

}
