package com.trackandfield;

import java.io.FileReader;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class App {

	class io {
		final static String CSV_FILE_PATH = "./resources/registration-list.csv";
		final static String CSV_NEWLINE_PATTERN = "(\\r\\n|\\n)+";

		public List<Athletes> generateAthletes() {
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

		public List<Groups> generateGroups() {
			final List<Groups> groups = new ArrayList<Groups>(); // list to add groups
			final List<Athletes> athletes = generateAthletes(); // list of all athletes

			int count = 0;

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
						count++;
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

			System.err.println("generateGroups() iterations#:" + count);

			return groups;
		}

		// Generete subcomps

		public List<SubCompetition> generateSubCompetition(final List<Groups> groups) {

			final List<SubCompetition> subComps = new ArrayList<SubCompetition>(); // list of subcomps

			var id = 0;
			for (final var group : groups) {
				// put entire group into a buffer
				final var competitors = new LinkedList<Athletes>(group.athletes);

				double number_of_competitors = competitors.size();
				double number_of_subSubComp = 0;

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
						number_of_slots = 2;
						durationMinutes = 5;
						break;
					case Running_Sprint_200:
					case Running_Middle_800:
						number_of_slots = 8;
						durationMinutes = 5;
						break;
					case Running_Middle_1500:
						number_of_slots = 8;
						durationMinutes = 10;
						break;
					case Running_Long_3000:
						number_of_slots = 8;
						durationMinutes = 15;
						break;
					default:
						number_of_slots = 1;
						durationMinutes = 5;
						break;
				}

				// If more competitors than station spots, create qualifiers
				// if (number_of_competitors > number_of_slots) {

				number_of_subSubComp = Math.ceil(number_of_competitors / number_of_slots);
				// the main sub competetion is divided into smaller sub competetions if nr of
				// group members is more than nr of slots : ceil(20 / 8) = 3
				var max_number_of_Athletes_in_subCom = Math.ceil(number_of_competitors / number_of_subSubComp);
				// number of Athlets in a sub_comp ceil(20 / 3) = 7

				// [7][7][6]
				// [1][1][1]...

				for (int i = 0; i < number_of_subSubComp; i++) {
					// Create new_subComp competition by calling SubCompetition constructor
					var new_subComp = new SubCompetition(
							++id,
							0,
							"group#:" + group.id,
							durationMinutes,
							number_of_subSubComp == 1,
							group.discipline,
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
				// Store sub competition in the sub competition list
				// }
				// Create a final
				// }
				// } else if (number_of_competitors < number_of_slots && number_of_competitors >
				// 1) {

				// }
				// System.out.print("[[Final]]: ");
				// Create only a final (not enough competitors for a qualifier)
			}
			// System.out.println(
			// "Number of qualifers: " + number_of_subSubComp + ", Duration in minutes: " +
			// durationMinutes);
			// }

			return subComps;
		}

	}

	public static void main(String[] args) {

		// if (true)
		// return;

		// SQLiteJDBC.run();

		var a = new App();

		// var t = a.new Throwing();
		// t.delay = 1;
		// t.Sub_compet();

		io test = a.new io();

		var grps = test.generateGroups();
		// grps.retainAll(grp)
		// grps.removeIf(filter)

		for (var gr : grps)
			System.out.println(gr);

		var subs = test.generateSubCompetition(grps);
		for (var sub : subs)
			System.out.println(sub);

		// System.out.println(subs.get(1));
		// System.out.println(subs.get(2));

		if (true)
			return;
		// var atls = test.generateAthletes();

		// var als = test.generateGroups();
		// System.out.println(als.get(1));

		// atls.get(0).test();
		// atls.get(0).;

		// for (var athlete : atls) {
		// System.out.println(athlete.id);
		// }

		// System.out.println(atls.get(0));
		// System.out.println(atls.get(0).getDisciplines());
		// System.out.println(atls.get(0).getAgeGroup());
		// System.out.println(atls.get(0).getSexGroup());

		var gs = test.generateGroups();

		// var subComp = new Sub_competitions(gs);

		System.out.println("number of groups:" + gs.size());

		for (var g : gs) {
			System.out.println(g.toString());
		}

		// String s = " " + gs.get(0);

		// for (var g : gs) {
		// System.out.println(g.athletes.size());
		// }

		// System.out.println(", ,,".split(",")[0]);

		// Sub_competition<float[]> a = new Sub_competition<float[]>();

	}
}