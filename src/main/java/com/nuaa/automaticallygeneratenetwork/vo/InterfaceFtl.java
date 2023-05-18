package com.nuaa.automaticallygeneratenetwork.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author YZX
 * @Create 2023-05-17 17:12
 * @Java-version jdk1.8
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InterfaceFtl {
    String name;
    String ipAddress;
    Integer subnetMask;
}
