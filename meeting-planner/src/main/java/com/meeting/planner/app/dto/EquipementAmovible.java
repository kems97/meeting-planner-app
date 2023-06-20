package com.meeting.planner.app.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.meeting.planner.app.utils.DonneesSimulationUtils;

import lombok.Getter;

@Getter
public class EquipementAmovible extends Equipement {
	
	List<Disponibilite> disponibilites = new ArrayList<>();
	
	public EquipementAmovible() {
	}

	public EquipementAmovible(String nom) {
		this.nom = nom;
		this.disponibilites = DonneesSimulationUtils.initDisponibililes(disponibilites);
	}
	
	public boolean isAvailableForReservation(Reunion reunion) {
		// Vérifier la disponibilité d'un equipement amovible en fonction des créneaux disponibles
		if (!this.getDisponibilites().isEmpty()) {
			for (Disponibilite disponibilite : this.getDisponibilites()) {
				if (disponibilite.getDateDebut() == reunion.getHeureDebut()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void deleteDisponibilites(Disponibilite disponibilite) {
		Iterator<Disponibilite> iterator = disponibilites.iterator();
        while (iterator.hasNext()) {
        	Disponibilite d = iterator.next();
            if(d.equals(disponibilite)) {
                iterator.remove();
            }
        }
	}

}
