package com.nuaa.automaticallygeneratenetwork.service;

import com.nuaa.automaticallygeneratenetwork.dao.HostsRepository;
import com.nuaa.automaticallygeneratenetwork.pojo.Hosts;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author YZX
 * @Create 2023-04-28 9:55
 * @Java-version jdk1.8
 */
//实现主机的所有服务
public class HostsService {

    @Autowired
    HostsRepository hostsRepository;

    //保存主机
    public Hosts save(Hosts hosts){return hostsRepository.save(hosts);}

    //根据id找到主机
    public Hosts getById(Integer id){return hostsRepository.findById(id).get();}

    //查询所有
    public List<Hosts> getList(){return hostsRepository.findAll();}
}
