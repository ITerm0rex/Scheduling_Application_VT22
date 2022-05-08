package com.trackandfield;

import java.util.LinkedList;
import java.util.Arrays;
import java.util.List;

class Stations {
	String name;
	List<Event> events;
	List<DISCIPLINES> disciplines;

	private Stations(String name, List<DISCIPLINES> disciplines) {
		this.name = name;
		this.disciplines = disciplines;
		this.events = new LinkedList<Event>();
	}

	public static List<Stations> getStations() {
		return stations;
	}

	private final static List<Stations> stations = Arrays.asList(
			new Stations("Running circle 400m", Arrays.asList(
					DISCIPLINES.Running_Sprint_200,
					DISCIPLINES.Running_Middle_800,
					DISCIPLINES.Running_Middle_1500,
					DISCIPLINES.Running_Long_3000)),
			new Stations("Sprint Line 60m", Arrays.asList(
					DISCIPLINES.Running_Sprint_60,
					DISCIPLINES.Running_Hurdles_60)),
			new Stations("Long/Triple Jump 1", Arrays.asList(
					DISCIPLINES.Jumping_Long,
					DISCIPLINES.Jumping_Triple)),
			new Stations("Long/Triple Jump 2", Arrays.asList(
					DISCIPLINES.Jumping_Long,
					DISCIPLINES.Jumping_Triple)),
			new Stations("High_Jump-l", Arrays.asList(
					DISCIPLINES.Jumping_High)),
			new Stations("High_Jump_ll", Arrays.asList(
					DISCIPLINES.Jumping_High)),
			new Stations("Pole Vault", Arrays.asList(
					DISCIPLINES.Jumping_Pole)),
			new Stations("Shot Throwing l", Arrays.asList(
					DISCIPLINES.Throwing_Shot)),
			new Stations("Shot Throwing ll", Arrays.asList(
					DISCIPLINES.Throwing_Shot)),
			new Stations("Award Ceremony Area",
					Arrays.asList(DISCIPLINES.values())));

	@Override
	public String toString() {
		return "Stations ["
				+ "events#=" + events.size()
				+ ", name=" + name
				+ ", disciplines=" + disciplines
				+ "]";
	}
}
