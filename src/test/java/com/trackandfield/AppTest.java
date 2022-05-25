package com.trackandfield;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.trackandfield.io.CSVHandler;

import org.junit.Test;

public class AppTest {

	String registration_list_fileName = "./resources/registration-list.csv";
	List<String[]> athlete_list;

	List<Athletes> test_athletes_list = new ArrayList<>();

	public AppTest() throws Exception {
		this.athlete_list = CSVHandler.CSVReader(registration_list_fileName);
		System.out.println("athlete_list: loaded.");

	}

	@Test
	public void testLengthOfAthletes() {
		var records = asList("", "", "", "", "", "", "", "", "", "", "");

		records.set(1, "123:345.789");
		records.set(10, "1337.42");

		var atl = new Athletes(0, "club", "name", "surname", 'M', 180, records);
		System.out.println(atl);
		System.out.println(atl.getAgeGroup());
		System.out.println(atl.getSexGroup());

		var dis = atl.getDisciplines();
		var len = dis.size();
		System.out.println(dis);
		assertTrue("length is " + len, len == 2);
	}

	@Test
	public void testGenerateGroup() throws Exception {
		var atls = Athletes.generateAthletes(athlete_list);

		var grps = Groups.generateGroups(atls);

		assertEquals(grps.size(), 132);
	}

	@Test
	public void testGenerateSubCompetition() throws Exception {
		var atls = Athletes.generateAthletes(athlete_list);

		var grps = Groups.generateGroups(atls);

		var subcs = SubCompetition.generateSubCompetition(grps);

		subcs.forEach(s -> {
			System.err.println(s.group.discipline.timeAproxFunction.apply(s));
		});

		assertEquals(subcs.size(), 906);
	}

	@Test
	public void testLengthFromFile() throws Exception {

		var atls = Athletes.generateAthletes(athlete_list);
		atls.removeIf(x -> x.id > 1);
		for (var atl : atls)
			System.out.println(atl);

		System.out.println("-----------");

		var grps = Groups.generateGroups(atls);
		// grps.removeIf(x -> x.id != 15);
		for (var grp : grps)
			System.out.println(grp);

		System.out.println("-----------");

		var subcs = SubCompetition.generateSubCompetition(grps);
		// subcs.removeIf(x -> x.id > 1);
		for (var subc : subcs)
			System.out.println(subc);

	}

	@Test
	public void overlap_test() throws Exception {
		var atls = Athletes.generateAthletes(athlete_list);
		var grps = Groups.generateGroups(atls);
		var subcs = SubCompetition.generateSubCompetition(grps);
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

				assertTrue("e1=!O=e1", event_1.isOverlap(event_1));
				assertTrue("e2=!O=e2", event_2.isOverlap(event_2));
			}

			System.out.println(startTime);
		}
	}

	@Test
	public void timParseTest() {
		Double sum = 0.0;
		var tt = "1:23.444".split(":", -1);
		if (tt.length == 2) {
			sum += Double.parseDouble(tt[0]) * 60.0;
			sum += Double.parseDouble(tt[1]);
		} else {
			sum += Double.parseDouble(tt[0]);
		}
		System.out.println(sum);
	}
}
