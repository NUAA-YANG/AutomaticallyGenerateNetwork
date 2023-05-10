package com.nuaa.automaticallygeneratenetwork.service;

import com.nuaa.automaticallygeneratenetwork.dao.IptablesRepository;
import com.nuaa.automaticallygeneratenetwork.pojo.Iptables;
import com.nuaa.automaticallygeneratenetwork.pojo.NetInterfaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Author YZX
 * @Create 2023-05-10 15:32
 * @Java-version jdk1.8
 */
//实现防火墙所有服务
@Service
@Component
public class IptablesService {
    @Autowired
    IptablesRepository iptablesRepository;

    //保存
    @Transactional
    public Iptables save(Iptables iptables){return iptablesRepository.save(iptables);}

    //批量保存
    @Transactional
    public List<Iptables> saveList(List<Iptables> iptablesList){return iptablesRepository.saveAll(iptablesList);}

    //获取全部的防火墙信息
    @Transactional
    public List<Iptables> getList(){return iptablesRepository.findAll();}

    //根据id获得全部防火墙
    @Transactional
    public Iptables getById(Integer id){return iptablesRepository.findById(id).get();}

    //根据容器名称获得全部防火墙
    @Transactional
    public List<Iptables> getListByLxdName(String lxdName){
        return iptablesRepository.listIptablesByLxdName(lxdName);
    }
}
