package com.meeting.planner.app.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.meeting.planner.app.dto.Equipement;
import com.meeting.planner.app.dto.Reunion;
import com.meeting.planner.app.dto.Salle;
import com.meeting.planner.app.enums.TypeReunionEnum;
import com.meeting.planner.app.exceptions.MeetingPlannerTechnicalException;
import com.meeting.planner.app.services.MeetingPlannerService;

class MeetingPlannerControllerTest {
	
	@Mock
	MeetingPlannerService meetingPlannerService;
	
	@InjectMocks
	MeetingPlannerController controller;
	
	 @BeforeEach
	 void setup() {
	    MockitoAnnotations.openMocks(this);
	 }

    @Test
    void testRechecherMeilleureSalleSuccessful() throws MeetingPlannerTechnicalException {
       
        Reunion reunion = new Reunion(); 
        reunion.setNom("Réunion 1");
        reunion.setType(TypeReunionEnum.VC);
        reunion.setNbrPersonnes(8);
        reunion.setHeureDebut(9);
        reunion.setHeureFin(10);
        
        Optional<Salle> bestRoom = Optional.of(new Salle("E3001", 13, List.of(new Equipement("Ecran"), new Equipement("Webcam"), new Equipement("Pieuvre"))));
        when(meetingPlannerService.rechecherMeilleureSalle(reunion)).thenReturn(bestRoom);

        ResponseEntity<String> response = controller.rechecherMeilleureSalle(reunion);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("E3001", response.getBody());
    }

    @Test
    void testRechecherMeilleureSalleNotFound() throws MeetingPlannerTechnicalException {
        Reunion reunion = new Reunion(); 
        reunion.setNom("Réunion 21");
        reunion.setType(TypeReunionEnum.VC);
        reunion.setNbrPersonnes(40);
        reunion.setHeureDebut(21);
        reunion.setHeureFin(22);
        
        Optional<Salle> bestRoom = Optional.empty();
        when(meetingPlannerService.rechecherMeilleureSalle(reunion)).thenReturn(bestRoom);

        ResponseEntity<String> response = controller.rechecherMeilleureSalle(reunion);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Aucune salle disponible pour cette reunion", response.getBody());
    }
}
