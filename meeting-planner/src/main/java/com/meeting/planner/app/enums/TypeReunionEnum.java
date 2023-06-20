package com.meeting.planner.app.enums;

public enum TypeReunionEnum {
	
	VC("Les visioconférences"),
	SPEC("Les séances de partage et d'études de cas"),
	RS("Les réunions simples"),
	RC("Les réunions couplées");
	
	private final String libelle;

	private TypeReunionEnum(String libelle) {
		this.libelle = libelle;
	}

	public String getLibelle() {
		return libelle;
	} 

}
