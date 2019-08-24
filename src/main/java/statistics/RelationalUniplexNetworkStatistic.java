/**
 * 
 */
package statistics;

/**
 * @author duyvu
 * 
 */
public abstract class RelationalUniplexNetworkStatistic extends
		UniplexNetworkStatistic {

	protected String senderNodeProperty;
	protected String receiverNodeProperty;

	/**
	 * 
	 */
	public RelationalUniplexNetworkStatistic() {
	}

	public String getSenderNodeProperty() {
		return senderNodeProperty;
	}

	public void setSenderNodeProperty(String senderNodeProperty) {
		this.senderNodeProperty = senderNodeProperty;
	}

	public String getReceiverNodeProperty() {
		return receiverNodeProperty;
	}

	public void setReceiverNodeProperty(String receiverNodeProperty) {
		this.receiverNodeProperty = receiverNodeProperty;
	}

}
