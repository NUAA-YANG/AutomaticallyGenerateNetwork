package com.nuaa.automaticallygeneratenetwork.dao;

import com.nuaa.automaticallygeneratenetwork.pojo.Hosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @Author YZX
 * @Create 2023-04-27 20:59
 * @Java-version jdk1.8
 */
@Repository
//接口继承实现Hosts的增删改查
public interface HostsRepository extends JpaRepository<Hosts,Integer>, JpaSpecificationExecutor<Hosts> {

    //根据主机名称查询
    Hosts findHostsByName(String name);


}
