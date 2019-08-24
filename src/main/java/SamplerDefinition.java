

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


import java.io.File;

public abstract class SamplerDefinition {

	private static final Logger LOG = LoggerFactory
			.getLogger(SamplerDefinition.class.getName());

	public SamplerDefinition() {
	}

	public SamplerDefinition(String samplerDefinitionFile) {
		readModelDescription(samplerDefinitionFile);
	}

	public void readModelDescription(String samplerDefinitionFile) {

		try {

			File fXmlFile = new File(samplerDefinitionFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			LOG.trace("Root element : {}", doc.getDocumentElement()
					.getNodeName());

			NodeList nList = doc.getElementsByTagName("statistic");

			System.out.println("----------------------------");

			for (int statIndex = 0; statIndex < nList.getLength(); statIndex++) {

				Node nNode = nList.item(statIndex);

				LOG.trace("Current Element: {}", nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element element = (Element) nNode;

					StatisticDefinition statisticDefinition = createStatisticDefinition();

					statisticDefinition.populateDefinition(element);

					addStatisticDefinition(statisticDefinition);

				}
			}
		} catch (Exception e) {
			LOG.error("Failed to parse the sampler definition file {}",
					samplerDefinitionFile);
			e.printStackTrace();
			System.exit(-1);
		}

	}

	protected abstract StatisticDefinition createStatisticDefinition();

	protected abstract void addStatisticDefinition(
			StatisticDefinition statisticDefinition);

}
