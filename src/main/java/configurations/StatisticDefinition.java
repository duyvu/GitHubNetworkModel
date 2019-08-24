package configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public class StatisticDefinition {

	private static final Logger LOG = LoggerFactory
			.getLogger(StatisticDefinition.class.getName());

	// Specify node or edge statistics
	protected String type = null;

	// Name of the statistic
	protected String name = null;

	// The definition class
	protected String definition = null;

	protected String relationalEventClassName = null;

	// For data of static covariates
	protected String staticData = null;

	// For data of dynamic covariates
	protected String dynamicData = null;

	// For covariates with time-varying effects
	protected Double startInterval = null;
	protected Double endInterval = null;

	// For interaction terms
	protected String interactionStatistic1 = null;
	protected String interactionStatistic2 = null;

	public StatisticDefinition() {
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim();
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition.trim();
	}

	public String getRelationalEventClassName() {
		return relationalEventClassName;
	}

	public void setRelationalEventClassName(String relationalEventClassName) {
		this.relationalEventClassName = relationalEventClassName.trim();
	}

	public String getStaticData() {
		return staticData;
	}

	public void setStaticData(String staticData) {
		this.staticData = staticData.trim();
	}

	public String getDynamicData() {
		return dynamicData;
	}

	public void setDynamicData(String dynamicData) {
		this.dynamicData = dynamicData.trim();
	}

	public Double getStartInterval() {
		return startInterval;
	}

	public void setStartInterval(Double startInterval) {
		this.startInterval = startInterval;
	}

	public Double getEndInterval() {
		return endInterval;
	}

	public void setEndInterval(Double endInterval) {
		this.endInterval = endInterval;
	}

	public String getInteractionStatistic1() {
		return interactionStatistic1;
	}

	public void setInteractionStatistic1(String interactionStatistic1) {
		this.interactionStatistic1 = interactionStatistic1;
	}

	public String getInteractionStatistic2() {
		return interactionStatistic2;
	}

	public void setInteractionStatistic2(String interactionStatistic2) {
		this.interactionStatistic2 = interactionStatistic2;
	}

	public String toString() {
		StringBuilder results = new StringBuilder();

		if (name != null)
			results.append("Name = " + name + "\n");

		if (type != null)
			results.append("\t Type = " + type + "\n");

		if (definition != null)
			results.append("\t Definition = " + definition + "\n");

		if (relationalEventClassName != null)
			results.append("\t Edge Event Class Name = "
					+ relationalEventClassName + "\n");

		if (staticData != null)
			results.append("\t Static Data = " + staticData + "\n");

		if (dynamicData != null)
			results.append("\t Dynamic Data = " + dynamicData + "\n");

		if (startInterval != null)
			results.append("\t Start Interval = " + startInterval + "\n");

		if (endInterval != null)
			results.append("\t End Interval = " + endInterval + "\n");

		if (interactionStatistic1 != null)
			results.append("\t Statistic 1 = " + interactionStatistic1 + "\n");

		if (interactionStatistic2 != null)
			results.append("\t Statistic 2 = " + interactionStatistic2 + "\n");

		return results.toString();
	}

	public void populateDefinition(Element element) {

		if (element.getElementsByTagName("type").item(0) == null) {
			LOG.info("Empty type for the element {}", element);
			return;
		}

		String type = element.getElementsByTagName("type").item(0)
				.getTextContent();
		setType(type);

		LOG.trace("Statistic - Type : {}", type);

		if (element.getElementsByTagName("name").item(0) == null) {
			LOG.info("Empty name for the element {}", element);
			return;
		}

		String name = element.getElementsByTagName("name").item(0)
				.getTextContent();
		setName(name);

		LOG.trace("Statistic - Name : {}", name);

		if (element.getElementsByTagName("definition").item(0) != null) {
			String definition = element.getElementsByTagName("definition")
					.item(0).getTextContent();
			setDefinition(definition);
			LOG.trace("Statistic - Definition  {}", definition);
		} else
			LOG.trace("Statistic - Definition is EMPTY which is only valid for an INTERACTION term");

		if (element.getElementsByTagName("relationalEventClassName").item(0) != null) {
			String relationalEventClassName = element
					.getElementsByTagName("relationalEventClassName").item(0)
					.getTextContent();
			setRelationalEventClassName(relationalEventClassName);
			LOG.trace("Statistic - Relational Event Class Name: {}",
					relationalEventClassName);
		} else
			LOG.trace("Statistic - Relational Event Class Name is EMPTY which is only valid for an INTERACTION term");

		if (element.getElementsByTagName("staticData").item(0) != null) {
			String staticData = element.getElementsByTagName("staticData")
					.item(0).getTextContent();
			setStaticData(staticData);
			LOG.trace("Statistic - Static Data: {}", staticData);
		} else
			LOG.trace("Statistic - Static Data is EMPTY which is not valid for a STATIC COVARIATE term");

		if (element.getElementsByTagName("dynamicData").item(0) != null) {
			String dynamicData = element.getElementsByTagName("dynamicData")
					.item(0).getTextContent();
			setDynamicData(dynamicData);
			LOG.trace("Statistic - Dynamic Data: {}", dynamicData);
		} else
			LOG.trace("Statistic - Dynamic Data is EMPTY which is not valid for a DYNAMIC COVARIATE term");

		if (element.getElementsByTagName("startInterval").item(0) != null) {
			String startInterval = element
					.getElementsByTagName("startInterval").item(0)
					.getTextContent().trim();
			setStartInterval(Double.parseDouble(startInterval.trim()));
			LOG.trace("Statistic - Start Interval: {}", getStartInterval());
		} else
			LOG.trace("Statistic - Start Interval is EMPTY which is not valid for an INTERVAL TIME-VARYING COVARIATE term");

		if (element.getElementsByTagName("endInterval").item(0) != null) {
			String endInterval = element.getElementsByTagName("endInterval")
					.item(0).getTextContent().trim();
			setEndInterval(Double.parseDouble(endInterval.trim()));
			LOG.trace("Statistic - End Interval: {}", getEndInterval());
		} else
			LOG.trace("Statistic - End Interval is EMPTY which is not valid for an INTERVAL TIME-VARYING COVARIATE term");

		if (element.getElementsByTagName("interactionStatistic1").item(0) != null) {
			String interactionStatistic1 = element
					.getElementsByTagName("interactionStatistic1").item(0)
					.getTextContent();
			setInteractionStatistic1(interactionStatistic1);
			LOG.trace("Interaction Statistic 1: {}", interactionStatistic1);
		} else
			LOG.trace("Interaction Statistic 1 is EMPTY which is valid for a NODE or EDGE term");

		if (element.getElementsByTagName("interactionStatistic2").item(0) != null) {
			String interactionStatistic2 = element
					.getElementsByTagName("interactionStatistic2").item(0)
					.getTextContent();
			setInteractionStatistic2(interactionStatistic2);
			LOG.trace("Interaction Statistic 2: {}", interactionStatistic2);
		} else
			LOG.trace("Interaction Statistic 2 is EMPTY which is valid for a NODE or EDGE term");

	}

}
