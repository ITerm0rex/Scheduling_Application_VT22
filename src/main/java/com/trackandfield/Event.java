package com.trackandfield;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Event {
	int id;
	Instant startTime;
	Instant endTime;
	SubCompetition subCompetition;
	STATIONS station;
	final static private DateTimeFormatter hms = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss")
			.withZone(ZoneId.from(ZoneOffset.UTC));

	public Event(int id, Instant startTime, Instant endTime, SubCompetition subCompetition,
			STATIONS station) {
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
		this.subCompetition = subCompetition;
		this.station = station;
	}

	public String getStartTime() {
		return hms.format(startTime);
	}

	public String getEndTime() {
		return hms.format(endTime);
	}

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

	public Boolean isTimeOverlap(Event other) {
		return this.startTime.compareTo(other.endTime) < 0 && this.endTime.compareTo(other.startTime) > 0;
	}

	/**
	 * Athletes in Event overlap
	 * 
	 * @param other
	 * @return True if (time & station) or (time & athlete) does overlap. False
	 *         otherwise
	 */
	public Boolean isOverlap(Event other) {
		if (this.isTimeOverlap(other)) {
			if (this.subCompetition.containsSameAthletes(other.subCompetition))
				return true;
			if (this.station.equals(other.station))
				return true;
		}
		return false;
	}

	/**
	 * checking overlap with a every event in the list
	 * 
	 * @param events
	 * @return
	 */
	public Boolean isOverlapAll(List<Event> events) {
		for (var event : events) {
			if (this.isOverlap(event))
				return true;
		}
		return false;
	}

	/**
	 * postpone an event with a given duration
	 * 
	 * @param time
	 */
	public void moveEvent(Duration time) {
		this.startTime = this.startTime.plus(time);
		this.endTime = this.endTime.plus(time);
	}

	/**
	 * Used for handeling keaping track, add break after each event
	 */
	static class EventState {
		int id = 0;
		int breakAfterEvent;
		Instant eventStart;
		HashMap<Integer, Duration> delaysMap = new HashMap<>();

		public EventState(int breakAfterEvent, Instant eventStart) {
			this.breakAfterEvent = breakAfterEvent;
			this.eventStart = eventStart;
		}

		/**
		 * set delay from deley-list
		 * 
		 * @param delayList
		 * @throws Exception
		 */
		void setDelaysFromFile(List<String[]> delayList) throws Exception {
			int line = 0;
			for (var delayEntrie : delayList) {
				line++;
				try {
					var id = Integer.parseInt(delayEntrie[0]); // id
					var delay = Duration.ofMinutes(Integer.parseInt(delayEntrie[1])); // Minutes
					if (this.delaysMap.containsKey(id))
						delay = delay.plus(this.delaysMap.get(id));
					this.delaysMap.put(id, delay);
				} catch (Exception e) {
					throw new Exception("Invalid format of delay-list: entry: " + line + "\n" + e, e);
				}
			}
		}
	}

	/**
	 * {@summary Generate events for schedule}
	 * 
	 * @return List<Event>
	 */
	public static List<Event> generateEvents(final List<SubCompetition> subComps, EventState state) {
		var all_events = new LinkedList<Event>();

		for (final var subComp : subComps) {

			// Loop through stations
			for (var station : STATIONS.values()) {

				if (station.disciplines.contains(subComp.group.discipline)) {
					if ((state.id % 2 == 0) && Arrays.asList(
							STATIONS.High_Jump_l,
							STATIONS.Long_Triple_Jump_1,
							STATIONS.Shot_Throwing_l).contains(station))
						continue;

					state.id++;

					var eventTimeStart = state.eventStart;

					var eventTimeEnd = eventTimeStart.plus(state.breakAfterEvent, ChronoUnit.MINUTES);

					eventTimeEnd = eventTimeEnd.plus(subComp.group.discipline.timeAproxFunction.apply(subComp));

					var delay = state.delaysMap.get(state.id);
					if (delay != null)
						eventTimeEnd = eventTimeEnd.plus(delay);

					var new_event = new Event(state.id, eventTimeStart, eventTimeEnd, subComp, station);

					all_events.add(new_event);

					break;
				}
			}
		}

		return improveEvents(all_events);
	}

	/**
	 * {@summary Iterative Repair of list of Events}
	 */
	public static List<Event> improveEvents(List<Event> events) {
		var temp_events = new ArrayList<Event>(events);
		var result_events = new LinkedList<Event>();

		while (!temp_events.isEmpty()) {
			for (int i = 0; i < temp_events.size(); i++) {
				var event = temp_events.get(i);

				var start_break = event.startTime.truncatedTo(ChronoUnit.DAYS).plus(12, ChronoUnit.HOURS);
				var end_break = start_break.plus(1, ChronoUnit.HOURS);

				if ((event.endTime.isAfter(start_break) && event.endTime.isBefore(end_break))
						|| (event.startTime.isAfter(start_break) && event.startTime.isBefore(end_break))) {
					event.endTime = end_break.plus(
							event.startTime.until(event.endTime, ChronoUnit.MINUTES),
							ChronoUnit.MINUTES);
					event.startTime = end_break;
				}

				var end_day = event.startTime.truncatedTo(ChronoUnit.DAYS).plus(16, ChronoUnit.HOURS);
				var start_day = end_day.plus(16, ChronoUnit.HOURS);

				if ((event.endTime.isAfter(end_day) && event.endTime.isBefore(start_day))
						|| (event.startTime.isAfter(end_day) && event.startTime.isBefore(start_day))) {
					event.endTime = start_day.plus(
							event.startTime.until(event.endTime, ChronoUnit.MINUTES),
							ChronoUnit.MINUTES);
					event.startTime = start_day;
				}

				if (event.isOverlapAll(result_events)) {
					// event.addDelay(5);
					event.moveEvent(Duration.of(1, ChronoUnit.MINUTES));
				} else {
					result_events.add(event);
					temp_events.remove(event);
				}
			}
		}

		return result_events;
	}
}
