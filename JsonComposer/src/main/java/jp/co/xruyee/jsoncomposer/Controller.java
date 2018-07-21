package jp.co.xruyee.jsoncomposer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.dynamic.DynamicEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.oxm.XmlMappingException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.co.xruyee.jsoncomposer.context.AppContextUtils;
import jp.co.xruyee.jsoncomposer.xml.composer.XMLHelper;

@RestController
public class Controller {

	@Autowired
	AppContextUtils _appContextUtils;

	@RequestMapping(value = "/test")
	public String name() {
		return "Welcome to JSON Composer";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/json/composer", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public JAXBElement<DynamicEntity> uploadComposer(Object composer) {

		ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) _appContextUtils
				.getApplicationContext();

		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext
				.getBeanFactory();

		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(XMLHelper.class);
		beanDefinitionBuilder.addPropertyValue("schemaFile", "classpath:customer.xsd");
		beanDefinitionBuilder.addPropertyReference("marshaller", "jaxbMarshaller");

		// 注册bean
		defaultListableBeanFactory.registerBeanDefinition("xmlHelper", beanDefinitionBuilder.getRawBeanDefinition());

		XMLHelper _xmlHelper = (XMLHelper) _appContextUtils.getBean("xmlHelper");

		// load Customer
		JAXBElement<DynamicEntity> jaxbElement = null;
		try (FileInputStream fis = new FileInputStream("./target/classes/customer.xml");) {
			StreamSource ss = new StreamSource(fis);
			jaxbElement = (JAXBElement<DynamicEntity>) _xmlHelper.load(ss);
			DynamicEntity customer = jaxbElement.getValue();
			// update customer
			DynamicEntity address = customer.get("address");
			address.set("street", "234 Some Other Rd.");
			customer.set("address", address);
			// save customer
			_xmlHelper.save(jaxbElement, new StreamResult(new FileOutputStream("new-customer.xml")));
		} catch (XmlMappingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jaxbElement;
	}

}
