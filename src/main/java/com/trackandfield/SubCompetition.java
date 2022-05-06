package com.trackandfield;

import java.util.List;

import java.time.Instant;
import java.time.Duration;

// class competetion
class SubCompetition {
	int id;
	int durationMinutes;
	boolean isFinal;
	Groups group;
	List<Athletes> athletes; // competitors

	public SubCompetition(int id, int durationMinutes, boolean isFinal, Groups group, List<Athletes> athletes) {
		this.id = id;
		this.durationMinutes = durationMinutes;
		this.isFinal = isFinal;
		this.group = group;
		this.athletes = athletes;
	}

	@Override
	public String toString() {
		return "SubCompetition ["
				+ "id=" + id
				+ ", group.id=" + group.id
				+ ", durationMinutes=" + durationMinutes
				+ ", athletes#=" + athletes.size()
				+ ", isFinal=" + isFinal
				+ ", group.discipline=" + group.discipline
				+ "]";
	}
}
