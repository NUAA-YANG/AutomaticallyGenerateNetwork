package com.nuaa.automaticallygeneratenetwork.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


/**
 * @Author YZX
 * @Create 2023-04-27 10:43
 * @Java-version jdk1.8
 */
//路由器类
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Routers")
public class Routers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//路由器id
    private String name;//路由器名称
    private String interfaces_id;//包含的接口id集合,用分号拆分不同的地址
    private Integer ospf_id;//ospf协议id
    private Integer bgp_id;//bgp协议id
}
