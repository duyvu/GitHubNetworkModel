package statistics;

public class StatisticInteraction implements Statistic {

	public String name;
	public Statistic statistic1;
	public Statistic statistic2;

	public StatisticInteraction(String name, Statistic statistic1,
			Statistic statistic2) {
		this.name = name;
		this.statistic1 = statistic1;
		this.statistic2 = statistic2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
