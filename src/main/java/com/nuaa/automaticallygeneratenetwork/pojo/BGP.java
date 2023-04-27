package com.nuaa.automaticallygeneratenetwork.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * @Author YZX
 * @Create 2023-04-27 11:01
 * @Java-version jdk1.8
 */
//BGP路由协议
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BGP {
    private Integer id;//协议id
    private Integer ans;//自治域
    private ArrayList<String> internal;//bgp入口流量ip地址集合
    private ArrayList<String> external;//bgp出口流量ip地址集合
    private String redistribute;//重转发协议
    private Integer routers_id;//协议所属路由器id
}
