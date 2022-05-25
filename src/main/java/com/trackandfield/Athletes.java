package com.trackandfield;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.CheckForNull;

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
		return "Athletes [id=" + id
				+ ", club=" + club
				+ ", name=" + name
				+ ", surname=" + surname
				+ ", sex=" + sex
				+ ", age=" + age
				+ ", records=" + records
				+ "]";
	}

	/**
	 * @return List of corresponding disciplines
	 */
	final public List<DISCIPLINES> getDisciplines() {
		// disciplines is a list of all disiplines.
		List<DISCIPLINES> disciplines = new ArrayList<>(Arrays.asList(DISCIPLINES.values()));

		int i = 0;
		// Removes the corresponding discipline if record is empty
		for (var record : this.records) {
			if (record.isEmpty())
				disciplines.remove(i);
			else
				i++;
		}
		return disciplines;
	}

	/**
	 * @return sex of athlete
	 */
	@CheckForNull
	final public SEX_GROUPS getSexGroup() {
		if (this.sex == 'M')
			return SEX_GROUPS.M;

		if (this.sex == 'F')
			return SEX_GROUPS.F;

		return null;
	}

	/**
	 * @return age group of athlete
	 */
	@CheckForNull
	final public AGE_GROUPS getAgeGroup() {
		if (this.age <= 8)
			return AGE_GROUPS._8;

		if (this.age == 9 || this.age == 10)
			return AGE_GROUPS._9_10;

		if (this.age == 11 || this.age == 12)
			return AGE_GROUPS._11_12;

		if (this.age == 13 || this.age == 14)
			return AGE_GROUPS._13_14;

		if (this.age == 15 || this.age == 16)
			return AGE_GROUPS._15_16;

		if (this.age == 17 || this.age == 18)
			return AGE_GROUPS._17_18;

		if (this.age > 18)
			return AGE_GROUPS.ADULT;

		return null;
	}

	/**
	 * {@summary Generete Athletes}
	 * 
	 * @return List<Athletes>
	 * @throws Exception
	 */
	public static List<Athletes> generateAthletes(final List<String[]> registration_list) throws Exception {
		List<Athletes> athletes = new LinkedList<Athletes>();
		int id = 0;
		int disiplines_length = DISCIPLINES.values().length;
		for (var entry : registration_list) {
			id++;
			try {
				athletes.add(new Athletes(
						id,
						entry[0],
						entry[1],
						entry[2],
						entry[3].charAt(0),
						Integer.parseInt(entry[4]),
						Arrays.asList(entry).subList(5, 5 + disiplines_length)));
			} catch (Exception e) {
				throw new Exception("Invalid format of registration-list: entry: " + id + "\n" + e, e);
			}
		}
		return athletes;
	}

	public static Object generateSubCompetition(String subcompetition) {
		return null;
	}
}