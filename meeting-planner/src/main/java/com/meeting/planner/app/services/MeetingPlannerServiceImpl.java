package com.meeting.planner.app.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.meeting.planner.app.dto.Disponibilite;
import com.meeting.planner.app.dto.Equipement;
import com.meeting.planner.app.dto.EquipementAmovible;
import com.meeting.planner.app.dto.Reunion;
import com.meeting.planner.app.dto.Salle;
import com.meeting.planner.app.enums.ModeRechercheEnum;
import com.meeting.planner.app.exceptions.MeetingPlannerTechnicalException;
import com.meeting.planner.app.utils.DonneesSimulationUtils;

import jakarta.annotation.PostConstruct;

@Service
public class MeetingPlannerServiceImpl implements MeetingPlannerService {

	List<Salle> salles;

	List<EquipementAmovible> equipementsAmovibles;
	
	@Autowired 
	MessageSource messageSource;
    
	@PostConstruct
	private void init() {
		salles = DonneesSimulationUtils.initSalles();
		equipementsAmovibles = DonneesSimulationUtils.initEquipementsAmovibles();
	}

	/**
	 * Service de lancement d'une simulation de réservation de salle pour un
	 * ensemble de réunion arrivant les unes après les autres
	 * 
	 * @throws MeetingPlannerTechnicalException
	 */
	@Override
	public List<Reunion> lancerSimulationPlanning(List<Reunion> reunions) throws MeetingPlannerTechnicalException {
		// Réinitialiser les données pour chaque simulation
		init();
		
		for (Reunion reunion : reunions) {
			Optional<Salle> meilleureSalle = rechecherMeilleureSalle(reunion);
			if (meilleureSalle.isPresent()) {
				reunion.setNomSalleAttribuee(meilleureSalle.get().getNom());
			}
		}
		return reunions;
	}

	/**
	 * Service de recherche de la meilleure salle disponible en fonction des
	 * paramètres d'une réunion (disponiblités, type et équipements requis
	 * 
	 * @throws MeetingPlannerTechnicalException
	 */
	@Override
	public Optional<Salle> rechecherMeilleureSalle(Reunion reunion) throws MeetingPlannerTechnicalException {
		Optional<Salle> optBestRoom = Optional.empty();

		try {
			List<Equipement> equipementsRequis = DonneesSimulationUtils.getEquipementsRequis(reunion.getType());

			// Recherche simple
			optBestRoom = rechercherSalle(ModeRechercheEnum.SIMPLE, reunion, salles, equipementsRequis);

			if (optBestRoom.isPresent()) {
				reserverSalle(optBestRoom.get(), reunion.getHeureDebut());
			} else {
				// Recherche avancée incluant les équipments amovibles
				optBestRoom = rechercherSalle(ModeRechercheEnum.AVANCEE, reunion, salles, equipementsRequis);
				if (optBestRoom.isPresent()) {
					reserverSalle(optBestRoom.get(), reunion.getHeureDebut());
					reserverEquipementAmovible(equipementsAmovibles, optBestRoom.get(), reunion);
				}
			}

		} catch (Exception e) {
			throw new MeetingPlannerTechnicalException(messageSource.getMessage("api.technical.error.planner", null, Locale.getDefault()));
		}
		return optBestRoom;
	}

	/**
	 * 
	 * @param reunion
	 * @param salles
	 * @param equipementsRequis
	 * @param equipementsAmovibles
	 * @return
	 */
	public Optional<Salle> rechercherSalle(ModeRechercheEnum modeRechercheEnum, Reunion reunion, List<Salle> salles,
			List<Equipement> equipementsRequis) {
		Optional<Salle> salleIdeale = Optional.empty();

		// Filter en fonction des disponibiltés et de la capacité
		List<Salle> sallesDisponibles = salles.stream().filter(salle -> salle.isAvailableForReservation(reunion))
				.filter(salle -> salle.isCapacitySufficient(reunion)).toList();

		// Filter en fonction des équipements requis
		List<Salle> sallesEventuelles = identifierSallesEventuelles(modeRechercheEnum, reunion, sallesDisponibles,
				equipementsRequis);

		if (!sallesEventuelles.isEmpty()) {
			salleIdeale = Optional.ofNullable(identifierSalleIdeale(sallesEventuelles));
			// Vider la liste des équpements amovibles ajoutés aux salles
			viderEquimentsAmoviblesSalles(sallesEventuelles);
		}

		return salleIdeale;
	}

	/**
	 * 
	 * @param reunion
	 * @param sallesDisponibles
	 * @param equipementsRequis
	 * @param equipementsAmovibles
	 * @return
	 */
	private List<Salle> identifierSallesEventuelles(ModeRechercheEnum modeRechercheEnum, Reunion reunion,
			List<Salle> sallesDisponibles, List<Equipement> equipementsRequis) {

		List<Salle> sallesEventuelles = new ArrayList<>();

		if (ModeRechercheEnum.SIMPLE.equals(modeRechercheEnum)) {
			rechercheSimpleSallesEventuelles(sallesDisponibles, equipementsRequis, sallesEventuelles);
		}

		if (ModeRechercheEnum.AVANCEE.equals(modeRechercheEnum)) {
			rechercheAvanceeSallesEventuelles(reunion, sallesDisponibles, equipementsRequis, equipementsAmovibles,
					sallesEventuelles);
		}

		return sallesEventuelles;
	}

	/**
	 * 
	 * @param sallesDisponibles
	 * @param equipementsRequis
	 * @param sallesEventuelles
	 */
	private void rechercheSimpleSallesEventuelles(List<Salle> sallesDisponibles, List<Equipement> equipementsRequis,
			List<Salle> sallesEventuelles) {
		for (Salle salle : sallesDisponibles) {
			if (salle.getEquipements().containsAll(equipementsRequis)) {
				sallesEventuelles.add(salle);
			}
		}
	}

	/**
	 * 
	 * @param reunion
	 * @param sallesDisponibles
	 * @param equipementsRequis
	 * @param equipementsAmovibles
	 * @param sallesEventuelles
	 */
	private void rechercheAvanceeSallesEventuelles(Reunion reunion, List<Salle> sallesDisponibles,
			List<Equipement> equipementsRequis, List<EquipementAmovible> equipementsAmovibles,
			List<Salle> sallesEventuelles) {

		// Rendre la liste muable
		sallesDisponibles = new ArrayList<>(sallesDisponibles);

		// Trier ces salles dans l'odre décroissant de de nombre d'éléments requis
		// trouvés pour traiter en premier celle qui contiennent déjà le max
		// d'équipements
		Collections.sort(sallesDisponibles,
				Comparator.comparingInt(
						salle -> compterEquipementsRequisPresents(((Salle) salle).getEquipements(), equipementsRequis))
						.reversed());

		// Effectuer des combinaisosn en ajoutant les équipements amovibles nécessaires
		// à ces salles
		// en vérifiant la disponibilités des équipements amovibles nécessaires en
		// fonction du créneau
		for (Salle salle : sallesDisponibles) {
			// Identifier les équipements amovibles à ajouter dans la salle
			List<Equipement> equipementsToAdd = equipementsRequis.stream().filter(e -> !salle.getEquipements().contains(e)).toList();

			for (Equipement equipementToAdd : equipementsToAdd) {
				// Identifier par leur nom dans la listes des équipements amovibles, la liste
				// des équipement à ajouter
				List<EquipementAmovible> listes = equipementsAmovibles.stream().filter(e -> e.getNom().equals(equipementToAdd.getNom())).toList();
				
				// Vérifier si parmi ces équipements y en a qui sont disponible pour ce créneau
				// de réunion
				for (EquipementAmovible equipementAmovible : listes) {
					if (equipementAmovible.isAvailableForReservation(equipementAmovible, reunion)) {
						// Ajouter le premier équipement trouvé disponible
						salle.addEquipementsAmovible(equipementAmovible);
						break;
					}
				}
			}
			sallesEventuelles.add(salle);
		}
	}

	/**
	 * 
	 * @param sallesEventuelles
	 * @return
	 */
	private Salle identifierSalleIdeale(List<Salle> sallesEventuelles) {
		// Choisir la salle ayant la plus petite capacité et ayant le moins
		// d'équipements amovibles ajoutés
		// et ayant le moins d'éléments non nécessaire présents dans la salle

		// Le scrore est égal à la capacité plus le nombres d'élements amovibles ajoutés
		// Calcul du score pour chaque salle et stockage des scores dans une liste
		// temporaire
		List<Integer> scores = new ArrayList<>();
		for (Salle salle : sallesEventuelles) {
			int score = salle.getCapaciteMax() + salle.getEquipements().size();
			scores.add(score);
		}

		int indexMinScore = scores.indexOf(Collections.min(scores));

		return sallesEventuelles.get(indexMinScore);
	}

	/**
	 * 
	 * @param equipementsSalle
	 * @param equipementsRequis
	 * @return
	 */
	// Méthode pour compter le nombre d'équipements requis présents dans une liste
	// d'équipements d'une salle
	private int compterEquipementsRequisPresents(List<Equipement> equipementsSalle, List<Equipement> equipementsRequis) {
		int count = 0;
		for (Equipement equipement : equipementsSalle) {
			if (equipementsRequis.contains(equipement)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Réserver une salle pour un créneau horaire de réunion
	 * @param salle
	 * @param heureDebut
	 */
	private void reserverSalle(Salle salle, int heureDebut) {
		List<Disponibilite> disponibilites = salle.getDisponibilites();			
		int i = 0;
		while (i < disponibilites.size() - 1) {
			if (disponibilites.get(i).getDateDebut() == heureDebut) {
		        disponibilites.remove(i); // Réserver le créneau
		        disponibilites.remove(i); // Rendre le créneau suivant indisponible pour nettoyage
		    } else {
		        i++; // Incrémenter l'indice uniquement si aucun élément n'est supprimé (pour éviter les exceptions)
		    }
		}
	}

	/**
	 * 
	 * @param equipementsAmovibleDispo
	 * @param bestRoom
	 * @param reunion
	 */
	private void reserverEquipementAmovible(List<EquipementAmovible> equipementsAmovibleDispo, Salle bestRoom,
			Reunion reunion) {
		// Indiquer les équipements amovibles qui seront indisponibles sur certains
		// créneaux car déjà utilisé dans d'autres salles à ce moment là
		List<EquipementAmovible> equipementsAmovibleSalle = bestRoom.getEquipementsAmovible();
		for (EquipementAmovible equipementAmovibleSalle : equipementsAmovibleSalle) {
			// Identifier par leur nom dans la listes des équipements amovibles, la liste
			// des équipement à ajouter
			List<EquipementAmovible> listes = equipementsAmovibleDispo.stream()
					.filter(e -> e.getNom().equals(equipementAmovibleSalle.getNom())).toList();
			// Vérifier si parmi ces équipements y en a qui sont disponible pour ce créneau
			// de réunion
			for (EquipementAmovible equipementAmovible : listes) {
				if (equipementAmovible.isAvailableForReservation(equipementAmovible, reunion)) {
					// Ajouter le premier équipement trouvé disponible
					equipementAmovible.deleteDisponibiltes(
							new Disponibilite(reunion.getHeureDebut(), reunion.getHeureFin()));
					break;
				}
			}
		}
	}

	/**
	 * 
	 * @param sallesEventuelles
	 */
	private void viderEquimentsAmoviblesSalles(List<Salle> sallesEventuelles) {
		for (int i = 0; i < sallesEventuelles.size(); i++) {
			sallesEventuelles.get(i).deleteAllEquipementsAmovible();
		}
	}

}
