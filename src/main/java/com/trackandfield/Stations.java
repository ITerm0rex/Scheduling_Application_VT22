package com.trackandfield;

import java.util.ArrayList;
import java.util.List;

class Stations {
	String name;
	List<Event> events;
	DISCIPLINES[] disciplines;

	public Stations(String name, DISCIPLINES[] disciplines) {
		this.name = name;
		this.disciplines = disciplines;
		this.events = new ArrayList<Event>();
	}

	final static Stations[] stations = {
			new Stations("Running circle 400m",
					new DISCIPLINES[] { DISCIPLINES.Running_Long_3000,
							DISCIPLINES.Running_Middle_800, DISCIPLINES.Running_Middle_1500,
							DISCIPLINES.Running_Sprint_200 }),
			new Stations("Sprint Line 60m",
					new DISCIPLINES[] { DISCIPLINES.Running_Sprint_60, DISCIPLINES.Running_Hurdles_60 }),
			new Stations("Long/Triple Jump 1",
					new DISCIPLINES[] { DISCIPLINES.Jumping_Long, DISCIPLINES.Jumping_Triple }),
			new Stations("Long/Triple Jump 2",
					new DISCIPLINES[] { DISCIPLINES.Jumping_Long, DISCIPLINES.Jumping_Triple }),
			new Stations("High_Jump-l",
					new DISCIPLINES[] { DISCIPLINES.Jumping_High, DISCIPLINES.Jumping_Pole }),
			new Stations("High_Jump_ll",
					new DISCIPLINES[] { DISCIPLINES.Jumping_High, DISCIPLINES.Jumping_Pole }),
			new Stations("Pole Vault",
					new DISCIPLINES[] { DISCIPLINES.Jumping_Pole }),
			new Stations("Shot Throwing l",
					new DISCIPLINES[] { DISCIPLINES.Throwing_Shot }),
			new Stations("Shot Throwing ll",
					new DISCIPLINES[] { DISCIPLINES.Throwing_Shot }),
			new Stations("Award Ceremony Area",
					DISCIPLINES.values())
	};
}
