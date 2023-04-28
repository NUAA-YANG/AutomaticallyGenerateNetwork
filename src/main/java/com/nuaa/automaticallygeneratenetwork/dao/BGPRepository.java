package com.nuaa.automaticallygeneratenetwork.dao;

import com.nuaa.automaticallygeneratenetwork.pojo.BGP;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author YZX
 * @Create 2023-04-27 15:05
 * @Java-version jdk1.8
 */
//接口继承实现BGP的增删改查
public interface BGPRepository extends JpaRepository<BGP,Integer>{
    //根据协议所属路由器名称找到BGP协议
    BGP findBGPByRouters_name(String routers_name);
}
