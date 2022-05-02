package com.trackandfield;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
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

			var groups = new ArrayList<Groups>();

			List<Athletes> athletes = generateAthletes();
			
			for (var athlete : athletes) {
				// alt1.sex = "M";
				// athlete.sorted().sex("M");
				// athlete.sorted().sex("F");
				// this.Sex = sex;
				// System.out.println(athlete);

			}

			return null;
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

	class Groups {
		// final static List<int[]> Age_Intervalsss = ;

		final static int[][] Age_Intervals = { { 0, 8 }, { 9, 10 }, { 11, 12 }, { 13, 14 }, { 15, 16 }, { 17, 18 },
				{ 19, 0x7FFFFFFF /* biggest positive integer */ } };
		final static char[] Sexes = { 'M', 'F' };

		int id;
		int age;
		char sex;
		String name;
		List<Athletes> Athletes;

		public AGE_GROUPS getAgeGroup(Athletes atl) {

			if (atl.age < 8)
				return AGE_GROUPS._8;

			if (atl.age == 17 || atl.age == 18)
				return AGE_GROUPS._17_18;

			if (atl.age > 18)
				return AGE_GROUPS.ADOULT;

			return AGE_GROUPS.INVALID;
		}

		public Groups(int id, int age, char sex, String name, List<com.trackandfield.App.Athletes> athletes) {
			this.id = id;
			this.age = age;
			this.sex = sex;
			this.name = name;
			Athletes = athletes;
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

		for (var athlete : test.generateAthletes()) {
			System.out.println(athlete.id);
		}

		// System.out.println(", ,,".split(",")[0]);

		// Sub_competition<float[]> a = new Sub_competition<float[]>();

	}

}