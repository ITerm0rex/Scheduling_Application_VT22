package com.trackandfield;

import java.util.List;

import java.time.Instant;
import java.time.Duration;

// class competetion
class Sub_competition {
	int id;
	String name;
	Stations station;
	List<Athletes> athletes;

	Instant startDate;
	Duration duration;
	Duration delay;

	public Sub_competition(int id, Groups group, Stations station) {
		this.id = id;
		this.athletes = group.athletes; // ?
		this.station = station;
		this.name = "id: " + id + ", age:" + group.age_groups + ", sex:" + group.sex_groups
				+ ", Discipline:" + group.discipline + ", station.id:" + station.id;

		this.startDate = null;
		this.duration = null;
		this.delay = null;
	}
}
