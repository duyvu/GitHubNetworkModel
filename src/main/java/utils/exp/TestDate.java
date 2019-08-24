package utils.exp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import events.EventStreams;

public class TestDate {

	public static void main(String[] args) {

		testDate2Double();

		testDouble2Date();

	}

	protected static void testDate2Double() {
		try {
			System.out.println(EventStreams.dateFormatter.parse("2008-04-01 00:00:00")
					.getTime() / EventStreams.ONE_DAY_IN_MILLISECONDS);

			System.out.println(EventStreams.dateFormatter.parse("2013-10-07 00:00:00")
					.getTime() / EventStreams.ONE_DAY_IN_MILLISECONDS);

			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected static void testDouble2Date() {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(formatter.format(new Date(
				(long) (11960.583 * EventStreams.ONE_DAY_IN_MILLISECONDS))));

	}
}
