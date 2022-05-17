package com.trackandfield;

import java.util.Arrays;
import java.util.List;

enum STATIONS {
	Running_circle_400m("Running circle 400m", Arrays.asList(
			DISCIPLINES.Running_Sprint_200,
			DISCIPLINES.Running_Middle_800,
			DISCIPLINES.Running_Middle_1500,
			DISCIPLINES.Running_Long_3000)),
	Sprin_Line_60m("Sprint Line 60m", Arrays.asList(
			DISCIPLINES.Running_Sprint_60,
			DISCIPLINES.Running_Hurdles_60)),
	Long_Triple_Jump_1("Long/Triple Jump 1", Arrays.asList(
			DISCIPLINES.Jumping_Long,
			DISCIPLINES.Jumping_Triple)),
	Long_Triple_Jump_2("Long/Triple Jump 2", Arrays.asList(
			DISCIPLINES.Jumping_Long,
			DISCIPLINES.Jumping_Triple)),
	High_Jump_l("High Jump 1", Arrays.asList(
			DISCIPLINES.Jumping_High)),
	High_Jump_ll("High Jump 2", Arrays.asList(
			DISCIPLINES.Jumping_High)),
	Pole_Vault("Pole Vault", Arrays.asList(
			DISCIPLINES.Jumping_Pole)),
	Shot_Throwing_l("Shot Throwing 1", Arrays.asList(
			DISCIPLINES.Throwing_Shot)),
	Shot_Throwing_ll("Shot Throwing 2", Arrays.asList(
			DISCIPLINES.Throwing_Shot)),
	Award_Ceremony_Area("Award Ceremony Area",
			Arrays.asList(DISCIPLINES.values()));

	final String name;
	final List<DISCIPLINES> disciplines;

	STATIONS(String name, List<DISCIPLINES> disciplines) {
		this.name = name;
		this.disciplines = disciplines;
	}

	@Override
	public String toString() {
		return name;
	}
}