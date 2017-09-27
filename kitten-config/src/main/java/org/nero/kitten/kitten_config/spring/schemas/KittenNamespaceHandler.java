package main.java.core.schemas;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/27
 * Time   上午11:33
 */
public class KittenNamespaceHandler implements NamespaceHandler {
    public void init() {

    }

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        return null;
    }

    public BeanDefinitionHolder decorate(Node source, BeanDefinitionHolder definition, ParserContext parserContext) {
        return null;
    }
}
