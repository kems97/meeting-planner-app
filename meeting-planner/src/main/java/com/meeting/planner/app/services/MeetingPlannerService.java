package com.meeting.planner.app.services;

import java.util.List;
import java.util.Optional;

import com.meeting.planner.app.dto.Reunion;
import com.meeting.planner.app.dto.Salle;
import com.meeting.planner.app.exceptions.MeetingPlannerTechnicalException;

public interface MeetingPlannerService {
	
	public List<Reunion> lancerSimulationPlanning(List<Reunion> reunions) throws MeetingPlannerTechnicalException;

	public Optional<Salle> rechecherMeilleureSalle(Reunion reunion) throws MeetingPlannerTechnicalException;
	
}
