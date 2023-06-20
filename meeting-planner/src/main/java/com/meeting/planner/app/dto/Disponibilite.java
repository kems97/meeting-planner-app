package com.meeting.planner.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Disponibilite {
    	
    private int dateDebut;

    private int dateFin;
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Disponibilite autreDisponibilte = (Disponibilite) obj;

        return dateDebut == autreDisponibilte.dateDebut && dateFin == autreDisponibilte.dateFin;
    }

    @Override
    public int hashCode() {
        int result = dateDebut;
        result = 31 * result + dateFin;
        return result;
    }
   
}
