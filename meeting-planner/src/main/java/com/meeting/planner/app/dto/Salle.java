package com.meeting.planner.app.dto;

import java.util.ArrayList;
import java.util.List;

import com.meeting.planner.app.enums.TypeReunionEnum;
import com.meeting.planner.app.utils.DonneesSimulationUtils;

import lombok.Getter;

@Getter
public class Salle {

	String nom;

	int capaciteMax;

	List<Equipement> equipements = new ArrayList<>();
	
	List<EquipementAmovible> equipementsAmovible = new ArrayList<>();

	List<Disponibilite> disponibilites = new ArrayList<>();

	public Salle() {
	}

	public Salle(String nom, int capaciteMax, List<Equipement> equipements) {
		this.nom = nom;
		this.capaciteMax = capaciteMax;
		this.equipements = equipements;
		this.disponibilites = DonneesSimulationUtils.initDisponibililes(disponibilites);
	}

	public void addEquipementsAmovible(EquipementAmovible equipementAmovible) {
		this.equipementsAmovible.add(equipementAmovible);
	}
	
	public void deleteAllEquipementsAmovible() {
		this.equipementsAmovible.removeAll(equipementsAmovible);
	}
	
	public boolean isAvailableForReservation(Reunion reunion) {
		// Vérifier la disponibilité de la salle en fonction des créneaux disponibles
		if (!this.getDisponibilites().isEmpty()) {
			for (Disponibilite disponibilite : this.getDisponibilites()) {
				if (disponibilite.getDateDebut() == reunion.getHeureDebut()) {
					return true;
				}
			}
		}
		return false;
	}

	// Vérifier la disponibilité de la salle en fonction capacité d'acceuil liée au covid (70%)
	public boolean isCapacitySufficient(Reunion reunion) {
		
		// Pour les salles de types RS il faut toujours prévoir au moins 3 personnes
		if(TypeReunionEnum.RS.equals(reunion.getType()) && reunion.getNbrPersonnes() < 3) {
			return (this.getCapaciteMax() * 70) / 100 >= 3;
		}
		return (this.getCapaciteMax() * 70) / 100 >= reunion.getNbrPersonnes();
	}
	
}
