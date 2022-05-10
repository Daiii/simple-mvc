package cn.self.zhangbo.kernel.xml;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * XML解析器
 */
public class XMLParser {

    /**
     * 读xml获取base-package
     *
     * @param xml XML
     * @return base-package路径
     */
    public static String getBasePackage(String xml) {
        try {
            SAXReader saxReader = new SAXReader();
            InputStream inputStream = XMLParser.class.getClassLoader().getResourceAsStream(xml);
            Document document = saxReader.read(inputStream);
            Element root = document.getRootElement();
            Element component = root.element("component-scan");
            Attribute attribute = component.attribute("base-package");
            return attribute.getText();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
}
