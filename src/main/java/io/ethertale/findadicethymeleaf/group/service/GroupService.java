package io.ethertale.findadicethymeleaf.group.service;

import io.ethertale.findadicethymeleaf.group.model.Group;
import io.ethertale.findadicethymeleaf.group.repo.GroupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class GroupService {

    private final GroupRepo groupRepo;

    @Autowired
    public GroupService(GroupRepo groupRepo) {
        this.groupRepo = groupRepo;
    }

    public List<Group> getAllGroupsSortedByCreationDesc() {
        return groupRepo.findAll().stream().sorted(Comparator.comparing(Group::getCreatedAt).reversed()).toList();
    }
}
