package com.nuaa.automaticallygeneratenetwork.protocolAcl.conversion;

import com.nuaa.automaticallygeneratenetwork.pojo.Iptables;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author YZX
 * @Create 2023-05-10 16:04
 * @Java-version jdk1.8
 */
//将Iptables类转化为Iptables命令
@Service
@Component
public class IptablesToLine {


    //将Iptables类取出转化为命令行
    public String turnIptablesToLine(Iptables iptables){
        StringBuffer sb = new StringBuffer("iptables -t");
        //添加插入表【iptables -t filter】
        sb.append(" "+iptables.getTableName());
        //添加插入规则【iptables -t filter -I INPUT】
        sb.append(" -"+iptables.getRule()+" "+iptables.getChain());

        //添加ip地址循环标识，源ip和目的ip有一个为any就添加循环
        if ((iptables.getDIp()!=null && iptables.getDIp().equals("any"))
                || (iptables.getSIp()!=null && iptables.getSIp().equals("any"))){
            sb.append(" -m iprange");
        }

        //添加源地址范围,ip不为空，即值为any或者任意ip地址
        if (iptables.getSIp()!=null){
            if (!"any".equals(iptables.getSIp())){
                // ip为数字，掩码不为空，指定网段
                if (iptables.getSNetmask()!=null){
                    //给了指定的范围，添加源地址和子网掩码
                    sb.append(" -s "+iptables.getSIp()+"/"+iptables.getSNetmask());
                }else {
                    // ip为数字，掩码为空，指定单个ip
                    sb.append(" -s "+iptables.getSIp());
                }
            }else {
                //ip为any，允许所有源ip，直接进行添加
                sb.append(" --src-range 0.0.0.0-255.255.255.255");
            }
        }

        //添加目的地址范围，ip不为空，即值为any或者任意ip地址
        if (iptables.getDIp()!=null){
            if (!"any".equals(iptables.getDIp())){
                // ip为数字，掩码不为空，指定网段
                if (iptables.getDNetmask()!=null){
                    //给了指定的范围，添加源地址和子网掩码
                    sb.append(" -s "+iptables.getDIp()+"/"+iptables.getDNetmask());
                }else {
                    // ip为数字，掩码为空，指定单个ip
                    sb.append(" -s "+iptables.getDIp());
                }
            }else {
                //允许所有目的ip，直接进行添加
                sb.append(" --dst-range 0.0.0.0-255.255.255.255");
            }
        }

        //添加协议以及端口(如果存在协议且协议不为ip协议，即tcp、udp及其他协议)
        if (iptables.getProtocol() != null && !"ip".equals(iptables.getProtocol())) {
            sb.append(" -p "+iptables.getProtocol());
            //添加协议端口
            //端口分为源连续端口(sRange)、目的连续端口(dRange)、源离散端口(sPort)、目的离散端口(sPort)
            if (iptables.getSRange()!=null){
                sb.append(" --sport "+iptables.getSStartPort()+":"+iptables.getSDestPort());
            }
            if (iptables.getSEq()!=null){
                //eq表示端口等于某个值
                if ("eq".equals(iptables.getSEq())){
                    sb.append(" --sport "+iptables.getSPort());
                }
                //ge表示端口大于某个值
                if ("ge".equals(iptables.getSEq())){
                    sb.append(" --sport "+iptables.getSPort()+":");
                }
                //le表示端口小于某个值
                if ("le".equals(iptables.getSEq())){
                    sb.append(" --sport "+":"+iptables.getSPort());
                }
            }
            if (iptables.getDRange()!=null){
                sb.append(" --dport "+iptables.getDStartPort()+":"+iptables.getDDestPort());
            }
            if (iptables.getDEq()!=null){
                //eq表示端口等于某个值
                if ("eq".equals(iptables.getDEq())){
                    sb.append(" --dport "+iptables.getDPort());
                }
                //ge表示端口大于某个值
                if ("ge".equals(iptables.getDEq())){
                    sb.append(" --dport "+iptables.getDPort()+":");
                }
                //le表示端口小于某个值
                if ("le".equals(iptables.getDEq())){
                    sb.append(" --dport "+":"+iptables.getDPort());
                }
            }
        }

        //添加行为规则
        if (iptables.getJudge()!=null){
            if ("permit".equals(iptables.getJudge())){
                sb.append(" -j ACCEPT;");
            }else {
                sb.append(" -j DROP;");
            }
        }
        return new String(sb);
    }


    //将Iptables类集合转化为Iptables语句集合，返回Iptables语句集合
    public List<String> turnToLine(String lxcName,List<Iptables> iptablesList){
        List<String> cmds = new ArrayList<>();
        for (int i = 0; i < iptablesList.size(); i++) {
            Iptables iptables = iptablesList.get(i);
            //将每个类转化为语句
            String line = turnIptablesToLine(iptables);
            cmds.add("lxc exec "+lxcName+" -- "+line);
        }
        return cmds;
    }
}
