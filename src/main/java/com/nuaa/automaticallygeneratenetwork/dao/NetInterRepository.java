package com.nuaa.automaticallygeneratenetwork.dao;

import com.nuaa.automaticallygeneratenetwork.pojo.NetInterfaces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author YZX
 * @Create 2023-04-27 20:59
 * @Java-version jdk1.8
 */
//接口继承实现Net接口的增删改查
public interface NetInterRepository extends JpaRepository<NetInterfaces,Integer> {

    //根据容器名称找到全部的网卡接口
    @Query("select n from NetInterfaces n where n.lxd_name=?1")
    List<NetInterfaces> listNetInterByLxd_name(String lxd_name);

    //根据网卡名称和容器名称找到网卡接口
    NetInterfaces findNetInterfacesByNameAndAndLxd_name(String name,String lxd_name);
}
