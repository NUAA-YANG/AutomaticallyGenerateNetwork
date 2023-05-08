package com.nuaa.automaticallygeneratenetwork.protocolXml.finalHandle;

import com.nuaa.automaticallygeneratenetwork.pojo.Hosts;
import com.nuaa.automaticallygeneratenetwork.pojo.NetInterfaces;
import com.nuaa.automaticallygeneratenetwork.pojo.Routers;
import com.nuaa.automaticallygeneratenetwork.service.HostsService;
import com.nuaa.automaticallygeneratenetwork.service.NetInterfacesService;
import com.nuaa.automaticallygeneratenetwork.service.RoutersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author YZX
 * @Create 2023-04-28 16:26
 * @Java-version jdk1.8
 */
@Service
@Component
//根据数据库中读取出来的类，创建接口yaml配置文件
public class CreateYaml {

    @Autowired
    NetInterfacesService interfacesService;
    @Autowired
    GetInfo getInfo;
    @Autowired
    RoutersService routersService;
    @Autowired
    HostsService hostsService;
    @Autowired
    CreateBridge createBridge;

    /**
     * @description  根据传入拼接的接口id在本地创建网络接口配置文件
     * @date 2023/5/4 14:59
     * @params [interfacesId：拼接的接口id, lxdName：容器名称]
     * @returns void
     */
//    public void touchInterYaml(String interfacesId,String lxdName) throws IOException {
//        //创建输出流
//        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/main/java/com/nuaa/automaticallygeneratenetwork/io/netInterfaces/"+lxdName+"_10-lxc.yaml")));
//        String[] splitId = interfacesId.split(";");
//        //先写入固定的头文件
//        writer.write("network:\n" +
//                "  version: 2\n" +
//                "  ethernets:\n" +
//                "    eth0:\n" +
//                "      dhcp4: true\n" +
//                "      dhcp-identifier: mac\n");
//        writer.flush();
//        //开始写入接口描述
//        for (int i = 0 ; i<splitId.length;i++){
//            //获取每个接口信息
//            NetInterfaces netInterface = interfacesService.getById(Integer.parseInt(splitId[i]));
//            writer.write("    "+netInterface.getName()+":\n" +
//                    "      dhcp4: false\n" +
//                    "      dhcp-identifier: ["+netInterface.getIpAddress()+"/"+netInterface.getSubnetMask()+"]");
//            //如果不是最后一个接口信息，就输入换行
//            if (i!=(splitId.length-1)){
//                writer.write("\n");
//            }
//            writer.flush();
//        }
//        //防止未写入
//        writer.flush();
//        writer.close();
//    }

    /**
     * @description 根据传入的容器信息创建网络接口配置文件
     * @date 2023/5/5 10:16
     * @params [interfacesId：拼接的接口id, lxdName：容器名称]
     * @returns java.lang.String[] 第一个元素表示配置文件名称，第二个元素表示配置文件内容
     */
    public String[] touchLinuxInterYaml(Object lxd) throws IOException {
        //使用多态进行判断,保证了传入的参数简单
        String lxdName = null;
        String interfacesId = null;
        if (lxd instanceof Routers){
            lxdName = ((Routers) lxd).getName();
            interfacesId = ((Routers) lxd).getInterfacesId();
        } else if (lxd instanceof Hosts) {
            lxdName = ((Hosts) lxd).getName();
            interfacesId = ((Hosts) lxd).getInterfacesId();
        }
        String interfaceFileName = lxdName+"_10-lxc.yaml";
        StringBuffer stringBuffer = new StringBuffer();
        String[] splitId = interfacesId.split(";");
        //先写入固定的头文件
        stringBuffer.append("network:\n" +
                "  version: 2\n" +
                "  ethernets:\n" +
                "    eth0:\n" +
                "      dhcp4: true\n" +
                "      dhcp-identifier: mac\n");
        //开始写入接口描述
        for (int i = 0 ; i<splitId.length;i++){
            //获取每个接口信息
            NetInterfaces netInterface = interfacesService.getById(Integer.parseInt(splitId[i]));
            stringBuffer.append("    "+netInterface.getName()+":\n" +
                    "      dhcp4: false\n" +
                    "      addresses: ["+netInterface.getIpAddress()+"/"+netInterface.getSubnetMask()+"]");
            //如果是主机，那么需要添加网关
            if (lxd instanceof Hosts){
                stringBuffer.append("\n");
                //为主机找到网关
                NetInterfaces gateway4Inter = createBridge.matchIp(netInterface.getIpAddress(), netInterface.getSubnetMask(), interfacesService.getList());
                stringBuffer.append("      gateway4: "+gateway4Inter.getIpAddress());

            }
            //如果不是最后一个接口信息，就输入换行
            if (i!=(splitId.length-1)){
                stringBuffer.append("\n");
            }
        }
        return new String[]{interfaceFileName, String.valueOf(stringBuffer)};
    }


    //根据容器信息构造命令行对应全部容器的接口配置文件
    public List<String> createYL(String routerPathName,String hostPathName) throws IOException {
        List<String> cmds = new ArrayList<>();
        //查询当前配置文件对应的容器名称
        List<String> allHostName = getInfo.getAllLxdName(hostPathName);
        List<String> allRouterName = getInfo.getAllLxdName(routerPathName);
        //根据容器名称获得容器信息
        List<Routers> routersInfo = getInfo.getRoutersInfo(allRouterName);
        List<Hosts> hostsInfo = getInfo.getHostsInfo(allHostName);
        //构造路由器
        for (int i = 0 ; i<routersInfo.size() ; i++){
            Routers routers = routersInfo.get(i);
            //针对每个路由器生成一个文件夹
            cmds.add("mkdir -p /root/AutoNetwork/"+routers.getName());
            //开启路由器
            //cmds.add("lxc start "+routers.getName());
            //生成网口的配置文件
            String[] routerYaml = touchLinuxInterYaml(routers);
            //将命令放入到集合中
            cmds.add("echo \""+routerYaml[1]+"\" > /root/AutoNetwork/"+routers.getName()+"/10-lxc.yaml");
        }
        //构造主机
        for (int j = 0 ; j < hostsInfo.size() ; j++){
            Hosts hosts = hostsInfo.get(j);
            //针对每个主机生成一个文件夹
            cmds.add("mkdir -p /root/AutoNetwork/"+hosts.getName());
            //开启主机
            //cmds.add("lxc start "+hosts.getName());
            //生成网口的配置文件
            String[] hostYaml = touchLinuxInterYaml(hosts);
            //将创建容器的命令放入到集合中
            cmds.add("echo \""+hostYaml[1]+"\" > /root/AutoNetwork/"+hosts.getName()+"/10-lxc.yaml");
        }
        return cmds;
    }

}
