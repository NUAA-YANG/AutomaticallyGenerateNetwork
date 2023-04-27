package com.nuaa.automaticallygeneratenetwork.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Author YZX
 * @Create 2023-04-27 11:01
 * @Java-version jdk1.8
 */
//BGP路由协议
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "BGP")
public class BGP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//协议id
    private Integer ans;//自治域
    private String internal;//bgp入口流量ip地址集合,用分号拆分不同的地址
    private String external;//bgp出口流量ip地址集合,用分号拆分不同的地址
    private String redistribute;//重转发协议
    private String network;//邻接点的ip地址集合,用分号拆分不同的地址
    private String routers_name;//协议所属路由器名称
}
