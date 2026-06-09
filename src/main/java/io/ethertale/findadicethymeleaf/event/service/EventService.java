package io.ethertale.findadicethymeleaf.event.service;

import io.ethertale.findadicethymeleaf.deletedReport.model.DeletedReport;
import io.ethertale.findadicethymeleaf.deletedReport.model.DeletedReportType;
import io.ethertale.findadicethymeleaf.deletedReport.repo.DeletedReportRepo;
import io.ethertale.findadicethymeleaf.event.model.Event;
import io.ethertale.findadicethymeleaf.event.repo.EventRepo;
import io.ethertale.findadicethymeleaf.hero.model.Hero;
import io.ethertale.findadicethymeleaf.hero.repo.HeroRepo;
import io.ethertale.findadicethymeleaf.web.dto.EventCreateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class EventService {

    private final EventRepo eventRepo;
    private final HeroRepo heroRepo;

    private final DeletedReportRepo deletedReportRepo;

    @Autowired
    public EventService(EventRepo eventRepo, HeroRepo heroRepo, DeletedReportRepo deletedReportRepo) {
        this.eventRepo = eventRepo;
        this.heroRepo = heroRepo;
        this.deletedReportRepo = deletedReportRepo;
    }

    @Transactional
    public void createEvent(EventCreateDTO eventCreateDTO, Hero hero) {

        Event event = Event.builder()
                .title(eventCreateDTO.getTitle())
                .description(eventCreateDTO.getDescription())
                .location(eventCreateDTO.getLocation())
                .createdByEvent(hero)
                .createdAt(LocalDateTime.now())
                .startTime(eventCreateDTO.getStartTime())
                .interested(0)
                .going(0)
                .heroes(new HashSet<>())
                .build();

        String imageTrack = eventCreateDTO.getImage();
        if (!imageTrack.substring(0, 8).equals("https://")) {
            event.setImage(imageTrack);
        } else {
            event.setImage("https://i.ibb.co/C33tdsz7/find-a-dice.jpg");
        }

        eventRepo.save(event);
        log.info("User with ID {} created EVENT.\nEvent Title -> {}\nEvent Image URL -> {}\nEvent Description -> {}", hero.getId(), eventCreateDTO.getTitle(), eventCreateDTO.getImage(), eventCreateDTO.getDescription());
    }

    public List<Event> getAllEventsSortedByCreationDesc() {
        return eventRepo.findAll().stream().sorted(Comparator.comparing(Event::getStartTime).reversed()).toList();
    }

    public Event getSpecificGroup(UUID id) {
        return eventRepo.findById(id).orElse(null);
    }

    public List<Event> searchEvents(String query) {
        return eventRepo.findEventByTitleContainingIgnoreCase(query);
    }

    /**
     * Toggles the "Interested" state for a hero on an event.
     * Mutually exclusive with "Going" - clicking Interested removes Going.
     */
    @Transactional
    public void toggleInterested(UUID eventId, Hero hero) {
        Event event = eventRepo.findById(eventId).orElseThrow();

        boolean alreadyInterested = event.getInterestedHeroes().contains(hero);

        // Remove from Going if present (mutual exclusivity)
        if (event.getGoingHeroes().contains(hero)) {
            event.getGoingHeroes().remove(hero);
            event.setGoing(Math.max(0, event.getGoing() - 1));
        }

        if (alreadyInterested) {
            // Toggle off
            event.getInterestedHeroes().remove(hero);
            event.setInterested(Math.max(0, event.getInterested() - 1));
        } else {
            // Toggle on
            event.getInterestedHeroes().add(hero);
            event.setInterested(event.getInterested() + 1);
        }

        eventRepo.save(event);
    }

    /**
     * Toggles the "Going" state for a hero on an event.
     * Mutually exclusive with "Interested" - clicking Going removes Interested.
     */
    @Transactional
    public void toggleGoing(UUID eventId, Hero hero) {
        Event event = eventRepo.findById(eventId).orElseThrow();

        boolean alreadyGoing = event.getGoingHeroes().contains(hero);

        // Remove from Interested if present (mutual exclusivity)
        if (event.getInterestedHeroes().contains(hero)) {
            event.getInterestedHeroes().remove(hero);
            event.setInterested(Math.max(0, event.getInterested() - 1));
        }

        if (alreadyGoing) {
            // Toggle off
            event.getGoingHeroes().remove(hero);
            event.setGoing(Math.max(0, event.getGoing() - 1));
        } else {
            // Toggle on
            event.getGoingHeroes().add(hero);
            event.setGoing(event.getGoing() + 1);
        }

        eventRepo.save(event);
    }

    @Transactional
    public void deleteEvent(UUID eventId, UUID loggedUserid) {
        Optional<Event> eventById = eventRepo.getEventById(eventId);
        DeletedReport newReport = DeletedReport.builder()
                .type(DeletedReportType.EVENT)
                .objectId(eventById.get().getId())
                .title(eventById.get().getTitle())
                .content(eventById.get().getDescription())
                .deletedByUserId(eventById.get().getCreatedByEvent().getUser().getId())
                .deletedOn(LocalDateTime.now())
                .build();
        deletedReportRepo.save(newReport);

        eventRepo.deleteById(eventId);
        log.info("User with ID {} deleted EVENT with ID {}.\nEvent Title -> {}\nEvent Image URL ->{}\nEvent Description ->{}", loggedUserid, eventId, eventById.get().getTitle(), eventById.get().getImage(), eventById.get().getDescription());
    }
}