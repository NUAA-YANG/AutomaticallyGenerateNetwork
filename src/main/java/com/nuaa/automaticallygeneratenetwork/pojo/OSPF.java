package com.nuaa.automaticallygeneratenetwork.pojo;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * @Author YZX
 * @Create 2023-04-27 10:55
 * @Java-version jdk1.8
 */
//ospf路由协议
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "OSPF")
public class OSPF {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//协议id
    private String network;//邻接点的ip地址集合,用分号拆分不同的地址
    private String routersName;//协议所属路由器名称
}
