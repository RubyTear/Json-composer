package jp.co.xruyee.jsoncomposer.xml.composer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.core.io.Resource;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public class XMLHelper {
	public static final String XML_SCHEMA_KEY = "xml-schema";
	private Map<String, Source> properties;
	private Jaxb2Marshaller marshaller;

	/**
	 * Unmarshal a given source
	 */
	public Object load(Source source) throws XmlMappingException, IOException {
		return marshaller.unmarshal(source);
	}

	/**
	 * Marshal a given Object to a Result
	 */
	public void save(Object obj, Result result) throws XmlMappingException, IOException {
		marshaller.marshal(obj, result);
	}

	/**
	 * This method is used by Spring to inject the XSD file that is to be used to
	 * generate the domain classes dynamically.
	 */
	public void setSchemaFile(Resource schemaFile) throws IOException, JAXBException {
		properties = new HashMap<String, Source>();
		properties.put(XML_SCHEMA_KEY, new StreamSource(schemaFile.getInputStream()));
	}

	/**
	 * This method is used by Spring to inject an instance of Jaxb2Marshaller
	 */
	public void setMarshaller(Jaxb2Marshaller marshaller) {
		this.marshaller = marshaller;
		this.marshaller.setJaxbContextProperties(properties);
		this.setMarshalFeature();

	}

	private void setMarshalFeature() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		this.marshaller.setMarshallerProperties(properties);
	}
}