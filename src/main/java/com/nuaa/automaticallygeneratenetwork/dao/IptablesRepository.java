package com.nuaa.automaticallygeneratenetwork.dao;

import com.nuaa.automaticallygeneratenetwork.pojo.Iptables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author YZX
 * @Create 2023-05-10 15:29
 * @Java-version jdk1.8
 */
@Repository
public interface IptablesRepository extends JpaRepository<Iptables,Integer>, JpaSpecificationExecutor<Iptables> {
    //根据容器名称获得全部防火墙
    @Query("select i from Iptables i where i.lxdName=?1")
    List<Iptables> listIptablesByLxdName(String lxdName);
}
