# 自动生成网络

---

## 一. 概述

主要目标是：

1.  理解虚拟网络平台的基于XML描述网络方法，搞清基于XML/FRR生成NFV网络的技术原理；

2. 理解不同厂商路由器的路由协议配置指令，搞清将其转换为自动生成NFV网络的方法；

3. 理解不同厂商路由器ACL指令，研究将其转换为自动配置NFV网络安全策略的方法；

4. 设计实现能够与虚拟网络平台整合的基于网管信息的NFV网络生成VNF，完成系统测试和设计、操作文档；

5. 研究不同厂商路由器其他主要配置指令及其转化方法，实现相关程序。

测试实验拓扑图如下，拓扑图链接：[image.png](https://postimg.cc/KRkySLPL)

![image-20230530155930615](ReadMeImage/img.png)





## 二. 自动生成网络

### 2.1 思路

---

1. 根据网络拓扑图，写出对应的`XML`网络描述文件
2. 根据`XML`网络描述文件以及其数量，转化为容器类、接口类、协议类对象存入数据库中
3. 从数据库中取出容器类对象，生成对应的`LXD`容器
4. 从数据库中取出容器类对象，创建并且连接网桥
5. 从数据库中取出容器类对象，为每个容器创建接口配置文件
6. 从数据库中取出协议类对象，为每个容器创建网络配置文件
7. 启动所有容器，替换两种配置文件
8. 重启所有的接口以及网络使之生效

![img](ReadMeImage/img_1.png)



### 2.2 生成网络容器展示

---

生成的所有主机容器

![img](ReadMeImage/img_2.png)

生成的所有路由器容器

![img](ReadMeImage/img_3.png)



### 2.3 网络连通性展示

---

利用`QH11`访问任意容器

![image-20230530160306985](ReadMeImage/img_4.png)



### 2.4 AS域之间网络连通性展示

---

访问`AS300`，`QH11`访问`QH3`

![image-20230530163925458](ReadMeImage/img_5.png)

访问`AS200`，`QH11`访问`QH2`

![image-20230530163911641](ReadMeImage/img_6.png)

内部自治域内访问

![image-20230530163821170](ReadMeImage/img_7.png)





## 三. 自动部署ACL策略

### 3.1 思路

---

1. 抽象方法，能将中兴或华为的单条`ACL`命令转化为`Iptables`对象
2. 批量处理`ACL`文件，将转化完的`Iptables`对象存入到数据库中
3. 从数据库中取出`Iptables`对象，将其构建为`Iptables`命令配置文件
4. 替换容器中的防火墙配置文件，使之生效

![img](ReadMeImage/img_23.png)

![img](ReadMeImage/img_8.png)



### 3.2 防火墙整体展示

----

生成的`QR1`防火墙

![image-20230530171239838](ReadMeImage/img_9.png)

生成的`QR2`防火墙

![image-20230530171306342](ReadMeImage/img_10.png)

生成的`QR3`防火墙

![image-20230530171327998](ReadMeImage/img_11.png)



### 3.3 测试防火墙性能

---

为了验证单个路由器上防火墙性能，排除其余容器防火墙规则重叠的影响，清空其余容器防火墙

#### 3.3.1 测试QR1

---

验证`QH3`可以连通`QH1`【lxc exec QH3 ping 192.168.80.1】，即验证【permit ip 192.168.86.2 0.0.0.255 any】

![image-20230531105611285](ReadMeImage/img_11.png)

验证`QH2`无法连通`QH11`【lxc exec QH2 ping 192.168.89.2】，即验证【deny ip 192.168.88.2 0.0.0.255 any】

![image-20230531105545567](ReadMeImage/img_12.png)

查看`QR1`拦截流量情况【lxc exec QR1 -- iptables -t filter -nvL】

![image-20230531105638632](ReadMeImage/img_13.png)



#### 3.3.2 测试QR2

---

验证`QH33`可以连通`QH22`【lxc exec QH33 ping 192.168.90.2】，即验证【permit ip 192.168.91.2 0.0.0.255 any】

![image-20230531112133703](ReadMeImage/img_14.png)

验证`QH3`可以连通`QH2`【lxc exec QH3 ping 192.168.88.2】，但`QH1`无法连通`QH2`【lxc exec QH1 ping 192.168.88.2】，即验证【permit ip 192.168.86.2 0.0.0.255 any 和deny ip any 255.255.255.255 192.168.88.2 0.0.0.0】，同时还验证了`iptables`的命令具有优先级

![image-20230531112156175](ReadMeImage/img_15.png)

![image-20230531112207678](ReadMeImage/img_16.png)

查看`QR2`拦截流量情况，【lxc exec QR2 -- iptables -t filter -nvL】

![image-20230531112222605](ReadMeImage/img_17.png)

#### 3.3.3 测试QR3

---

验证`QH2`可以连通`QH3`【lxc exec QH2 ping 192.168.86.2】，但`QH22`无法连通`QH3`【lxc exec QH22 ping 192.168.86.2】，即验证【permit ip source 192.168.88.2 0.0.0.255和deny ip source 192.168.90.2 0.0.0.255】，同时验证`iptables`只和规则有关，和`AS`域无关

![image-20230531112409296](ReadMeImage/img_18.png)

![image-20230531112424206](ReadMeImage/img_19.png)

验证`QH11`无法连通`QH33`【lxc exec QH11 ping 192.168.91.2】，即验证【deny ip source 192.168.89.2 0.0.0.255】

![image-20230531112438142](ReadMeImage/img_20.png)

查看`QR3`拦截流量情况

![image-20230531112507998](ReadMeImage/img_21.png)









