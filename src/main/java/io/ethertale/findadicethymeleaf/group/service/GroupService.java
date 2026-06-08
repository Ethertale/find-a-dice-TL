package io.ethertale.findadicethymeleaf.group.service;

import io.ethertale.findadicethymeleaf.deletedReport.model.DeletedReport;
import io.ethertale.findadicethymeleaf.deletedReport.model.DeletedReportType;
import io.ethertale.findadicethymeleaf.deletedReport.repo.DeletedReportRepo;
import io.ethertale.findadicethymeleaf.exceptions.GroupDoesNotExistException;
import io.ethertale.findadicethymeleaf.exceptions.GroupHeroAlreadyInGroupException;
import io.ethertale.findadicethymeleaf.exceptions.GroupPostTooLongOrTooShort;
import io.ethertale.findadicethymeleaf.group.model.Group;
import io.ethertale.findadicethymeleaf.group.repo.DeletedGroupPostsRepo;
import io.ethertale.findadicethymeleaf.group.repo.DeletedGroupsRepo;
import io.ethertale.findadicethymeleaf.group.repo.GroupRepo;
import io.ethertale.findadicethymeleaf.hero.model.Hero;
import io.ethertale.findadicethymeleaf.hero.repo.HeroRepo;
import io.ethertale.findadicethymeleaf.post.model.GroupPost;
import io.ethertale.findadicethymeleaf.post.repo.GroupPostRepo;
import io.ethertale.findadicethymeleaf.web.dto.GroupCreateDTO;
import io.ethertale.findadicethymeleaf.web.dto.GroupPostDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class GroupService {

    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);
    private final GroupRepo groupRepo;
    private final HeroRepo heroRepo;
    private final GroupPostRepo groupPostRepo;
    private final DeletedReportRepo deletedReportRepo;

    @Autowired
    public GroupService(GroupRepo groupRepo, HeroRepo heroRepo, GroupPostRepo groupPostRepo, DeletedReportRepo deletedReportRepo) {
        this.groupRepo = groupRepo;
        this.heroRepo = heroRepo;
        this.groupPostRepo = groupPostRepo;
        this.deletedReportRepo = deletedReportRepo;
    }

    public List<Group> getAllGroupsSortedByCreationDesc() {
        return groupRepo.findAll().stream().sorted(Comparator.comparing(Group::getCreatedAt).reversed()).toList();
    }

    public Group getSpecificGroup(UUID id){
        return groupRepo.findById(id).orElse(null);
    }

    @Transactional
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

    @Transactional
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

    @Transactional
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

    @Transactional
    public void createPost(GroupPostDTO postDTO, UUID groupId, Hero hero) {
        if  (postDTO.getDescription().isBlank() || postDTO.getDescription().length() > 1000 || postDTO.getTitle().isBlank() || postDTO.getTitle().length() > 100 || postDTO.getTitle().length() <= 2) {
            throw new GroupPostTooLongOrTooShort();
        }

        GroupPost newPost = GroupPost.builder()
                .group(groupRepo.findById(groupId).orElse(null))
                .title(postDTO.getTitle())
                .createdAt(LocalDateTime.now())
                .hero(hero)
                .comments(new HashSet<>())
                .description(postDTO.getDescription())
                .image(hero.getImageUrl())
                .likes(0)
                .build();

        groupPostRepo.save(newPost);
        log.info("User {} created post {} in group {}", hero.getUser().getId(), newPost.getId(), groupId);
    }

    public List<Group> searchGroups(String query) {
        return groupRepo.findGroupByNameContainingIgnoreCase(query);
    }

    @Transactional
    public void deletePost(UUID loggedUserId, UUID postId, UUID groupId) {
        Optional<GroupPost> groupPostById = groupPostRepo.getGroupPostById(postId);
        DeletedReport newReport = DeletedReport.builder()
                .type(DeletedReportType.GROUP_POST)
                .objectId(groupPostById.get().getId())
                .title(groupPostById.get().getTitle())
                .content(groupPostById.get().getDescription())
                .deletedByUserId(groupPostById.get().getHero().getUser().getId())
                .deletedOn(LocalDateTime.now())
                .build();
        deletedReportRepo.save(newReport);

        groupPostRepo.deleteById(postId);
        log.info("User with ID {} deleted a post from group with ID {}.\nPost Title -> {}\nPost Content -> {}", loggedUserId, groupId, groupPostById.get().getTitle(), groupPostById.get().getDescription());
    }

    @Transactional
    public void deleteGroup(UUID groupId, UUID id) {
        Optional<Group> groupById = groupRepo.getGroupById(groupId);
        DeletedReport newReport = DeletedReport.builder()
                .type(DeletedReportType.GROUP)
                .objectId(groupById.get().getId())
                .title(groupById.get().getName())
                .content(groupById.get().getDescription())
                .deletedByUserId(groupById.get().getCreatedBy().getUser().getId())
                .deletedOn(LocalDateTime.now())
                .build();
        deletedReportRepo.save(newReport);

        for (Hero hero : groupById.get().getHeroes()){
            hero.getGroups().remove(groupById.get());
        }

        groupRepo.deleteById(groupId);
        log.info("User with ID {} deleted a group with ID {}.", id, groupId);
    }
}
