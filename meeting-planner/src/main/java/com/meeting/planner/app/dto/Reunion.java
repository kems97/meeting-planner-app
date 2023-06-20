package com.meeting.planner.app.dto;

import com.meeting.planner.app.enums.TypeReunionEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reunion {
	
	String nom;

	TypeReunionEnum type;

	int heureDebut;
	
	int heureFin;

	int nbrPersonnes;
	
	String nomSalleAttribuee;
	
	public Reunion() {
	}

	public Reunion(String nom, int heureDebut, int heureFin, TypeReunionEnum type, int nbrPersonnes) {
		this.nom = nom;
		this.heureDebut = heureDebut;
		this.heureFin = heureFin;
		this.type = type;
		this.nbrPersonnes = nbrPersonnes;
	}

}
