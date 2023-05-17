<Information>
    <type>host</type><!--描述为路由器router或者主机host-->
    <lxd_name>${lxd_name}</lxd_name>
    <interfaces><!--描述接口信息-->
        <interface><!--描述详细接口信息-->
            <#list interfaceList as interface>
            <name>${interface.name}</name>
            <ipAddress>${interface.ipAddress}</ipAddress>
            <subnetMask>${interface.subnetMask}</subnetMask>
            </#list>
        </interface>
    </interfaces>
</Information>