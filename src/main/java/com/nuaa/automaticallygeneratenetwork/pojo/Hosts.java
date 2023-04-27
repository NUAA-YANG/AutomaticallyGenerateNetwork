package com.nuaa.automaticallygeneratenetwork.pojo;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * @Author YZX
 * @Create 2023-04-27 10:43
 * @Java-version jdk1.8
 */
//主机类
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Hosts")
public class Hosts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主机id
    private String name;//主机名称
    private String interfaces_id;//包含的接口id集合,用分号拆分不同的地址
}
