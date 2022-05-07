package com.trackandfield;

// import java.time.Instant;
// import java.time.temporal.ChronoUnit;

public class Event {
	int startTime;
	int endTime;
	SubCompetition subCompetition;

	public Event(int startTime, SubCompetition subCompetition) {
		this.startTime = startTime;
		this.subCompetition = subCompetition;
		this.endTime = startTime + subCompetition.durationMinutes;
		// this.endTime = startTime.plus(subCompetition.durationMinutes,
		// ChronoUnit.MINUTES);
	}

	@Override
	public String toString() {
		return "Event [endTime=" + endTime + ", startTime=" + startTime + ", subCompetition=" + subCompetition + "]";
	}

}
