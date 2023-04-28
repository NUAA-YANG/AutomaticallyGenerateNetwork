package com.nuaa.automaticallygeneratenetwork.service;

import com.nuaa.automaticallygeneratenetwork.dao.NetInterfacesRepository;
import com.nuaa.automaticallygeneratenetwork.pojo.NetInterfaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Author YZX
 * @Create 2023-04-28 10:04
 * @Java-version jdk1.8
 */
//实现网口的所有服务
@Service
@Component
public class NetInterfacesService {

    @Autowired
    NetInterfacesRepository netInterfacesRepository;

    //保存网口
    @Transactional
    public NetInterfaces save(NetInterfaces netInterfaces){
        return netInterfacesRepository.save(netInterfaces);}

    //批量保存网口
    @Transactional
    public List<NetInterfaces> saveList(List<NetInterfaces> netInterList){
        return netInterfacesRepository.saveAll(netInterList);}

    //根据id找到网口
    @Transactional
    public NetInterfaces getById(Integer id){return netInterfacesRepository.findById(id).get();}

    //根据网卡名称和容器名称找到网卡接口
    @Transactional
    public NetInterfaces getByNameAndLxd_name(String name,String lxd_name){
        return netInterfacesRepository.findNetInterfacesByNameAndAndLxdName(name, lxd_name);
    }

    //根据容器名称找到全部的网卡接口
    @Transactional
    public List<NetInterfaces> getListByLxd_name(String lxd_name){
        return netInterfacesRepository.listNetInterByLxdName(lxd_name);
    }


}
