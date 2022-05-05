package com.trackandfield;

import java.util.List;

import java.time.Instant;
import java.time.Duration;

// class competetion
class SubCompetition {
	int id;
	int delay;
	String name;
	int durationMinutes;
	boolean isFinal;
	DISCIPLINES discipline;
	List<Athletes> athletes;

	public SubCompetition(int id, int delay, String name, int durationMinutes, boolean isFinal, DISCIPLINES discipline,
			List<Athletes> athletes) {
		this.id = id;
		this.delay = delay;
		this.name = name;
		this.durationMinutes = durationMinutes;
		this.isFinal = isFinal;
		this.discipline = discipline;
		this.athletes = athletes;
	}

	@Override
	public String toString() {
		return "SubCompetition ["
				+ ", id=" + id
				+ ", name=" + name
				+ ", athletes#=" + athletes.size()
				+ ", delay=" + delay
				+ ", discipline=" + discipline
				+ ", durationMinutes=" + durationMinutes
				+ ", isFinal=" + isFinal
				+ "]";
	}

}
