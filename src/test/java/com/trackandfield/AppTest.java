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
		var a = new App();
		var test = a.new io();
		var atls = test.generateAthletes();

		var atl = atls.get(7);

		System.out.println(atl);
		System.out.println(atl.getAgeGroup());
		System.out.println(atl.getSexGroup());

		var dis = atl.getDisciplines();
		var len = dis.size();
		System.out.println(dis);
		assertTrue("length is " + len, len == 4);
	}

	@Test
	public void sql_test() {
		SQLiteJDBC.run();
	}
}
