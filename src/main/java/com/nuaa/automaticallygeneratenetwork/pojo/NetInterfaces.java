package com.nuaa.automaticallygeneratenetwork.pojo;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author YZX
 * @Create 2023-04-27 10:47
 * @Java-version jdk1.8
 */
//记录网端接口
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "NetInterfaces")
public class NetInterfaces {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//接口id
    private String name;//接口名称
    private String ipAddress;//接口ip地址
    private Integer subnetMask;//接口子网掩码
    private String lxdName;//接口所属路由器或者主机名称
}
