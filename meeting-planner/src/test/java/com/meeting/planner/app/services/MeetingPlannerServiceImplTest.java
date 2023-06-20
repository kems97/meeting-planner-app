package com.meeting.planner.app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import com.meeting.planner.app.dto.Reunion;
import com.meeting.planner.app.dto.Salle;
import com.meeting.planner.app.enums.TypeReunionEnum;
import com.meeting.planner.app.exceptions.MeetingPlannerTechnicalException;
import com.meeting.planner.app.utils.DonneesSimulationUtils;

@ExtendWith(MockitoExtension.class)
class MeetingPlannerServiceImplTest {
	
	@Mock
    private MessageSource messageSource;

    private MeetingPlannerServiceImpl meetingPlannerService;

    @BeforeEach
    void setup() {
    	MockitoAnnotations.openMocks(this);
        meetingPlannerService = new MeetingPlannerServiceImpl();
        meetingPlannerService.messageSource = messageSource;

        meetingPlannerService.salles = DonneesSimulationUtils.initSalles();
        meetingPlannerService.equipementsAmovibles = DonneesSimulationUtils.initEquipementsAmovibles();
    }
	
	@Test
    void testRechecherMeilleureSalleSuccessful() throws MeetingPlannerTechnicalException {
        Reunion reunion = new Reunion();
        reunion.setNom("Réunion 1");
        reunion.setType(TypeReunionEnum.VC);
        reunion.setNbrPersonnes(8);
        reunion.setHeureDebut(9);
        reunion.setHeureFin(10);

        Optional<Salle> result = meetingPlannerService.rechecherMeilleureSalle(reunion);

        assertTrue(result.isPresent());
        assertEquals("E3001", result.get().getNom());
    }
	
	@Test
    void testRechecherMeilleureSalleNotFound() throws MeetingPlannerTechnicalException {
        Reunion reunion = new Reunion();
        reunion.setNom("Réunion 21");
        reunion.setType(TypeReunionEnum.VC);
        reunion.setNbrPersonnes(30);
        reunion.setHeureDebut(22);
        reunion.setHeureFin(21);

        Optional<Salle> result = meetingPlannerService.rechecherMeilleureSalle(reunion);

        assertTrue(result.isEmpty());
    }

}