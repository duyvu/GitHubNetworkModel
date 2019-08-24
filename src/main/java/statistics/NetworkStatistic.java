package statistics;

public abstract class NetworkStatistic implements Statistic {

	protected String name = null;

	public NetworkStatistic() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
