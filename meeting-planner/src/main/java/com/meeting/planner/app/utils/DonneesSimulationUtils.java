package com.meeting.planner.app.utils;

import java.util.ArrayList;
import java.util.List;

import com.meeting.planner.app.dto.Disponibilite;
import com.meeting.planner.app.dto.Equipement;
import com.meeting.planner.app.dto.EquipementAmovible;
import com.meeting.planner.app.dto.Reunion;
import com.meeting.planner.app.dto.Salle;
import com.meeting.planner.app.enums.TypeReunionEnum;

public class DonneesSimulationUtils {
		
	// Recommendation de Sonar pour les classes utils aynat des méthodes static
	private DonneesSimulationUtils() {
		throw new IllegalStateException("Utility class");
	}

	private static final String ECRAN = "Ecran";
	private static final String PIEUVRE = "Pieuvre";
	private static final String WEBCAM = "Webcam";
	private static final String TABLEAU = "Tableau";
	private static final String NEANT = "Néant";
	
	public static List<Reunion> getReunionsSimulationData() {
		List<Reunion> reunions = new ArrayList<>();

		reunions.add(new Reunion("Reunion 1", 9, 10, TypeReunionEnum.VC, 8));
		reunions.add(new Reunion("Reunion 2", 9, 10, TypeReunionEnum.VC, 6));
		reunions.add(new Reunion("Reunion 3", 11, 12, TypeReunionEnum.RC, 4));
		reunions.add(new Reunion("Reunion 4", 11, 12, TypeReunionEnum.RS, 2));
		reunions.add(new Reunion("Reunion 5", 11, 12, TypeReunionEnum.SPEC, 9));
		reunions.add(new Reunion("Reunion 6", 9, 10, TypeReunionEnum.RC, 7));
		reunions.add(new Reunion("Reunion 7", 8, 10, TypeReunionEnum.VC, 9));
		reunions.add(new Reunion("Reunion 8", 8, 9, TypeReunionEnum.SPEC, 10));
		reunions.add(new Reunion("Reunion 9", 9, 10, TypeReunionEnum.SPEC, 5));
		reunions.add(new Reunion("Reunion 10", 9, 10, TypeReunionEnum.RS, 4));
		reunions.add(new Reunion("Reunion 11", 9, 10, TypeReunionEnum.RC, 8));
		reunions.add(new Reunion("Reunion 12", 11, 12, TypeReunionEnum.VC, 12));
		reunions.add(new Reunion("Reunion 13", 11, 12, TypeReunionEnum.SPEC, 5));
		reunions.add(new Reunion("Reunion 14", 8, 9, TypeReunionEnum.VC, 3));
		reunions.add(new Reunion("Reunion 15", 8, 9, TypeReunionEnum.SPEC, 2));
		reunions.add(new Reunion("Reunion 16", 8, 9, TypeReunionEnum.VC, 12));
		reunions.add(new Reunion("Reunion 17", 10, 11, TypeReunionEnum.VC, 6));
		reunions.add(new Reunion("Reunion 18", 11, 12, TypeReunionEnum.RS, 2));
		reunions.add(new Reunion("Reunion 19", 9, 10, TypeReunionEnum.RS, 4));
		reunions.add(new Reunion("Reunion 20", 9, 10, TypeReunionEnum.RC, 7));

		return reunions;
	}
	
	public static List<Salle> initSalles() {
		List<Salle> salles = new ArrayList<>();
		
		salles.add(new Salle("E1001", 23, List.of(new Equipement(NEANT))));
		salles.add(new Salle("E1002", 10, List.of(new Equipement(ECRAN))));
		salles.add(new Salle("E1003", 8, List.of(new Equipement(PIEUVRE))));
		salles.add(new Salle("E1004", 4, List.of(new Equipement(TABLEAU))));
		
		salles.add(new Salle("E2001", 4, List.of(new Equipement(NEANT))));
		salles.add(new Salle("E2002", 15, List.of(new Equipement(ECRAN), new Equipement(WEBCAM))));
		salles.add(new Salle("E2003", 7, List.of(new Equipement(NEANT))));
		salles.add(new Salle("E2004", 9, List.of(new Equipement(TABLEAU))));
		
		salles.add(new Salle("E3001", 13, List.of(new Equipement(ECRAN), new Equipement(WEBCAM), new Equipement(PIEUVRE))));
		salles.add(new Salle("E3002", 8, List.of(new Equipement(NEANT))));
		salles.add(new Salle("E3003", 9, List.of(new Equipement(ECRAN), new Equipement(PIEUVRE))));
		salles.add(new Salle("E3004", 4, List.of(new Equipement(NEANT))));

		return salles;
	}
	
	public static List<Equipement> getEquipementsRequis(TypeReunionEnum type) {		
		switch (type) {
        case VC:
            return List.of(new Equipement(ECRAN),new Equipement(PIEUVRE), new Equipement(WEBCAM));
        case SPEC:
            return List.of(new Equipement(TABLEAU));
        case RS:
            return List.of();
        case RC:
            return List.of(new Equipement(TABLEAU), new Equipement(ECRAN), new Equipement(PIEUVRE));
        default:
            return List.of();
    	}
	}
	
	public static List<EquipementAmovible> initEquipementsAmovibles() {
		List<EquipementAmovible> equipementsAmovibles = new ArrayList<>();
		
		equipementsAmovibles.add(new EquipementAmovible(PIEUVRE));
		equipementsAmovibles.add(new EquipementAmovible(PIEUVRE));
		equipementsAmovibles.add(new EquipementAmovible(PIEUVRE));
		equipementsAmovibles.add(new EquipementAmovible(PIEUVRE));
		
		equipementsAmovibles.add(new EquipementAmovible(ECRAN));
		equipementsAmovibles.add(new EquipementAmovible(ECRAN));
		equipementsAmovibles.add(new EquipementAmovible(ECRAN));
		equipementsAmovibles.add(new EquipementAmovible(ECRAN));
		equipementsAmovibles.add(new EquipementAmovible(ECRAN));
			
		equipementsAmovibles.add(new EquipementAmovible(WEBCAM));
		equipementsAmovibles.add(new EquipementAmovible(WEBCAM));
		equipementsAmovibles.add(new EquipementAmovible(WEBCAM));
		equipementsAmovibles.add(new EquipementAmovible(WEBCAM));
		
		equipementsAmovibles.add(new EquipementAmovible(TABLEAU));
		equipementsAmovibles.add(new EquipementAmovible(TABLEAU));
		
		return equipementsAmovibles;
	}
	
	public static List<Disponibilite> initDisponibililes(List<Disponibilite> disponibilites) {	
		disponibilites.add(new Disponibilite(8, 9));
		disponibilites.add(new Disponibilite(9, 10));
		disponibilites.add(new Disponibilite(10, 11));
		disponibilites.add(new Disponibilite(11, 12));
		disponibilites.add(new Disponibilite(12, 13));
		disponibilites.add(new Disponibilite(13, 14));
		disponibilites.add(new Disponibilite(14, 15));
		disponibilites.add(new Disponibilite(15, 16));
		disponibilites.add(new Disponibilite(16, 17));
		disponibilites.add(new Disponibilite(17, 18));
		disponibilites.add(new Disponibilite(18, 19));
		disponibilites.add(new Disponibilite(19, 20));
		
		return new ArrayList<>(disponibilites);
	}
	
}
