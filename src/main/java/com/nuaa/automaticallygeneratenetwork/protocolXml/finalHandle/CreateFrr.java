package com.nuaa.automaticallygeneratenetwork.protocolXml.finalHandle;

import com.nuaa.automaticallygeneratenetwork.pojo.BGP;
import com.nuaa.automaticallygeneratenetwork.pojo.OSPF;
import com.nuaa.automaticallygeneratenetwork.pojo.Routers;
import com.nuaa.automaticallygeneratenetwork.service.BGPService;
import com.nuaa.automaticallygeneratenetwork.service.OSPFService;
import com.nuaa.automaticallygeneratenetwork.service.RoutersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author YZX
 * @Create 2023-05-05 11:32
 * @Java-version jdk1.8
 */
//创建frr.config配置文件
@Service
@Component
public class CreateFrr {

    @Autowired
    OSPFService ospfService;
    @Autowired
    BGPService bgpService;
    @Autowired
    GetInfo getInfo;
    @Autowired
    RoutersService routersService;


    /**
     * @description 根据传入的路由器构造frr描述文件
     * @date 2023/5/5 15:36
     * @params [routers] 传入的路由器对象
     * @returns java.lang.String[] 返回数组,第一个元素表示配置文件名称,第二个元素表示配置文件内容
     */
    public String[] touchLinuxFrrConfig(Routers routers){
        String frrFileName = routers.getName()+"_frr.config";
        StringBuffer stringBuffer = new StringBuffer();
        //1. 获得bgp协议
        BGP bgp = bgpService.getById(routers.getBgpId());
        String[] externalIp = bgp.getExternal().split(";");
        String[] internalIp = bgp.getInternal().split(";");
        String[] bgpNetworkIp = bgp.getNetwork().split(";");
        //1.1 开始遍历出入口流量
        stringBuffer.append("!\n" +
                "router bgp 100\n" +
                "no bgp ebgp-requires-policy\n");
        for (String eI:externalIp){
            if (!eI.equals("")){
                stringBuffer.append("neighbor "+eI+" remote-as external\n");
            }
        }
        for (String iI:internalIp){
            if (!iI.equals("")){
                stringBuffer.append("neighbor "+iI+" remote-as internal\n");
            }
        }
        stringBuffer.append("!\n" +
                "address-family ipv4 unicast\n");
        //1.2 开始遍历邻接ip
        for (String bNI:bgpNetworkIp){
            if (!bNI.equals("")){
                stringBuffer.append("network "+bNI+"\n");
            }
        }
        //1.3 添加重转发协议
        stringBuffer.append("redistribute "+bgp.getRedistribute()+"\n" +
                "exit-address-family\n" +
                "exit\n" +
                "!\n");
        //2. 获得ospf协议
        OSPF ospf = ospfService.getById(routers.getOspfId());
        String[] ospfNetworkIp = ospf.getNetwork().split(";");
        stringBuffer.append("router ospf\n");
        for (String oNI:ospfNetworkIp){
            if (!oNI.equals("")){
                stringBuffer.append("network "+oNI+" area 0.0.0.0\n");
            }
        }
        stringBuffer.append("exit\n" +
                "!\n" +
                "segment-routing\n" +
                "traffic-eng\n" +
                "exit\n" +
                "exit\n" +
                "!");
        return new String[]{frrFileName, String.valueOf(stringBuffer)};
    }

    /**
     * @description 根据传入的路由器信息构造出所有的frr配置文件
     * @date 2023/5/6 10:04
     * @params [routersList]：传入的路由器信息
     * @returns java.util.List<java.lang.String>：返回命令集合列表
     */
    public List<String> createFR(String routerPathName){
        List<String> cmds = new ArrayList<>();
        //查询当前配置文件对应的容器名称
        List<String> allRouterName = getInfo.getAllLxdName(routerPathName);
        //根据容器名称获得路由器信息
        List<Routers> routersInfo = getInfo.getRoutersInfo(allRouterName);
        //先创建文件夹
        cmds.add("mkdir /root/AutoNetwork/frr");
        //构造路由器frr文件
        for (int i = 0 ; i<routersInfo.size() ; i++){
            Routers routers = routersInfo.get(i);
            //frr文件
            String[] routerFrr = touchLinuxFrrConfig(routers);
            //将创建的命令放到集合中
            cmds.add("echo \""+routerFrr[1]+"\" > /root/AutoNetwork/frr/"+routerFrr[0]);
        }
        return cmds;
    }
}
