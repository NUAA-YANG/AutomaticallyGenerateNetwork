<Information>
    <type>router</type><!--描述为路由器router或者主机host-->
    <lxd_name>${lxd_name}</lxd_name>
    <#if interfaceFtlList??>
    <interfaces><!--描述接口信息-->
        <#list interfaceFtlList as interfaceFtl>
        <interface><!--描述详细接口信息-->
            <name>${interfaceFtl.name}</name>
            <ipAddress>${interfaceFtl.ipAddress}</ipAddress>
            <subnetMask>${interfaceFtl.subnetMask}</subnetMask>
        </interface>
        </#list>
    </interfaces>
    </#if>
    <protocol><!--描述协议信息-->
        <ospf>
            <#--需要判断是否非空-->
            <#if ospfIp??>
            <ospf_network><!--ospf的邻接点-->
                <#list ospfIp as oIp>
                <ip>${oIp}</ip>
                </#list>
            </ospf_network>
            </#if>
            <#if ospfRedis??>
            <ospf_redistribute>${ospfRedis}</ospf_redistribute><!--重转发网络协议-->
            </#if>
        </ospf>
        <bgp>
            <#if bgpAns??>
            <ans>${bgpAns}</ans><!--bgp自治域-->
            </#if>
            <#if bgpInterIp??>
            <internal><!--bgp的入口流量-->
                <#list bgpInterIp as interIp>
                <ip>${interIp}</ip>
                </#list>
            </internal>
            </#if>
            <#if bgpExterIp??>
            <external><!--bgp的出口流量-->
                <#list bgpExterIp as exterIp>
                <ip>${exterIp}</ip>
                </#list>
            </external>
            </#if>
            <#if bgpIp??>
            <bgp_network><!--bgp的邻接点-->
                <#list bgpIp as bIp>
                <ip>${bIp}</ip>
                </#list>
            </bgp_network>
            </#if>
            <#if bgpRedis??>
            <bgp_redistribute>${bgpRedis}</bgp_redistribute><!--重转发网络协议-->
            </#if>
        </bgp>
    </protocol>
</Information>