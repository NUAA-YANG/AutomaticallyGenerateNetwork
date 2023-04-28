package com.nuaa.automaticallygeneratenetwork.service;

import com.nuaa.automaticallygeneratenetwork.dao.NetInterRepository;
import com.nuaa.automaticallygeneratenetwork.pojo.NetInterfaces;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author YZX
 * @Create 2023-04-28 10:04
 * @Java-version jdk1.8
 */
//实现网口的所有服务
public class NetInterService {
    @Autowired
    NetInterRepository netInterRepository;

    //保存网口
    public NetInterfaces save(NetInterfaces netInterfaces){return netInterRepository.save(netInterfaces);}

    //根据id找到网口
    public NetInterfaces getById(Integer id){return netInterRepository.findById(id).get();}

    //根据网卡名称和容器名称找到网卡接口
    public NetInterfaces getByNameAndLxd_name(String name,String lxd_name){
        return netInterRepository.findNetInterfacesByNameAndAndLxd_name(name, lxd_name);
    }

    //根据容器名称找到全部的网卡接口
    public List<NetInterfaces> getListByLxd_name(String lxd_name){
        return netInterRepository.listNetInterByLxd_name(lxd_name);
    }


}
