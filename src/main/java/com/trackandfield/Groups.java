package com.trackandfield;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

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
	Running_Sprint_60(8, runningCompsAproxTime(0)),
	Running_Sprint_200(6, runningCompsAproxTime(1)),
	Running_Middle_800(6, runningCompsAproxTime(2)),
	Running_Middle_1500(6, runningCompsAproxTime(3)),
	Running_Long_3000(6, runningCompsAproxTime(4)),
	Running_Hurdles_60(8, runningCompsAproxTime(5)),
	Jumping_Long(1, s -> {
		return Duration.ofMinutes(5);
	}),
	Jumping_Triple(1, Duration.ofMinutes(5)),
	Jumping_High(1, Duration.ofMinutes(5)),
	Jumping_Pole(1, Duration.ofMinutes(5)),
	Throwing_Shot(1, Duration.ofMinutes(5));

	final public int slots;
	final public Duration durationMinutes;
	final public Function<SubCompetition, Duration> timeAproxFunction;

	/**
	 * 
	 * @param slots
	 * @param timeAproxFunction
	 */
	DISCIPLINES(int slots, Function<SubCompetition, Duration> timeAproxFunction) {
		this.slots = slots;
		this.durationMinutes = Duration.ZERO;
		this.timeAproxFunction = timeAproxFunction;
	}

	/**
	 * 
	 * @param slots
	 * @param durationMinutes
	 */
	DISCIPLINES(int slots, Duration durationMinutes) {
		this.slots = slots;
		this.durationMinutes = durationMinutes;
		this.timeAproxFunction = s -> durationMinutes;
	}

	@Override
	public String toString() {
		return super.toString().replace('_', ' ');
	}

	/**
	 * function that return runningComps function .
	 * 
	 * @param index
	 * @return Function
	 */
	private static Function<SubCompetition, Duration> runningCompsAproxTime(int index) {
		return s -> {
			var max_time = Duration.ZERO;
			for (var atl : s.athletes) {
				Double sum = 0.0;
				var tt = atl.records.get(index).split(":", -1);
				if (tt.length == 2) {
					sum += Double.parseDouble(tt[0]) * 60.0;
					sum += Double.parseDouble(tt[1]);
				} else {
					sum += Double.parseDouble(tt[0]);
				}
				var time = Duration.ofSeconds(sum.longValue());
				if (max_time.compareTo(time) < 0)
					max_time = time;
			}
			if (max_time.truncatedTo(ChronoUnit.MINUTES).equals(max_time))
				return max_time;

			return max_time.truncatedTo(ChronoUnit.MINUTES).plus(1, ChronoUnit.MINUTES);
		};
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

	/**
	 * {@summary Generete Groups}
	 * 
	 * @return List<Groups>
	 */
	public static List<Groups> generateGroups(final List<Athletes> athletes) {
		final List<Groups> groups = new LinkedList<Groups>(); // list to add groups

		int id = 0;
		for (final var athlete : athletes) {

			// all disciplines that the athlete participaces in
			final var athlete_disciplines = athlete.getDisciplines();
			final var athlete_ageGroup = athlete.getAgeGroup();
			final var athlete_sexGroup = athlete.getSexGroup();

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
							new LinkedList<Athletes>());
					groups.add(new_group); // if an athlete doesn't find a group create a new one
					new_group.athletes.add(athlete); // and add the athlete to it
				}
			}
		}
		return groups;
	}
}