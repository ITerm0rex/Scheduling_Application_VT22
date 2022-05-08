package com.trackandfield;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

enum AGE_GROUPS {
	_8,
	_9_10,
	_11_12,
	_13_14,
	_15_16,
	_17_18,
	ADULT,
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
		disciplines.remove(DISCIPLINES.INVALID); // removes invalid
		return disciplines;
	}

	// Check and return the sex of an athlete
	@Nullable
	final public SEX_GROUPS getSexGroup() {
		if (this.sex == 'M')
			return SEX_GROUPS.M;

		if (this.sex == 'F')
			return SEX_GROUPS.F;

		return null;
	}

	// Check and return the age group of an athlete
	@Nullable
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