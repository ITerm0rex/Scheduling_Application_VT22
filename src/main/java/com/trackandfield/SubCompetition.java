package com.trackandfield;

import java.util.ArrayList;
import java.util.LinkedList;
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

	/**
	 * {@summary Generete SubCompetition}
	 * 
	 * @return List<SubCompetition>
	 */
	public static List<SubCompetition> generateSubCompetition(final List<Groups> groups) {
		final List<SubCompetition> subComps = new ArrayList<SubCompetition>();

		var id = 0;
		for (final var group : groups) {
			// put entire group into a buffer
			final var competitors = new LinkedList<Athletes>(group.athletes);

			double number_of_slots = group.discipline.slots; // slots from group discipline
			double number_of_competitors = competitors.size(); // Number of competitors in each group
			// number of qualifiers for each group
			var number_of_subSubComp = Math.ceil(number_of_competitors / number_of_slots);
			// max number of athletes for each qualifier
			var max_number_of_Athletes_in_subCom = Math.ceil(number_of_competitors / number_of_subSubComp);

			var isFinal = number_of_subSubComp == 1;
			// Case 1 example: 2 competitors but less slots. Create final and add both
			// athletes
			if (number_of_competitors == 2 && number_of_slots < 2) {
				number_of_subSubComp = 1;
				isFinal = true;
			}
			// Case 2 example: 20 athletes, 8 slots -> creates 3 qualifiers with 7, 7, 6
			// competitors each
			else if (number_of_competitors > number_of_slots) {
				isFinal = false;
			}
			// Case 3 example: 5 competitors, 8 slots -> create final and add athletes
			else if (number_of_competitors > 1) {
				isFinal = true;
			}

			for (int i = 0; i < number_of_subSubComp; i++) {
				var new_subComp = new SubCompetition(
						++id,
						isFinal,
						group,
						new LinkedList<Athletes>());
				subComps.add(new_subComp);
				// Adding athletes into qualifiers
				for (int j = 0; j < max_number_of_Athletes_in_subCom; j++) {
					// returns first athlete and removes it from the list
					var comp = competitors.pollFirst();
					if (comp == null)
						break;

					new_subComp.athletes.add(comp);
				}
			}
			if (!isFinal) {
				// Only adding a final with all athletes since we can't foresee
				// qualifier-winners
				var new_final = new SubCompetition(
						++id,
						true,
						group,
						group.athletes);

				subComps.add(new_final);
			}
		}
		return subComps;
	}

}
