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

				double number_of_slots;
				int durationMinutes;
				switch (group.discipline) {
					case Running_Sprint_60:
					case Running_Hurdles_60:
						number_of_slots = 8;
						durationMinutes = 5;
						break;
					case Jumping_Long:
					case Jumping_Triple:
					case Jumping_High:
						number_of_slots = 1;
						durationMinutes = 5;
						break;
					case Throwing_Shot:
						number_of_slots = 1;
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

				double number_of_competitors = competitors.size();

				var number_of_subSubComp = Math.ceil(number_of_competitors / number_of_slots);
				// the main sub competetion is divided into smaller sub competetions if nr of
				// group members is more than nr of slots : ceil(20 / 8) = 3
				var max_number_of_Athletes_in_subCom = Math.ceil(number_of_competitors / number_of_subSubComp);
				// number of Athlets in a sub_comp ceil(20 / 3) = 7

				// [7][7][6]
				// [1][1][1]...

				// IF there are more competitors than number of slots, we need to create
				// qualifiers first
				// A final will be held after the finals, so we create a final

				if (number_of_competitors == 2 && number_of_slots < 2) {
					var new_final = new SubCompetition(
							++id,
							durationMinutes,
							true,
							group,
							new ArrayList<Athletes>());

					subComps.add(new_final);

					for (int j = 0; j < number_of_competitors; j++) {
						var comp = competitors.pollFirst(); // returns first athlete and removes it from the list
						if (comp == null)
							break;
						// Add athlete to athletes
						new_final.athletes.add(comp);
					}
				}

				else if (number_of_competitors > number_of_slots) {

					for (int i = 0; i < number_of_subSubComp; i++) {
						// Create new_subComp competition by calling SubCompetition constructor
						var new_subComp = new SubCompetition(
								++id,
								durationMinutes,
								false,
								group,
								new ArrayList<Athletes>());
						// 1-sub-com 0 -->7
						// 2- sub-com 7--> 1
						// 3- sub-comp 14-->20
						subComps.add(new_subComp);

						for (int j = 0; j < max_number_of_Athletes_in_subCom; j++) {
							var comp = competitors.pollFirst(); // returns first athlete and removes it from the list
							if (comp == null)
								break;
							// Add athlete to athletes
							new_subComp.athletes.add(comp);
						}
					}

					// When the final is created, we don't need to worry about the list of athletes,
					// since
					// that cannot be pre-determined. If the list is empty, we will simply print out
					// "TBD (to be determined)"
					// in our schedule
					var new_final = new SubCompetition(
							++id,
							durationMinutes,
							true,
							group,
							new ArrayList<Athletes>());

					subComps.add(new_final);

				}
				// If all of the competitors can fit inside of one station, or we have more than
				// 1 competitor in said group, we will only create a final.
				// Neither a qualifier or a final would get created if there is only 1 athlete
				// in a group.

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
						// Add athlete to athletes
						new_final.athletes.add(comp);
					}
				}
			}
			return subComps;
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
		for (var grp : grps)
			System.out.println(grp);

		System.out.println("-----------");

		var subcs = App.util.generateSubCompetition(grps);
		// subcs.removeIf(x -> x.id > 1);
		for (var subc : subcs)
			System.out.println(subc);

	}
}