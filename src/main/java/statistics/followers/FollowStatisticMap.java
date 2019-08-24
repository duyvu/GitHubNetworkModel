package statistics.followers;

import java.util.ArrayList;

import statistics.Statistic;
import statistics.StatisticInteraction;
import statistics.maps.InteractionStatisticMap;
import statistics.maps.StatisticMap;
import statistics.maps.OneModeUserStatisticMap;
import statistics.users.UserStatistic;

public class FollowStatisticMap extends StatisticMap implements
		OneModeUserStatisticMap, InteractionStatisticMap {

	protected ArrayList<UserStatistic> senderStatistics;
	protected ArrayList<UserStatistic> receiverStatistics;
	protected ArrayList<SenderReceiverStatistic> senderReceiverStatistics;
	protected ArrayList<StatisticInteraction> interactionStatistics;

	public FollowStatisticMap() {
		senderStatistics = new ArrayList<UserStatistic>();
		receiverStatistics = new ArrayList<UserStatistic>();
		senderReceiverStatistics = new ArrayList<SenderReceiverStatistic>();
		interactionStatistics = new ArrayList<StatisticInteraction>();
	}

	@Override
	public void addSenderStatistic(UserStatistic senderStatistic) {
		senderStatistics.add(senderStatistic);
		addNamedStatistic(senderStatistic);
	}

	@Override
	public void addReceiverStatistic(UserStatistic receiverStatistic) {
		receiverStatistics.add(receiverStatistic);
		addNamedStatistic(receiverStatistic);
	}

	@Override
	public void addSenderReceiverStatistic(
			SenderReceiverStatistic senderReceiverStatistic) {
		senderReceiverStatistics.add(senderReceiverStatistic);
		addNamedStatistic(senderReceiverStatistic);
	}

	public void addInteractionStatistic(String name, Statistic statistic1,
			Statistic statistic2) {
		StatisticInteraction statisticInteraction = new StatisticInteraction(
				name, statistic1, statistic2);
		interactionStatistics.add(statisticInteraction);
		addNamedStatistic(statisticInteraction);
	}

	@Override
	public ArrayList<UserStatistic> getSenderStatistics() {
		return senderStatistics;
	}

	@Override
	public ArrayList<UserStatistic> getReceiverStatistics() {
		return receiverStatistics;
	}

	@Override
	public ArrayList<SenderReceiverStatistic> getSenderReceiverStatistics() {
		return senderReceiverStatistics;
	}

	@Override
	public ArrayList<StatisticInteraction> getInteractionStatistics() {
		return interactionStatistics;
	}

}
