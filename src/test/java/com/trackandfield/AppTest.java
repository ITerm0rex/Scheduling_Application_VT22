package com.trackandfield;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import static java.util.Arrays.asList;

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
		SQLiteJDBC.run();
	}
}
