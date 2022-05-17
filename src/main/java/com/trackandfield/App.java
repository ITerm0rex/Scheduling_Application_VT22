package com.trackandfield;

import java.io.FileReader;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

import com.trackandfield.io.CSVHandler;

public class App {

	class util {
		final static String CSV_FILE_PATH = "./resources/registration-list.csv";
		final static String CSV_NEWLINE_PATTERN = "(\\r\\n|\\n)+";

		/**
		 * {@summary Generete Athletes}
		 * 
		 * @return List<Athletes>
		 */
		public static List<Athletes> generateAthletes() {
			List<Athletes> athletes = new LinkedList<Athletes>();

			try (FileReader file = new FileReader(CSV_FILE_PATH);
					Scanner read = new Scanner(file)) {

				read.useDelimiter(CSV_NEWLINE_PATTERN);

				int id = 0;
				for (int line = 1; read.hasNext(); line++) {

					String[] values = read.next().split(",", -1);

					List<String> records = new LinkedList<String>();
					for (int i = 5; i < 16; i++) {
						records.add(values[i]);
					}

					if (values[0].isEmpty() || values[1].isEmpty() || values[2].isEmpty() || values[3].isEmpty()
							|| values[4].isEmpty()) {
						System.err.println("Error on line: " + line);
						continue;
					}

					athletes.add(new Athletes(
							++id,
							values[0],
							values[1],
							values[2],
							values[3].charAt(0),
							Integer.parseInt(values[4]),
							records));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return athletes;
		}

		/**
		 * {@summary Generete Groups}
		 * 
		 * @return List<Groups>
		 */
		public static List<Groups> generateGroups(final List<Athletes> athletes) {
			final List<Groups> groups = new ArrayList<Groups>(); // list to add groups

			int id = 0;
			for (final var athlete : athletes) {

				// all disciplines that the athlete participaces in
				final var athlete_disciplines = athlete.getDisciplines();
				final var athlete_ageGroup = athlete.getAgeGroup();
				final var athlete_sexGroup = athlete.getSexGroup();

				if (athlete_sexGroup == null)
					continue;

				if (athlete_ageGroup == null)
					continue;

				for (final var athlete_discipline : athlete_disciplines) { // iterate through all athletes
					var not_found = true;

					for (final var group : groups) { // iterate through all groups
						if (athlete_ageGroup.equals(group.age_groups)
								&& athlete_sexGroup.equals(group.sex_groups)
								&& athlete_discipline.equals(group.discipline)
								&& !group.athletes.contains(athlete)) {
							group.athletes.add(athlete);
							not_found = false;
							break; // if the athlete finds the appropriate group break from the loop
						}
					}
					if (not_found) {
						final var new_group = new Groups(
								++id,
								athlete_ageGroup,
								athlete_sexGroup,
								athlete_discipline,
								new LinkedList<Athletes>());
						groups.add(new_group); // if an athlete doesn't find a group create a new one
						new_group.athletes.add(athlete); // and add the athlete to it
					}
				}
			}
			return groups;
		}

		/**
		 * {@summary Generete SubCompetition}
		 * 
		 * @return List<SubCompetition>
		 */
		public static List<SubCompetition> generateSubCompetition(final List<Groups> groups) {
			final List<SubCompetition> subComps = new ArrayList<SubCompetition>();

			var id = 0;
			for (final var group : groups) {
				// put entire group into a buffer
				final var competitors = new LinkedList<Athletes>(group.athletes);

				double number_of_slots = group.discipline.slots; // slots from group discipline
				double number_of_competitors = competitors.size(); // Number of competitors in each group
				// number of qualifiers for each group
				var number_of_subSubComp = Math.ceil(number_of_competitors / number_of_slots);
				// max number of athletes for each qualifier
				var max_number_of_Athletes_in_subCom = Math.ceil(number_of_competitors / number_of_subSubComp);

				var isFinal = number_of_subSubComp == 1;
				// Case 1 example: 2 competitors but less slots. Create final and add both
				// athletes
				if (number_of_competitors == 2 && number_of_slots < 2) {
					number_of_subSubComp = 1;
					isFinal = true;
				}
				// Case 2 example: 20 athletes, 8 slots -> creates 3 qualifiers with 7, 7, 6
				// competitors each
				else if (number_of_competitors > number_of_slots) {
					isFinal = false;
				}
				// Case 3 example: 5 competitors, 8 slots -> create final and add athletes
				else if (number_of_competitors > 1) {
					isFinal = true;
				}

				for (int i = 0; i < number_of_subSubComp; i++) {
					var new_subComp = new SubCompetition(
							++id,
							isFinal,
							group,
							new LinkedList<Athletes>());
					subComps.add(new_subComp);
					// Adding athletes into qualifiers
					for (int j = 0; j < max_number_of_Athletes_in_subCom; j++) {
						// returns first athlete and removes it from the list
						var comp = competitors.pollFirst();
						if (comp == null)
							break;

						new_subComp.athletes.add(comp);
					}
				}
				if (!isFinal) {
					// Only adding a final with all athletes since we can't foresee
					// qualifier-winners
					var new_final = new SubCompetition(
							++id,
							true,
							group,
							group.athletes);

					subComps.add(new_final);
				}
			}
			return subComps;
		}

		/**
		 * {@summary Generate events for schedule}
		 * 
		 * @return List<Event>
		 */
		public static List<Event> generateEvents(final List<SubCompetition> subComps, Instant eventStart,
				List<String[]> delays) {

			var all_events = new LinkedList<Event>();

			int id = 0;
			int delay = 5; // 5 minutes break after all events

			for (final var subComp : subComps) {

				// Loop through stations
				for (var station : STATIONS.values()) {

					if (station.disciplines.contains(subComp.group.discipline)) {
						if ((id % 2 == 0) && Arrays.asList(
								STATIONS.High_Jump_l,
								STATIONS.Long_Triple_Jump_1,
								STATIONS.Shot_Throwing_l).contains(station))
							continue;

						var tempDelay = delay;

						for (var dely : delays) {
							if (dely[0].equals("" + (id + 1))) {
								tempDelay += Integer.parseInt(dely[1]);
							}
						}

						var endTime = eventStart.plus(subComp.group.discipline.durationMinutes + tempDelay,
								ChronoUnit.MINUTES);

						var new_event = new Event(++id, eventStart, endTime, subComp, station);

						// eventStart = endTime;

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

			while (temp_events.size() != 0) {
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
						event.addDelay(5);
					} else {
						result_events.add(event);
						temp_events.remove(event);
					}

				}
			}

			return result_events;
		}
	}

	public static void main(String[] args) {

		var atls = App.util.generateAthletes();
		// atls.removeIf(x -> x.id > 10);
		// atls.forEach(System.out::println);

		System.out.println("-----------");

		var grps = App.util.generateGroups(atls);
		// grps.removeIf(x -> x.id != 15);
		// grps.forEach(System.out::println);

		System.out.println("-----------");

		var subcs = App.util.generateSubCompetition(grps);
		// subcs.removeIf(x -> x.id > 1);
		// subcs.forEach(System.out::println);

		// if (true)
		// return;

		final var delays = CSVHandler.CSVReader("./resources/delay.csv");

		final var eventStart = Instant.parse("2022-05-20T08:00:00Z");

		enum SortFunctions {
			SorBytTime((a, b) -> a.startTime.compareTo(b.startTime)),
			SorBytStation((a, b) -> a.station.ordinal() - b.station.ordinal()),
			SorBytDiscipline((a, b) -> a.subCompetition.group.discipline.ordinal()
					- b.subCompetition.group.discipline.ordinal()),
			SorBytAgeGroup((a, b) -> a.subCompetition.group.age_groups.ordinal()
					- b.subCompetition.group.age_groups.ordinal()),
			SorBySexGroups((a, b) -> a.subCompetition.group.sex_groups.ordinal()
					- b.subCompetition.group.sex_groups.ordinal()),
			SorBytId((a, b) -> a.id - b.id);

			public final Comparator<Event> sf;

			SortFunctions(final Comparator<Event> sf) {
				this.sf = sf;
			}
		}

		final var qualifiers = new LinkedList<SubCompetition>(); // list of subcomps
		final var finals = new LinkedList<SubCompetition>(); // list of subcomps

		for (final var subComp : subcs) {
			(subComp.isFinal ? finals : qualifiers).add(subComp);
		}

		final var qualifier_events = App.util.generateEvents(qualifiers, eventStart, delays);

		Collections.sort(qualifier_events, SortFunctions.SorBytTime.sf.reversed());

		final var qualifier_events_end = qualifier_events.get(0).endTime;

		final var final_events_start = qualifier_events_end.truncatedTo(ChronoUnit.DAYS)
				.plus(1, ChronoUnit.DAYS).plus(8, ChronoUnit.HOURS);

		final var finals_events = App.util.generateEvents(finals, final_events_start, delays);

		qualifier_events.addAll(finals_events);

		Collections.sort(finals_events, SortFunctions.SorBytTime.sf.reversed());

		final var final_events_end = finals_events.get(0).endTime;

		final var ceremonie_events_start = final_events_end.truncatedTo(ChronoUnit.DAYS)
				.plus(1, ChronoUnit.DAYS).plus(8, ChronoUnit.HOURS);

		final var ceremonie_events_end = ceremonie_events_start.plus(5, ChronoUnit.MINUTES);

		final List<Event> ceremonie_events = new LinkedList<Event>();

		finals_events.forEach(new Consumer<Event>() {
			int ceremonies_id = qualifier_events.size();

			@Override
			public void accept(Event e) {
				final var ceremonie = new Event(
						++ceremonies_id,
						ceremonie_events_start,
						ceremonie_events_end,
						e.subCompetition,
						STATIONS.Award_Ceremony_Area);

				ceremonie_events.add(ceremonie);
			}
		});

		qualifier_events.addAll(App.util.improveEvents(ceremonie_events));

		var evts = qualifier_events;

		// Collections.sort(evts,
		// SortFunctions.SorBytStation.sf.thenComparing(SortFunctions.SorBytTime.sf));
		// Collections.sort(evts, SortFunctions.SorBytAgeGroup.sf.reversed());
		Collections.sort(evts, SortFunctions.SorBytTime.sf);

		// evts = evts.subList(0, 10);

		// TODO: fix duplicate ids from multiple generateEvents.
		// evts.removeIf(x -> x.id != 63);

		evts.forEach(System.out::println);

		System.out.println("-----------");

		List<String[]> buffevents = new LinkedList<String[]>();

		for (var evt : evts) {
			String athletes = "";
			if (evt.subCompetition.isFinal) {
				athletes = "TBD";
			} else {
				for (var atl : evt.subCompetition.athletes) {
					athletes += atl.name + " " + atl.surname + "|";
				}
			}
			var res = new String[] {
					"" + evt.id,
					"" + evt.getStartTime(),
					"" + evt.getEndTime(),
					"" + evt.station,
					"" + evt.subCompetition.group.discipline,
					"" + evt.subCompetition.group.age_groups,
					"" + evt.subCompetition.group.sex_groups,
					"" + athletes
			};
			buffevents.add(res);
		}

		CSVHandler.CSVWriter(buffevents, "./resources/test.csv");

		// buffevents.forEach(CSVHandler.rowPrint);

		// if (true)
		// return;

		var hs = new HashMap<STATIONS, List<Event>>(evts.size());

		for (var stat : STATIONS.values()) {
			hs.put(stat, new LinkedList<Event>());
			for (var evt : evts) {
				if (evt.station == stat) {
					hs.get(stat).add(evt);
				}
			}
		}

		hs.keySet().forEach(k -> System.out.println(k + " # : " + hs.get(k).size()));

		System.out.println("::finals # : " + finals_events.size());
		System.out.println("::events # : " + evts.size());
	}
}