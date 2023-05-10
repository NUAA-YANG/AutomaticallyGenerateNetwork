package com.nuaa.automaticallygeneratenetwork.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Author YZX
 * @Create 2023-05-10 11:24
 * @Java-version jdk1.8
 */
//防火墙
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Iptables")
public class Iptables {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //id，自增
    Integer id;
    //所属容器名称
    String lxdName;
    //存储的表结构，默认使用 filter 表示过滤表
    String table;
    //插入规则，常见的使用 -I 表示在最后一条插入，-A 表示在第一条插入，-D 表示删除，-R 表示修改
    String rule;
    //存储的链结构，常见的使用 INPUT 表示进入的流量
    String chain;
    /**行为属性*/
    //行为，常见的为 ACCEPT 表示接受、REJECT 表示拒绝、DROP 表示丢弃
    String judge;
    //匹配的协议类型，常见的为 ip、tcp、udp、icmp
    String protocol;
    /**源属性：其中range和eq只能出现其中一个*/
    //源IP地址【举例：单个ip为 10.250.143.31，网段为 10.250.143.31/28】
    String sIp;
    //源子网掩码
    String sNetmask;
    //源端口range关键字
    String sRange;
    //源端口起始端口
    String sStartPort;
    //源端口截止端口
    String sDestPort;
    //目的端口关键字
    String sEq;
    //目的端口号
    String sPort;
    /**目的属性：其中range和eq只能出现其中一个*/
    //目的IP地址【举例：单个ip为 10.250.143.31，网段为 10.250.143.31/28】
    String dIp;
    //目的子网掩码
    String dNetmask;
    //目的端口range关键字
    String dRange;
    //目的端口起始端口
    String dStartPort;
    //目的端口截止端口
    String dDestPort;
    //目的端口关键字
    String dEq;
    //目的端口号
    String dPort;
}
