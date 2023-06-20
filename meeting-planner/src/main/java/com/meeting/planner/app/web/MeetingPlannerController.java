package com.meeting.planner.app.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.meeting.planner.app.dto.Reunion;
import com.meeting.planner.app.dto.Salle;
import com.meeting.planner.app.exceptions.MeetingPlannerTechnicalException;
import com.meeting.planner.app.services.MeetingPlannerService;
import com.meeting.planner.app.utils.DonneesSimulationUtils;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/planner")
@ResponseBody
public class MeetingPlannerController {

	@Autowired
	MeetingPlannerService meetingPlannerService;

	/**
	 * Lancer une simulation de planning de réunion
	 * 
	 * @return
	 * @throws MeetingPlannerTechnicalException
	 */
	@GetMapping("/simulation")
	@Operation(summary = "Lancer une simulation de planning de réunion", description = "Cette opération permet d'obtenir une liste de réunion avec des salles attribuées pour chaque.")
	public List<Reunion> lancerSimulationPlanning() throws MeetingPlannerTechnicalException {
		List<Reunion> reunions = DonneesSimulationUtils.getReunionsSimulationData();
		return meetingPlannerService.lancerSimulationPlanning(reunions);
	}

	/**
	 * Rechercher la meilleure salle pour une réunion
	 * 
	 * @param reunion
	 * @return
	 * @throws MeetingPlannerTechnicalException
	 */
	@PostMapping("/bestroom")
	@Operation(summary = "Rechercher la meilleur salle pour une réunion", description = "Cette opération permet de rechercher la meilleur salle en fonction du nombre de personne, du type, des ressources disponibles et du panning pour une réuinon. La variable nomSalleAttribuee ne sert que pour les paramètres de sorties en cas de simulation. Il n'est pas nécessaire de la remplir en entrée")
	public ResponseEntity<String> rechecherMeilleureSalle(@RequestBody Reunion reunion)
			throws MeetingPlannerTechnicalException {
		Optional<Salle> bestRoom = meetingPlannerService.rechecherMeilleureSalle(reunion);

		if (bestRoom.isPresent()) {
			return ResponseEntity.ok(bestRoom.get().getNom());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune salle disponible pour cette reunion");
		}
	}

}
