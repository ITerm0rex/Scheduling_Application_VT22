package com.trackandfield;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import com.trackandfield.App.io;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
	/**
	 * Rigorous Test :-)
	 */
	@Test
	public void shouldAnswerWithTrue() {
		assertTrue(true);
	}
	
	@Test
	public void testLength() {
		// var a = new App();
		// io test = a.new io();
		// var atl = test.generateAthletes();
		var rec = new String[]{"","","","","","","","","","",""};
		
		rec[1] = "0:0";
		
		var atl = new App().new Athletes(0, "club", "name", "surname", 'M', 18, Arrays.asList(rec));

		
		var len = App.Groups.getDisciplines(atl).size();
		
		assertTrue("length is " + len, len == 1);
	}
	
	@Test
	public void testFromFile() {
		var a = new App();
		io test = a.new io();
		var atl = test.generateAthletes();
		
		var len = App.Groups.getDisciplines(atl.get(1)).size();
		assertTrue("length is " + len, len == 4);
	}
}
