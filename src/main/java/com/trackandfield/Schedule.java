package com.trackandfield;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import com.trackandfield.io.CSVHandler;
import com.trackandfield.io.JSONHandler;

public class Schedule {
	static enum SortFunctions {
		SortByTime((a, b) -> a.startTime.compareTo(b.startTime)),
		SortByStation((a, b) -> a.station.ordinal() - b.station.ordinal()),
		SortByDiscipline((a, b) -> a.subCompetition.group.discipline.ordinal()
				- b.subCompetition.group.discipline.ordinal()),
		SortByAgeGroup((a, b) -> a.subCompetition.group.age_groups.ordinal()
				- b.subCompetition.group.age_groups.ordinal()),
		SortBySex((a, b) -> a.subCompetition.group.sex_groups.ordinal()
				- b.subCompetition.group.sex_groups.ordinal()),
		SortByGroupId((a, b) -> a.subCompetition.group.id - b.subCompetition.group.id),
		SortBySubCompetitionId((a, b) -> a.subCompetition.id - b.subCompetition.id),
		SortByEventId((a, b) -> a.id - b.id);

		public final Comparator<Event> sf;

		SortFunctions(final Comparator<Event> sf) {
			this.sf = sf;
		}
	}

	static class ConfigFormat {
		String registration_list_input_file;
		String event_delay_input_file;
		String schedule_output_file;
		String schedule_start;
		Integer break_after_each_event;
		List<String> sorting_procedure;

		@Override
		public String toString() {
			return "configFormat ["
					+ " \n\tregistration_list_input_file=" + registration_list_input_file
					+ ",\n\tevent_delay_input_file=" + event_delay_input_file
					+ ",\n\tschedule_output_file=" + schedule_output_file
					+ ",\n\tschedule_start=" + schedule_start
					+ ",\n\tbreak_after_each_event=" + break_after_each_event
					+ ",\n\tsorting_procedure=" + sorting_procedure
					+ "\n]";
		}

		Comparator<Event> get_sorting_procedure() {
			Comparator<Event> acum = (a, b) -> 0;
			for (final var sf : this.sorting_procedure) {
				if (sf.equals("reverseOrder"))
					acum = acum.reversed();
				else
					acum = acum.thenComparing(SortFunctions.valueOf(sf).sf);
			}
			return acum;
		}
	}

	public static <T, J> void printStatistics(final T[] a, final List<J> b, final BiPredicate<T, J> comp) {
		final var hs = new HashMap<T, List<J>>(b.size());
		for (final var x : a) {
			hs.put(x, new LinkedList<J>());
			for (final var y : b) {
				if (comp.test(x, y))
					hs.get(x).add(y);
			}
			System.out.println(x + " # : " + hs.get(x).size());
		}
	}

	/**
	 * generate Schedule
	 * 
	 * @apiNote this method is in desperate need of functional seperation.
	 * 
	 * @param settings
	 * @return
	 * @throws Exception
	 */
	public static List<Event> generateSchedule(ConfigFormat settings) throws Exception {

		final var eventStart = Instant.parse(settings.schedule_start);

		// get delay-list
		final var delayList = CSVHandler.CSVReader(settings.event_delay_input_file);

		// inisiate state used for generateEvents
		final var config = new Event.EventState(settings.break_after_each_event, eventStart);
		config.setDelaysFromFile(delayList);

		// get registration-list
		final var registrationList = CSVHandler.CSVReader(settings.registration_list_input_file);

		// generate athletes
		final var atls = Athletes.generateAthletes(registrationList);

		// generate groups
		final var grps = Groups.generateGroups(atls);

		// generate SubCompetition
		final var subcs = SubCompetition.generateSubCompetition(grps);

		// list of subcomps
		final var qualifiers = new LinkedList<SubCompetition>();
		final var finals = new LinkedList<SubCompetition>();

		final List<Event> all_events = new LinkedList<Event>();

		for (final var subComp : subcs)
			(subComp.isFinal ? finals : qualifiers).add(subComp);

		// qualifiers
		final var qualifier_events = Event.generateEvents(qualifiers, config);

		Collections.sort(qualifier_events, SortFunctions.SortByTime.sf.reversed());

		all_events.addAll(qualifier_events);

		final var qualifier_events_end = qualifier_events.get(0).endTime;

		// finals
		final var final_events_start = qualifier_events_end.truncatedTo(ChronoUnit.DAYS)
				.plus(1, ChronoUnit.DAYS).plus(8, ChronoUnit.HOURS);

		config.eventStart = final_events_start;

		final var finals_events = Event.generateEvents(finals, config);

		all_events.addAll(finals_events);

		Collections.sort(finals_events, SortFunctions.SortByTime.sf.reversed());

		final var final_events_end = finals_events.get(0).endTime;

		// ceremonies
		final var ceremonie_events_start = final_events_end.truncatedTo(ChronoUnit.DAYS)
				.plus(1, ChronoUnit.DAYS).plus(8, ChronoUnit.HOURS);

		final var ceremonie_events_end = ceremonie_events_start.plus(5, ChronoUnit.MINUTES);

		final var ceremonie_events = new LinkedList<Event>();

		finals_events.forEach(new Consumer<Event>() {
			int ceremonies_id = all_events.size();

			@Override
			public void accept(final Event e) {
				final var ceremonie = new Event(
						++ceremonies_id,
						ceremonie_events_start,
						ceremonie_events_end,
						e.subCompetition,
						STATIONS.Award_Ceremony_Area);

				ceremonie_events.add(ceremonie);
			}
		});

		all_events.addAll(Event.improveEvents(ceremonie_events));

		Collections.sort(all_events, settings.get_sorting_procedure());

		return all_events;
	}

	public Schedule(final String configFileName) throws Exception {

		final var settings = JSONHandler.ConfigReader(configFileName, ConfigFormat.class);

		var all_events = Schedule.generateSchedule(settings);

		System.out.println(settings);
		printSchedule(all_events, settings.schedule_output_file);
		System.out.println("-----------");
		printStatistics(DISCIPLINES.values(), all_events, (s, e) -> s == e.subCompetition.group.discipline);
		System.out.println("-----------");
		printStatistics(AGE_GROUPS.values(), all_events, (s, e) -> s == e.subCompetition.group.age_groups);
		System.out.println("-----------");
		printStatistics(SEX_GROUPS.values(), all_events, (s, e) -> s == e.subCompetition.group.sex_groups);
		System.out.println("-----------");
		printStatistics(STATIONS.values(), all_events, (s, e) -> s == e.station);
		System.out.println("-----------");
		System.out.println("# all events # : " + all_events.size());
		System.out.println("-----------");
	}

	public void printSchedule(final List<Event> events, final String fileName) throws Exception {
		final List<String[]> buffevents = new LinkedList<String[]>();

		buffevents.add(
				new String[] { "id", "startTime", "endTime", "type", "station", "discipline", "age",
						"sex", "athletes" });

		for (final var evt : events) {
			String athletes = "";
			if (evt.subCompetition.isFinal) {
				athletes = "TBD";
			} else {
				for (final var atl : evt.subCompetition.athletes) {
					athletes += atl.name + " " + atl.surname + "|";
				}
			}

			String type = "Qualifier";
			if (evt.subCompetition.isFinal) {
				type = "Final";
			}
			if (evt.station.equals(STATIONS.Award_Ceremony_Area)) {
				type = "A.C.";
			}

			final var res = new String[] {
					"" + evt.id,
					// "" + evt.subCompetition.id,
					// "" + evt.subCompetition.group.id,
					"" + evt.getStartTime(),
					"" + evt.getEndTime(),
					"" + type,
					"" + evt.station,
					"" + evt.subCompetition.group.discipline,
					"" + evt.subCompetition.group.age_groups,
					"" + evt.subCompetition.group.sex_groups,
					"" + athletes
			};
			buffevents.add(res);
		}

		CSVHandler.CSVWriter(fileName, buffevents);
	}
}
