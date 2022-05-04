package com.trackandfield;

class Stations {
	int id;
	String name;
	int slots;
	boolean empty;

	public Stations(int id, String name, int slots, boolean empty) {
		this.id = id;
		this.name = name;
		this.slots = slots;
		this.empty = empty;
	}

	final Stations Running_Circle = new Stations(1, "Running circle 400m", 6, true);
	final Stations Sprint_Line = new Stations(2, "Sprint Line (60m, 8 tracks)", 8, true);
	final Stations Long_Triple_Jump_l = new Stations(3, "Long/Triple Jump-l ", 1, true);
	final Stations Long_Triple_Jump_ll = new Stations(4, "Long/Triple Jump-ll", 1, true);
	final Stations High_Jump_I = new Stations(5, "High_Jump-l", 1, true);
	final Stations High_Jump_II = new Stations(6, "High_Jump_ll", 1, true);
	final Stations Pole_Vault = new Stations(7, "Pole Vault", 1, true);
	final Stations Shot_Throwing_I = new Stations(8, "Shot Throwing l", 1, true);
	final Stations Shot_Throwing_II = new Stations(9, "Shot_Throwing ll", 1, true);
}
