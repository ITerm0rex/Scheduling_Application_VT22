package com.trackandfield;

import java.util.List;

class Athletes extends AthleteGroupAttributes {
	int id;
	String club;
	String name;
	String surname;

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
}