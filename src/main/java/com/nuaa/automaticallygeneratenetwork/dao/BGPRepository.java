package com.nuaa.automaticallygeneratenetwork.dao;

import com.nuaa.automaticallygeneratenetwork.pojo.BGP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @Author YZX
 * @Create 2023-04-27 15:05
 * @Java-version jdk1.8
 */
@Repository
//接口继承实现BGP的增删改查
public interface BGPRepository extends JpaRepository<BGP,Integer>, JpaSpecificationExecutor<BGP> {
    //根据协议所属路由器名称找到BGP协议
    BGP findBGPByRoutersName(String routersName);
}
