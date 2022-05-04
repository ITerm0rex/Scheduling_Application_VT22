package com.trackandfield;

import java.io.FileReader;
import java.io.IOException;
// import java.io.PrintWriter;
// import java.lang.ProcessBuilder.Redirect.Type;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.regex.Pattern;

// import org.javatuples.Pair;
// import org.javatuples.Tuple;

import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import com.trackandfield.*;

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

	}

	public static void main(String[] args) {

		// SQLiteJDBC.run();

		// if (true)
		// return;

		var a = new App();

		// var t = a.new Throwing();
		// t.delay = 1;
		// t.Sub_compet();

		io test = a.new io();

		// var atls = test.generateAthletes();

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