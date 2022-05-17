package com.trackandfield;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

class Event {
	int id;
	Instant startTime;
	Instant endTime;
	SubCompetition subCompetition;
	STATIONS station;

	public Event(int id, Instant startTime, Instant endTime, SubCompetition subCompetition,
			STATIONS station) {
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
		this.subCompetition = subCompetition;
		this.station = station;
	}

	// Athletes in Event overlap
	public Boolean isOverlap(Event evt) {
		if (this.startTime.compareTo(evt.endTime) < 0 && this.endTime.compareTo(evt.startTime) > 0) {
			if (this.subCompetition.containsSameAthletes(evt.subCompetition))
				return true;
			else if (this.station.equals(evt.station))
				return true;
		}
		return false;
	}

	public Boolean isOverlapAll(List<Event> events) {
		for (var event : events) {
			if (this.isOverlap(event))
				return true;
		}
		return false;
	}

	public void addDelay(int delayInMinutes) {
		this.startTime = this.startTime.plus(delayInMinutes, ChronoUnit.MINUTES);
		this.endTime = this.endTime.plus(delayInMinutes, ChronoUnit.MINUTES);
	}

	final static private DateTimeFormatter hms = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss")
			.withZone(ZoneId.from(ZoneOffset.UTC));

	@Override
	public String toString() {
		return "Event ["
				+ "id=" + id
				+ ", startTime=" + hms.format(startTime)
				+ ", endTime=" + hms.format(endTime)
				+ ", station=" + station
				+ ", subCompetition=" + subCompetition
				+ "]";
	}

	public String getStartTime() {
		return hms.format(startTime);
	}

	public String getEndTime() {
		return hms.format(endTime);
	}
}
