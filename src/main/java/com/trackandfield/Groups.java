package com.trackandfield;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.CheckForNull;

enum AGE_GROUPS {
	_8("Under 8"),
	_9_10("9 to 10"),
	_11_12("11 to 12"),
	_13_14("13 to 14"),
	_15_16("15 to 16"),
	_17_18("17 to 18"),
	ADULT("Adult");

	final private String description;

	AGE_GROUPS(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return description;
	}
}

enum SEX_GROUPS {
	M,
	F
}

enum DISCIPLINES {
	Running_Sprint_60(8, 5),
	Running_Sprint_200(6, 5),
	Running_Middle_800(6, 5),
	Running_Middle_1500(6, 10),
	Running_Long_3000(6, 15),
	Running_Hurdles_60(8, 5),
	Jumping_Long(1, 5),
	Jumping_Triple(1, 5),
	Jumping_High(1, 15),
	Jumping_Pole(1, 5),
	Throwing_Shot(1, 5);

	final public int slots;
	final public int durationMinutes;

	DISCIPLINES(int slots, int durationMinutes) {
		this.slots = slots;
		this.durationMinutes = durationMinutes;
	}

	@Override
	public String toString() {
		return super.toString().replace('_', ' ');
	}
}

class Groups {
	int id;
	AGE_GROUPS age_groups;
	SEX_GROUPS sex_groups;
	DISCIPLINES discipline;
	List<Athletes> athletes;

	public Groups(int id, AGE_GROUPS age_groups, SEX_GROUPS sex_groups, DISCIPLINES discipline,
			List<Athletes> athletes) {
		this.id = id;
		this.age_groups = age_groups;
		this.sex_groups = sex_groups;
		this.discipline = discipline;
		this.athletes = athletes;
	}

	@Override
	public String toString() {
		return "Groups ["
				+ "id=" + id
				+ ", athletes#=" + athletes.size()
				+ ", age=" + age_groups
				+ ", sex=" + sex_groups
				+ ", discipline=" + discipline
				+ "]";
	}

}

abstract class AthleteGroupAttributes {
	char sex;
	int age;
	List<String> records;

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

}