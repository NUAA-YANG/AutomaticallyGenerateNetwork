package com.nuaa.automaticallygeneratenetwork.protocolXml.createXML.ftlReplace;

import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author YZX
 * @Create 2023-05-18 15:14
 * @Java-version jdk1.8
 */
public class test {
    public static void main(String[] args) throws IOException, TemplateException {
        TextMatch textMatch = new TextMatch();
        FtlXml ftlXml = new FtlXml();
        Map<String, Object> map = textMatch.MatchFtl(new File("src/main/java/com/nuaa/automaticallygeneratenetwork/protocolXml/createXML/manufactureText/QR1_Router_HuaWei.txt"));
        ftlXml.ftlToXml(map, (String) map.get("type"),(String) map.get("lxd_name"),"src/main/java/com/nuaa/automaticallygeneratenetwork/protocolXml/createXML/ftlExample");
    }
}
