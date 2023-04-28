package com.nuaa.automaticallygeneratenetwork.dao;

import com.nuaa.automaticallygeneratenetwork.pojo.NetInterfaces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author YZX
 * @Create 2023-04-27 20:59
 * @Java-version jdk1.8
 */
@Repository
//接口继承实现Net接口的增删改查
public interface NetInterfacesRepository extends JpaRepository<NetInterfaces,Integer>, JpaSpecificationExecutor<NetInterfaces> {

    //根据容器名称找到全部的网卡接口
    @Query("select n from NetInterfaces n where n.lxdName=?1")
    List<NetInterfaces> listNetInterByLxdName(String lxdName);

    //根据网卡名称和容器名称找到网卡接口
    NetInterfaces findNetInterfacesByNameAndAndLxdName(String name,String lxdName);
}
