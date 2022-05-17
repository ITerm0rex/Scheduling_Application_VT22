package com.trackandfield;

import java.util.List;

class SubCompetition {
	int id;
	boolean isFinal;
	Groups group;
	List<Athletes> athletes;

	public SubCompetition(int id, boolean isFinal, Groups group, List<Athletes> athletes) {
		this.id = id;
		this.isFinal = isFinal;
		this.group = group;
		this.athletes = athletes;
	}

	/**
	 * {@summary Check for overlap}
	 */
	public Boolean containsSameAthletes(SubCompetition subComp) {
		for (var atl : subComp.athletes) {
			if (this.athletes.contains(atl))
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "SubCompetition ["
				+ "id=" + id
				+ ", group.id=" + group.id
				+ ", durationMinutes=" + group.discipline.durationMinutes
				+ ", athletes#=" + athletes.size()
				+ ", isFinal=" + isFinal
				+ ", group.discipline=" + group.discipline
				+ "]";
	}
}
