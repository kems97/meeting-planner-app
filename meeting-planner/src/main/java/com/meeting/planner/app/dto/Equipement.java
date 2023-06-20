package com.meeting.planner.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Equipement {
	
	protected String nom;
	
	public Equipement() {
	}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Equipement autreEquipement = (Equipement) obj;
        return nom.equals(autreEquipement.nom);
    }
    
    @Override
    public int hashCode() {
        return nom.hashCode();
    }
	
}
