<Information>
    <type>router</type><!--描述为路由器router或者主机host-->
    <lxd_name>${lxd_name}</lxd_name>
    <interfaces><!--描述接口信息-->
        <interface><!--描述详细接口信息-->
            <#list interfaceFtlList as interfaceFtl>
            <name>${interfaceFtl.name}</name>
            <ipAddress>${interfaceFtl.ipAddress}</ipAddress>
            <subnetMask>${interfaceFtl.subnetMask}</subnetMask>
            </#list>
        </interface>
    </interfaces>
    <protocol><!--描述协议信息-->
        <ospf>
            <ospf_network><!--ospf的邻接点-->
                <#list ospfIp as oIp>
                <ip>${oIp}</ip>
                </#list>
            </ospf_network>
            <ospf_redistribute>${ospfRedis}</ospf_redistribute><!--重转发网络协议-->
        </ospf>
        <bgp>
            <ans>${bgpAns}</ans><!--bgp自治域-->
            <internal><!--bgp的入口流量-->
                <#list bgpInterIp as interIp>
                <ip>${interIp}</ip>
                </#list>
            </internal>
            <external><!--bgp的出口流量-->
                <#list bgpExterIp as exterIp>
                <ip>${exterIp}</ip>
                </#list>
            </external>
            <bgp_network><!--bgp的邻接点-->
                <#list bgpIp as bIp>
                <ip>${bIp}</ip>
                </#list>
            </bgp_network>
            <bgp_redistribute>${bgpRedis}</bgp_redistribute><!--重转发网络协议-->
        </bgp>
    </protocol>
</Information>