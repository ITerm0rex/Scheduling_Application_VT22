package com.trackandfield;

import java.io.FileReader;
import java.util.Scanner;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class App {

	class util {
		final static String CSV_FILE_PATH = "./resources/registration-list.csv";
		final static String CSV_NEWLINE_PATTERN = "(\\r\\n|\\n)+";

		public static List<Athletes> generateAthletes() {
			List<Athletes> athletes = new ArrayList<Athletes>();

			try (FileReader file = new FileReader(CSV_FILE_PATH);
					Scanner read = new Scanner(file)) {

				read.useDelimiter(CSV_NEWLINE_PATTERN);

				int id = 0;
				for (int line = 1; read.hasNext(); line++) {

					String[] values = read.next().split(",", -1);

					List<String> records = new ArrayList<String>();
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

		public static List<Groups> generateGroups(final List<Athletes> athletes) {
			final List<Groups> groups = new ArrayList<Groups>(); // list to add groups

			int id = 0;
			for (final var athlete : athletes) {

				final var athlete_ageGroup = athlete.getAgeGroup();
				final var athlete_sexGroup = athlete.getSexGroup();
				final var athlete_disciplines = athlete.getDisciplines(); // all disciplines that the athlete
																			// participaces in

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
								new ArrayList<Athletes>());
						groups.add(new_group); // if an athlete doesn't find a group create a new one
						new_group.athletes.add(athlete); // and add the athlete to it
					}
				}

			}

			return groups;
		}

		// Generete subcomps

		public static List<SubCompetition> generateSubCompetition(final List<Groups> groups) {
			final List<SubCompetition> subComps = new ArrayList<SubCompetition>(); // list of subcomps

			var id = 0;
			for (final var group : groups) {
				// put entire group into a buffer
				final var competitors = new LinkedList<Athletes>(group.athletes);

				// Get slots and durationMinutes from groups method
				double number_of_slots;
				int durationMinutes;
				switch (group.discipline) {
					case Running_Sprint_60:
					case Running_Hurdles_60:
						number_of_slots = 8;
						durationMinutes = 5;
						break;
					case Running_Sprint_200:
					case Running_Middle_800:
						number_of_slots = 6;
						durationMinutes = 5;
						break;
					case Running_Middle_1500:
						number_of_slots = 6;
						durationMinutes = 10;
						break;
					case Running_Long_3000:
						number_of_slots = 6;
						durationMinutes = 15;
						break;
					default:
						number_of_slots = 1;
						durationMinutes = 5;
						break;
				}

				double number_of_competitors = competitors.size(); // Number of competitors in each group
				// number of qualifiers for each group
				var number_of_subSubComp = Math.ceil(number_of_competitors / number_of_slots);
				// max number of athletes for each qualifier
				var max_number_of_Athletes_in_subCom = Math.ceil(number_of_competitors / number_of_subSubComp);

				// Case 1 example: 2 competitors but less slots. Create final and add both
				// athletes
				if (number_of_competitors == 2 && number_of_slots < 2) {
					var new_final = new SubCompetition(
							++id,
							durationMinutes,
							true,
							group,
							new ArrayList<Athletes>());

					subComps.add(new_final);

					// Adding athletes to final
					for (int j = 0; j < number_of_competitors; j++) {
						var comp = competitors.pollFirst(); // returns first athlete and removes it from the list
						if (comp == null)
							break;
						new_final.athletes.add(comp);
					}
				}

				// Case 2 example: 20 athletes, 8 slots -> creates 3 qualifiers with 7, 7, 6
				// competitors each
				else if (number_of_competitors > number_of_slots) {
					for (int i = 0; i < number_of_subSubComp; i++) {
						var new_subComp = new SubCompetition(
								++id,
								durationMinutes,
								false,
								group,
								new ArrayList<Athletes>());

						subComps.add(new_subComp);

						// Adding athletes into qualifiers
						for (int j = 0; j < max_number_of_Athletes_in_subCom; j++) {
							var comp = competitors.pollFirst(); // returns first athlete and removes it from the list
							if (comp == null)
								break;

							new_subComp.athletes.add(comp);
						}
					}

					// Only adding a final without athletes since we can't foresee qualifier-winners
					var new_final = new SubCompetition(
							++id,
							durationMinutes,
							true,
							group,
							new ArrayList<Athletes>());

					subComps.add(new_final);
				}

				// Case 3 example: 5 competitors, 8 slots -> create final and add athletes
				else if (number_of_competitors > 1) {
					var new_final = new SubCompetition(
							++id,
							durationMinutes,
							true,
							group,
							new ArrayList<Athletes>());

					subComps.add(new_final);

					for (int j = 0; j < max_number_of_Athletes_in_subCom; j++) {
						var comp = competitors.pollFirst(); // returns first athlete and removes it from the list
						if (comp == null)
							break;

						new_final.athletes.add(comp);
					}
				}
			}
			return subComps;
		}

		// Generate events for schedule
		public static List<Event> generateEvents(final List<SubCompetition> subComps) {
			// TODO: finish implementation.
			for (var stat : Stations.stations) {
				System.out.println(stat.name);
			}

			final List<SubCompetition> qualifiers = new ArrayList<SubCompetition>(); // list of subcomps
			final List<SubCompetition> finals = new ArrayList<SubCompetition>(); // list of subcomps

			for (final var subComp : subComps) {
				if (subComp.isFinal)
					finals.add(subComp);
				else
					qualifiers.add(subComp);
			}

			int eventStart = 0;
			for (final var qualifier : qualifiers) {
				var age_groups = qualifier.group.age_groups;
				var sex_groups = qualifier.group.sex_groups;

				var new_event = new Event(eventStart, qualifier);
				eventStart += new_event.endTime;

			}

			return null;
		}
	}

	public static void main(String[] args) {

		var atls = App.util.generateAthletes();
		// atls.removeIf(x -> x.id > 10);
		// for (var atl : atls)
		// System.out.println(atl);

		System.out.println("-----------");

		var grps = App.util.generateGroups(atls);
		// grps.removeIf(x -> x.id != 15);
		// for (var grp : grps)
		// System.out.println(grp);

		System.out.println("-----------");

		var subcs = App.util.generateSubCompetition(grps);
		// subcs.removeIf(x -> x.id > 1);
		// for (var subc : subcs)
		// System.out.println(subc);

		System.out.println("-----------");

		var evts = App.util.generateEvents(subcs);

	}
}