/**
 * 
 */
package statistics;

/**
 * @author duyvu
 * 
 */
public abstract class RelationalBiplexNetworkStatistic extends
		BiplexNetworkStatistic {

	protected String primarySenderNodeProperty;
	protected String primaryReceiverNodeProperty;

	protected String secondarySenderNodeProperty;
	protected String secondaryReceiverNodeProperty;

	/**
	 * 
	 */
	public RelationalBiplexNetworkStatistic() {
	}

	public String getPrimarySenderNodeProperty() {
		return primarySenderNodeProperty;
	}

	public void setPrimarySenderNodeProperty(String primarySenderNodeProperty) {
		this.primarySenderNodeProperty = primarySenderNodeProperty;
	}

	public String getPrimaryReceiverNodeProperty() {
		return primaryReceiverNodeProperty;
	}

	public void setPrimaryReceiverNodeProperty(String primaryReceiverNodeProperty) {
		this.primaryReceiverNodeProperty = primaryReceiverNodeProperty;
	}

	public String getSecondarySenderNodeProperty() {
		return secondarySenderNodeProperty;
	}

	public void setSecondarySenderNodeProperty(
			String secondarySenderNodeProperty) {
		this.secondarySenderNodeProperty = secondarySenderNodeProperty;
	}

	public String getSecondaryReceiverNodeProperty() {
		return secondaryReceiverNodeProperty;
	}

	public void setSecondaryReceiverNodeProperty(
			String secondaryReceiverNodeProperty) {
		this.secondaryReceiverNodeProperty = secondaryReceiverNodeProperty;
	}

}
