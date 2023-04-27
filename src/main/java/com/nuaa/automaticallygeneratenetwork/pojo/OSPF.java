package com.nuaa.automaticallygeneratenetwork.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * @Author YZX
 * @Create 2023-04-27 10:55
 * @Java-version jdk1.8
 */
//ospf路由协议
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OSPF {
    private Integer id;//协议id
    private ArrayList<String> neighbor;//邻接点的ip地址集合
    private Integer routers_id;//协议所属路由器id
}
