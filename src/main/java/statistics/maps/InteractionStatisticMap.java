package statistics.maps;

import java.util.ArrayList;

import statistics.Statistic;
import statistics.StatisticInteraction;

public interface InteractionStatisticMap {

	public void addInteractionStatistic(String name, Statistic statistic1,
			Statistic statistic2);

	public ArrayList<StatisticInteraction> getInteractionStatistics();

}
