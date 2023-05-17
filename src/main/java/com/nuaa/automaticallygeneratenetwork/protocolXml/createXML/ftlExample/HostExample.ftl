<Information>
    <type>host</type><!--描述为路由器router或者主机host-->
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
</Information>