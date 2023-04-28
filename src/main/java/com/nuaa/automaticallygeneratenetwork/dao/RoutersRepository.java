package com.nuaa.automaticallygeneratenetwork.dao;

import com.nuaa.automaticallygeneratenetwork.pojo.Routers;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author YZX
 * @Create 2023-04-27 20:59
 * @Java-version jdk1.8
 */
//接口继承实现Routers的增删改查
public interface RoutersRepository extends JpaRepository<Routers,Integer> {
}
