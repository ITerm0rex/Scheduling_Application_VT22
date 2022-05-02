package com.trackandfield;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class App {

	class io {
		final static String csvPath = "./resources/registration-list.csv";

		static void read() {
			try (FileReader file = new FileReader(csvPath)) {
				Scanner read = new Scanner(file);

				read.useDelimiter("\n");
				while (read.hasNext()) {
					String[] values = read.next().split(",");
					if (values[3].equals("M") && Integer.parseInt(values[4]) <= 8) {
						System.out.println(values[1]);
					}
				}
				read.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public List<Athletes> generateAthletes() {
			int id = 0;
			try (FileReader file = new FileReader(csvPath)) {
				Scanner read = new Scanner(file);

				read.useDelimiter("\n");

				List<Athletes> athletes = new ArrayList<Athletes>();

				for (int j = 1; read.hasNext(); j++) {

					String[] values = read.next().split(",", -1);

					List<String> records = new ArrayList<String>();
					for (int i = 5; i < 16; i++) {
						records.add(values[i]);
					}

					if (values[0].isEmpty() || values[1].isEmpty() || values[2].isEmpty() || values[3].isEmpty()
							|| values[4].isEmpty()) {
						System.err.println("Error on line: " + j);
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
				read.close();

				return athletes;
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		public List<Groups> generateGroups() {
			int id = 0;
			List<Groups> groups = new ArrayList<Groups>();
			List<Athletes> athletes = generateAthletes();

			// var temp = generateAthletes();
			// List<Athletes> athletes = new ArrayList<Athletes>();

			// for(int i = 0; i < 20; i++)
			// athletes.add(temp.get(i));

			int count = 0;

			for (var athlete : athletes) {

				AGE_GROUPS athlete_ageGroup = Groups.getAgeGroup(athlete);
				SEX_GROUPS athlete_sexGroup = Groups.getsexGroup(athlete);
				List<DISCIPLINES> athlete_disciplines = Groups.getDisciplines(athlete);

				for (var athlete_discipline : athlete_disciplines) {
					boolean not_found = true;

					for (var group : groups) {
						if (group.age == athlete_ageGroup && group.sex == athlete_sexGroup
								&& group.discipline == athlete_discipline) {
							if (!group.athletes.contains(athlete)) {
								group.athletes.add(athlete);
								not_found = false;
							}
						}
						count++;
					}
					if (not_found) {
						var new_group = new Groups(
								++id,
								athlete_ageGroup,
								athlete_sexGroup,
								athlete_discipline,
								new ArrayList<Athletes>());
						groups.add(new_group);
						new_group.athletes.add(athlete);
					}

				}

			}

			System.err.println(count);

			return groups;
		}

	}

	enum AGE_GROUPS {
		_8,
		_9_10,
		_11_12,
		_13_14,
		_15_16,
		_17_18,
		ADOULT,
		INVALID
	}

	enum SEX_GROUPS {
		M,
		F,
		INVALID
	}

	enum DISCIPLINES {
		Running_Sprint_60,
		Running_Sprint_200,
		Running_Middle_800,
		Running_Middle_1500,
		Running_Long_3000,
		Running_Hurdles_60,
		Jumping_Long,
		Jumping_Triple,
		Jumping_High,
		Jumping_Pole,
		Throwing_Shot,
		INVALID
	}

	class Groups {
		int id;
		AGE_GROUPS age;
		SEX_GROUPS sex;
		DISCIPLINES discipline;
		List<Athletes> athletes;

		public Groups(int id, AGE_GROUPS age, SEX_GROUPS sex, DISCIPLINES discipline,
				List<com.trackandfield.App.Athletes> athletes) {
			this.id = id;
			this.age = age;
			this.sex = sex;
			this.discipline = discipline;
			this.athletes = athletes;
		}

		public void AddAthlete(Athletes athlete) {
			athletes.add(athlete);
		}

		public static List<DISCIPLINES> getDisciplines(Athletes athlete) {
			// dis is a list of all disiplines
			List<DISCIPLINES> dis = new ArrayList<DISCIPLINES>(Arrays.asList(DISCIPLINES.values()));

			int i = 0;
			// Removes the corresponding discipline if record is empty
			for (var rec : athlete.records) {
				if (rec.isEmpty())
					dis.remove(i);
				else
					i++;
			}
			dis.remove(DISCIPLINES.INVALID);
			return dis;
		}

		// Check and return the sex of an athlete
		public static SEX_GROUPS getsexGroup(Athletes athlete) {
			if (athlete.sex == 'M')
				return SEX_GROUPS.M;

			if (athlete.sex == 'F')
				return SEX_GROUPS.F;

			return SEX_GROUPS.INVALID;
		}

		// Check and return the age group of an athlete
		public static AGE_GROUPS getAgeGroup(Athletes athlete) {
			if (athlete.age <= 8)
				return AGE_GROUPS._8;

			if (athlete.age == 9 || athlete.age == 10)
				return AGE_GROUPS._9_10;

			if (athlete.age == 11 || athlete.age == 12)
				return AGE_GROUPS._11_12;

			if (athlete.age == 13 || athlete.age == 14)
				return AGE_GROUPS._13_14;

			if (athlete.age == 15 || athlete.age == 16)
				return AGE_GROUPS._15_16;

			if (athlete.age == 17 || athlete.age == 18)
				return AGE_GROUPS._17_18;

			if (athlete.age > 18)
				return AGE_GROUPS.ADOULT;

			return AGE_GROUPS.INVALID;
		}

		@Override
		public String toString() {
			return "Groups [age=" + age + ", athletes#=" + athletes.size() + ", discipline=" + discipline + ", id=" + id
					+ ", sex=" + sex + "]";
		}

	}

	class Athletes {
		int id;
		String club;
		String name;
		String surname;
		char sex;
		int age;
		List<String> records;

		public Athletes(int id, String club, String name, String surname, char sex, int age, List<String> records) {
			this.id = id;
			this.club = club;
			this.name = name;
			this.surname = surname;
			this.sex = sex;
			this.age = age;
			this.records = records;
		}

		@Override
		public String toString() {
			return "Athletes [age=" + age + ", club=" + club + ", id=" + id + ", name=" + name + ", records=" + records
					+ ", sex=" + sex + ", surname=" + surname + "]";
		}
	}

	class Schedule {
		String place;
		String date;
		String time;
		String group;
		List<Athletes> competetors;
		String displine;
		String station;
		String Round;

		public void CreateSchudle() {

		}

		public void updateSchudle() {

		}

		public void printSchudle() {

		}
	}

	class Stations {
		int id;
		String place;
		int slots;
		boolean empty;
	}

	class Sub_competition {
		int id;
		String name;
		String datum;
		int start_finish_time;
		int delay;
		List<Athletes> Athlets;

		void Sub_compet() {
			try {
				System.out.println("which sub competition and how long it takes" + id + delay);
				TimeUnit.MINUTES.sleep(delay);
				System.out.println(new Date());
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	class Throwing extends Sub_competition {
		int trials;
	}

	class Jumping extends Sub_competition {
		String type;
		int trials;
	}

	class Running extends Sub_competition {
		String distance;
		int trials;
	}

	public static void main(String[] args) {
		System.out.println(123);

		// SQLiteJDBC.qqq();

		// io.read();

		var a = new App();

		io test = a.new io();

		// var atl = test.generateAthletes();

		// for (var athlete : atl) {
		// System.out.println(athlete.id);
		// }

		// System.out.println(App.Groups.getDisciplines(atl.get(0)));
		// System.out.println(App.Groups.getAgeGroup(atl.get(0)));
		// System.out.println(App.Groups.getsexGroup(atl.get(0)));

		// a.new Groups(0, atl.athlets , al1.athlete_sexGroup,at1.athlete_discipline
		// discipline, athletes)

		var gs = test.generateGroups();

		for (var g : gs) {
			System.out.println(g.toString());
		}

		// String s = " " + gs.get(0);

		// for (var g : gs) {
		// System.out.println(g.athletes.size());
		// }

		// System.out.println(gs.size());

		// System.out.println(", ,,".split(",")[0]);

		// Sub_competition<float[]> a = new Sub_competition<float[]>();

	}

}