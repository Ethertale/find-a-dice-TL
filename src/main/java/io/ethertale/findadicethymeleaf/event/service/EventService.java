package io.ethertale.findadicethymeleaf.event.service;

import io.ethertale.findadicethymeleaf.event.model.Event;
import io.ethertale.findadicethymeleaf.event.repo.EventRepo;
import io.ethertale.findadicethymeleaf.hero.model.Hero;
import io.ethertale.findadicethymeleaf.hero.repo.HeroRepo;
import io.ethertale.findadicethymeleaf.web.dto.EventCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {

    private final EventRepo eventRepo;
    private final HeroRepo heroRepo;

    @Autowired
    public EventService(EventRepo eventRepo, HeroRepo heroRepo) {
        this.eventRepo = eventRepo;
        this.heroRepo = heroRepo;
    }

    public void createEvent(EventCreateDTO eventCreateDTO, Hero hero) {
        Event event = Event.builder()
                .title(eventCreateDTO.getTitle())
                .description(eventCreateDTO.getDescription())
                .location(eventCreateDTO.getLocation())
                .createdBy(hero)
                .createdAt(LocalDateTime.now())
                .startTime(eventCreateDTO.getStartTime())
                .image(eventCreateDTO.getImage())
                .interested(0)
                .going(0)
                .heroes(new HashSet<>())
                .build();

        eventRepo.save(event);
    }

    public List<Event> getAllEventsSortedByCreationDesc() {
        return eventRepo.findAll().stream().sorted(Comparator.comparing(Event::getCreatedAt).reversed()).toList();
    }

    public Event getSpecificGroup(UUID id) {
        return eventRepo.findById(id).orElse(null);
    }
}
