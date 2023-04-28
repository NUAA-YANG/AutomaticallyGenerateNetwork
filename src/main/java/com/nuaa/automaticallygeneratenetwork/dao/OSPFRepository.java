package com.nuaa.automaticallygeneratenetwork.dao;

import com.nuaa.automaticallygeneratenetwork.pojo.BGP;
import com.nuaa.automaticallygeneratenetwork.pojo.OSPF;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author YZX
 * @Create 2023-04-27 20:59
 * @Java-version jdk1.8
 */
//接口继承实现OSPF的增删改查
public interface OSPFRepository extends JpaRepository<OSPF,Integer> {
    //根据协议所属路由器名称找到OSPF协议
    OSPF findOSPFByRouters_name(String routers_name);
}
