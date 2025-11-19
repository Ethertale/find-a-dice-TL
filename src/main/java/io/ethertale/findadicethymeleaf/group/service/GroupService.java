package io.ethertale.findadicethymeleaf.group.service;

import io.ethertale.findadicethymeleaf.exceptions.GroupDoesNotExistException;
import io.ethertale.findadicethymeleaf.exceptions.GroupHeroAlreadyInGroupException;
import io.ethertale.findadicethymeleaf.group.model.Group;
import io.ethertale.findadicethymeleaf.group.repo.GroupRepo;
import io.ethertale.findadicethymeleaf.hero.model.Hero;
import io.ethertale.findadicethymeleaf.hero.repo.HeroRepo;
import io.ethertale.findadicethymeleaf.web.dto.GroupCreateDTO;
import io.ethertale.findadicethymeleaf.web.dto.GroupPostDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class GroupService {

    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);
    private final GroupRepo groupRepo;
    private final HeroRepo heroRepo;

    @Autowired
    public GroupService(GroupRepo groupRepo, HeroRepo heroRepo) {
        this.groupRepo = groupRepo;
        this.heroRepo = heroRepo;
    }

    public List<Group> getAllGroupsSortedByCreationDesc() {
        return groupRepo.findAll().stream().sorted(Comparator.comparing(Group::getCreatedAt).reversed()).toList();
    }

    public Group getSpecificGroup(UUID id){
        return groupRepo.findById(id).orElse(null);
    }

    public void addHeroToGroup(UUID loggedUserHeroId, UUID groupId) {
        Group group = groupRepo.findById(groupId).orElse(null);
        Hero hero = heroRepo.findById(loggedUserHeroId).orElse(null);

        if (group != null && hero != null) {
            if (!group.getHeroes().contains(hero) && !hero.getGroups().contains(group)) {
                group.getHeroes().add(hero);
                hero.getGroups().add(group);
                groupRepo.save(group);
                heroRepo.save(hero);
            } else {
                throw new GroupHeroAlreadyInGroupException();
            }
        } else {
            throw new GroupDoesNotExistException();
        }

    }

    public void removeHeroFromGroup(UUID loggedUserHeroId, UUID groupId) {
        Group group = groupRepo.findById(groupId).orElse(null);
        Hero hero = heroRepo.findById(loggedUserHeroId).orElse(null);

        if (group != null && hero != null) {
            if (group.getHeroes().contains(hero) && hero.getGroups().contains(group)){
                group.getHeroes().remove(hero);
                hero.getGroups().remove(group);
                groupRepo.save(group);
                heroRepo.save(hero);
            } else {
                throw new GroupDoesNotExistException();
            }
        }
    }

    public void createGroup(GroupCreateDTO groupDTO, Hero hero) {
        Group group = Group.builder()
                .name(groupDTO.getName())
                .description(groupDTO.getDescription())
                .imageUrl(groupDTO.getImageUrl())
                .heroes(new HashSet<>())
                .groupPosts(new HashSet<>())
                .createdBy(hero)
                .createdAt(LocalDateTime.now())
                .build();

        groupRepo.save(group);
    }

    public void createPost(GroupPostDTO postDTO, Hero hero, UUID groupId) {

    }
}
