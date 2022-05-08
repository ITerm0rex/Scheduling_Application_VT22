package com.trackandfield;

// import java.time.Instant;
// import java.time.temporal.ChronoUnit;

public class Event {
	int id;
	int startTime;
	int endTime;
	SubCompetition subCompetition;

	public Event(int id, int startTime, SubCompetition subCompetition) {
		this.id = id;
		this.startTime = startTime;
		this.subCompetition = subCompetition;
		this.endTime = startTime + subCompetition.durationMinutes;
		// this.endTime = startTime.plus(subCompetition.durationMinutes,
		// ChronoUnit.MINUTES);
	}

	public Boolean isOverlap(Event evt) {
		if ((this.startTime < evt.endTime) && (this.endTime > evt.startTime))
			return this.subCompetition.isOverlap(evt.subCompetition);
		return false;

	}

	@Override
	public String toString() {
		return "Event ["
				+ "id=" + id
				+ ", startTime=" + startTime
				+ ", endTime=" + endTime
				+ ", subCompetition=" + subCompetition
				+ "]";
	}
}
