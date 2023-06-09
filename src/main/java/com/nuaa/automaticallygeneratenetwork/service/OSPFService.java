package com.nuaa.automaticallygeneratenetwork.service;

import com.nuaa.automaticallygeneratenetwork.dao.OSPFRepository;
import com.nuaa.automaticallygeneratenetwork.pojo.BGP;
import com.nuaa.automaticallygeneratenetwork.pojo.OSPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Author YZX
 * @Create 2023-04-28 9:59
 * @Java-version jdk1.8
 */
//实现OSPF的所有服务
@Service
@Component
public class OSPFService {
    @Autowired
    OSPFRepository ospfRepository;

    //保存协议
    @Transactional
    public OSPF save(OSPF ospf){return ospfRepository.save(ospf);}

    //根据本身的id找到OSPF协议
    @Transactional
    public OSPF getById(Integer id){
        return ospfRepository.findById(id).get();
    }

    //根据协议所属路由器名称找到OSPF协议
    @Transactional
    public OSPF getByRouters_Name(String routers_name){return ospfRepository.findOSPFByRoutersName(routers_name);}

    //查询所有的BGP列表
    @Transactional
    public List<OSPF> getList(){
        return ospfRepository.findAll();
    }
}
