package com.nuaa.automaticallygeneratenetwork.service;

import com.nuaa.automaticallygeneratenetwork.dao.BGPRepository;
import com.nuaa.automaticallygeneratenetwork.pojo.BGP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author YZX
 * @Create 2023-04-27 15:12
 * @Java-version jdk1.8
 */
//实现BGP的所有服务
@Service
public class BGPService {
    @Autowired
    BGPRepository bgpRepository;

    //保存BGP协议
    public BGP save(BGP bgp){
        return bgpRepository.save(bgp);
    }

    //根据本身的id找到BGP协议
    public BGP getById(Integer id){
        return bgpRepository.findById(id).get();
    }

    //根据协议所属路由器名称找到BGP协议
    public BGP getByRouters_Name(String routers_name){return bgpRepository.findBGPByRouters_name(routers_name);}

    //查询所有的BGP列表
    public List<BGP> getList(){
        return bgpRepository.findAll();
    }
}
