package com.nuaa.automaticallygeneratenetwork.protocolXml.finalHandle;

import com.nuaa.automaticallygeneratenetwork.pojo.NetInterfaces;
import com.nuaa.automaticallygeneratenetwork.service.NetInterfacesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.util.*;

/**
 * @Author YZX
 * @Create 2023-05-04 17:12
 * @Java-version jdk1.8
 */
@Service
@Component
//创建网桥以及命令
public class CreateBridge {

    @Autowired
    NetInterfacesService netInterfacesService;
    @Autowired
    GetInfo getInfo;

    public static void main(String[] args) {
        CreateBridge createBridge = new CreateBridge();

        //测试匹配ip端口
        List<NetInterfaces> list = new ArrayList<>();
        list.add(new NetInterfaces(1,"1","192.168.80.1",24,"1",0));
        list.add(new NetInterfaces(2,"2","192.168.80.2",24,"2",0));
        list.add(new NetInterfaces(3,"3","192.168.83.1",24,"3",0));
        list.add(new NetInterfaces(4,"4","192.168.83.2",24,"4",0));
        list.add(new NetInterfaces(5,"5","192.168.82.2",24,"5",0));
        list.add(new NetInterfaces(6,"6","192.168.82.4",24,"6",0));
        list.add(new NetInterfaces(7,"7","192.168.84.1",24,"7",0));
        System.out.println(createBridge.matchIp("192.168.84.1", 24, list));
    }


    /**
     * @description  创建命令行用于构建网桥以及连接网桥
     * @date 2023/5/10 10:25
     * @params [routerPathName, hostPathName]
     * @returns java.util.List<java.util.List<java.lang.String>> 返回[0]号元素是网桥的创建命令，返回[1]号元素是所有网桥的名称
     */
    public List<String> CreateAndAttachBridge(String routerPathName,String hostPathName) throws Exception {
        //返回网桥的创建语句
        List<String> cmds = new ArrayList<>();
        //1. 获取全部的接口以及当前配置文件对应的容器信息
        List<NetInterfaces> list = netInterfacesService.getList();
        //查询当前配置文件对应的容器名称
        List<String> allHostName = getInfo.getAllLxdName(hostPathName);
        List<String> allRouterName = getInfo.getAllLxdName(routerPathName);
        //2. 接口信息匹配
        List<String> bridge = new ArrayList<>();//用来记录网桥名称信息
        for (NetInterfaces n:list){
            NetInterfaces searchInter = matchIp(n.getIpAddress(), n.getSubnetMask(), list);
            if (searchInter==null){
                throw new Exception("端口匹配失败");
            }
            //只有两个接口所属的容器名称和当前容器配置文件名称对应,才可以创建网桥
            if ((allHostName.contains(n.getLxdName()) || allRouterName.contains(n.getLxdName())) &&
                    (allHostName.contains(searchInter.getLxdName()) || allRouterName.contains(searchInter.getLxdName())) ){
                //创建网桥的名称
                String bridgeName = n.getLxdName().compareTo(searchInter.getLxdName())<=0?(n.getLxdName()+searchInter.getLxdName()):(searchInter.getLxdName()+n.getLxdName());
                //如果两个接口对应的容器名称都不存在
                if (!bridge.contains(bridgeName)){
                    bridge.add(bridgeName);//添加网桥名称
                    //3. 创建网桥
                    cmds.add("lxc network create "+bridgeName+" ipv6.address=none ipv4.address=none;");
                    //4. 连接网桥(参数分别为网桥名称、容器名称以及容器网卡名称)
                    cmds.add("lxc network attach "+bridgeName+" "+n.getLxdName()+" "+n.getName()+";");
                    cmds.add("lxc network attach "+bridgeName+" "+searchInter.getLxdName()+" "+searchInter.getName()+";");
                }
            }
        }
        return cmds;
    }

    /***
     * @description 匹配ip接口，用来搜索网桥两端的接口
     * @date 2023/5/4 19:30
     * @params [ip：需要搜索的ip地址, netMask：需要搜索的ip地址的子网掩码, list：带搜索的列表]
     * @returns java.lang.Integer ：返回搜索到的ip的id
     */
    public NetInterfaces matchIp(String ip,Integer netMask,List<NetInterfaces> list){
        for (int i = 0 ; i<list.size() ; i++){
            NetInterfaces netInterfaces = list.get(i);
            //掩码相同才能继续比较
            if (netInterfaces.getSubnetMask()==netMask){
                String searchIp = netInterfaces.getIpAddress();//待匹配的ip地址
                //首先要保证匹配的是除自己之外的ip地址
                if (!ip.equals(searchIp)){
                    //如果掩码对应的ip完全一样，那么则表示匹配成功
                    if (compareIp(ip,searchIp,netMask)==0){
                        return netInterfaces;
                    }
                }
            }
        }
        //否则表示没找到对应的ip地址
        return null;
    }

    
    /**
     * @description 比较两个指定掩码的ip地址是否在同一网段
     * @date 2023/5/4 19:50
     * @params [ip：比较ip, searchIp：待比较ip, netMask：掩码]
     * @returns int：0表示在同一个网段，-1和1表示不在同一个网段
     */
    public int compareIp(String ip,String searchIp,Integer netMask){
        //如果掩码数量小于等于8，直接切割比较
        if (netMask<=8){
            return changeIp(ip).substring(0,netMask).compareTo(changeIp(searchIp).substring(0,netMask));
        } else if (netMask>8 && netMask<=16) {
            //如果掩码数量9-16，因为要添加一个“.”，那么长度+1
            return changeIp(ip).substring(0,netMask+1).compareTo(changeIp(searchIp).substring(0,netMask+1));
        } else if (netMask>16 && netMask<=24) {
            //如果掩码数量17-24，因为要添加两个“.”，那么长度+2
            return changeIp(ip).substring(0,netMask+2).compareTo(changeIp(searchIp).substring(0,netMask+2));
        }else {
            //如果掩码数量25-32，因为要添加三个“.”，那么长度+3
            return changeIp(ip).substring(0,netMask+2).compareTo(changeIp(searchIp).substring(0,netMask+3));
        }
    }


    //将ip地址转化为二进制ip地址，且不满足八位则补零
    public String changeIp(String ip){
        String[] splitIp = ip.split("\\.");
        //变化为二进制的ip地址
        String ipChange = "";
        //拼接ip地址
        for (int i=0 ; i<splitIp.length;i++){
            //转化为二进制
            String ip1 =Integer.toBinaryString(Integer.parseInt(splitIp[i]));
            //判断，如果不满足八位则补零
            if (ip1.length()<8){
                ip1 = String.format("%08d",Integer.parseInt(ip1));
            }
            ipChange+=ip1;
            //如果不是最后一位，则添加“.”来区分地址
            if (i!=splitIp.length-1){
                ipChange+=".";
            }
        }
        return ipChange;
    }
}
