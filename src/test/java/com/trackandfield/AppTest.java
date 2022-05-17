package com.trackandfield;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.trackandfield.io.CSVHandler;
import com.trackandfield.io.JSONHandler;
import com.trackandfield.io.SQLiteJDBC;

import org.junit.Test;

public class AppTest {

	@Test
	public void testLength() {
		var records = asList("", "", "", "", "", "", "", "", "", "", "");

		records.set(1, "123:345.789");
		records.set(10, "1337.42");

		var atl = new Athletes(0, "club", "name", "surname", 'å', 180, records);
		System.out.println(atl);
		System.out.println(atl.getAgeGroup());
		System.out.println(atl.getSexGroup());

		var dis = atl.getDisciplines();
		var len = dis.size();
		System.out.println(dis);
		assertTrue("length is " + len, len == 2);
	}

	@Test
	public void testLengthFromFile() {

		var atls = App.util.generateAthletes();
		atls.removeIf(x -> x.id != 2);
		for (var atl : atls)
			System.out.println(atl);

		System.out.println("-----------");

		var grps = App.util.generateGroups(atls);
		// grps.removeIf(x -> x.id != 15);
		for (var grp : grps)
			System.out.println(grp);

		System.out.println("-----------");

		var subcs = App.util.generateSubCompetition(grps);
		// subcs.removeIf(x -> x.id > 1);
		for (var subc : subcs)
			System.out.println(subc);

		var len = atls.get(0).getDisciplines().size();

		assertTrue("length is " + len, len == 4);
	}

	@Test
	public void sql_test() {
		SQLiteJDBC.test();
	}

	@Test
	public void json_test() {
		JSONHandler.test();
	}

	@Test
	public void csv_test() {
		CSVHandler.test();
	}

	@Test
	public void overlap_test() {
		var atls = App.util.generateAthletes();
		var grps = App.util.generateGroups(atls);
		var subcs = App.util.generateSubCompetition(grps);
		// subcs.removeIf(x -> x.id > 10);

		var startTime = Instant.now();

		for (var stat : STATIONS.values()) {
			for (var iter = subcs.iterator(); iter.hasNext();) {
				var subc = iter.next();

				if (!iter.hasNext()) {
					break;
				}

				var end_time = startTime.plus(5, ChronoUnit.MINUTES);
				var event_1 = new Event(0, startTime, end_time, subc, stat);
				startTime = event_1.endTime;

				subc = iter.next();

				end_time = startTime.plus(5, ChronoUnit.MINUTES);
				var event_2 = new Event(1, startTime, end_time, subc, stat);
				startTime = event_2.endTime;

				System.out.println(event_1.isOverlap(event_2));
				System.out.println(event_2.isOverlap(event_1));

				assertTrue("e1=!O=e1", event_1.isOverlap(event_1));
				assertTrue("e2=!O=e2", event_2.isOverlap(event_2));
			}

			System.out.println(startTime);
		}
	}

}
