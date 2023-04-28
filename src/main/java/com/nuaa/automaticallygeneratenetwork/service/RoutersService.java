package com.nuaa.automaticallygeneratenetwork.service;

import com.nuaa.automaticallygeneratenetwork.dao.RoutersRepository;
import com.nuaa.automaticallygeneratenetwork.pojo.Routers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author YZX
 * @Create 2023-04-28 10:24
 * @Java-version jdk1.8
 */
//实现路由器的全部服务
public class RoutersService {
    @Autowired
    RoutersRepository routersRepository;

    //保存路由器
    public Routers save(Routers routers){return routersRepository.save(routers);}

    //根据id获得路由器
    public Routers getById(Integer id){return routersRepository.findById(id).get();}

    //查询所有
    public List<Routers> getList(){return routersRepository.findAll();}

}
