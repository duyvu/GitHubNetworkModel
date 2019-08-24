package statistics.maps;

import java.util.ArrayList;

import statistics.followers.SenderReceiverStatistic;
import statistics.users.UserStatistic;

public interface OneModeUserStatisticMap {

	public void addSenderStatistic(UserStatistic senderStatistic);

	public void addReceiverStatistic(UserStatistic receiverStatistic);

	public void addSenderReceiverStatistic(
			SenderReceiverStatistic senderReceiverStatistic);

	public ArrayList<UserStatistic> getSenderStatistics();

	public ArrayList<UserStatistic> getReceiverStatistics();

	public ArrayList<SenderReceiverStatistic> getSenderReceiverStatistics();

}
