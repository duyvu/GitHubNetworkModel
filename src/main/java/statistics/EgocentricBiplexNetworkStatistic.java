/**
 * 
 */
package statistics;

/**
 * @author duyvu
 * 
 */
public class EgocentricBiplexNetworkStatistic extends BiplexNetworkStatistic {

	protected String primaryNodeProperty;

	protected String secondaryNodeProperty;

	/**
	 * 
	 */
	public EgocentricBiplexNetworkStatistic() {
	}

	public String getPrimaryNodeProperty() {
		return primaryNodeProperty;
	}

	public void setPrimaryNodeProperty(String primaryNodeProperty) {
		this.primaryNodeProperty = primaryNodeProperty;
	}

	public String getSecondaryNodeProperty() {
		return secondaryNodeProperty;
	}

	public void setSecondaryNodeProperty(String secondaryNodeProperty) {
		this.secondaryNodeProperty = secondaryNodeProperty;
	}

}
