package io.ethertale.findadicethymeleaf.hero.model;

import io.ethertale.findadicethymeleaf.user.model.Genders;
import io.ethertale.findadicethymeleaf.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "hero")
public class Hero {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(mappedBy = "hero")
    private User user;

    @Column(nullable = false)
    private String name;

    @Column
    private int age;

    @Column
    @Enumerated(EnumType.STRING)
    private Genders gender;

    @Column(nullable = false, name = "char_class")
    @Enumerated(EnumType.STRING)
    private Classes charClass;

    @Column(length = 10000)
    private String description;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    private Alignment alignment;

    @Column(nullable = false)
    private Backgrounds background;

    @Column(nullable = false)
    private int strength;

    @Column(nullable = false)
    private int dexterity;

    @Column(nullable = false)
    private int constitution;

    @Column(nullable = false)
    private int intelligence;

    @Column(nullable = false)
    private int wisdom;

    @Column(nullable = false)
    private int charisma;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    public Hero() {
    }

    public Hero(UUID id, User user, String name, int age, Genders gender, Classes charClass, String description, String imageUrl, Alignment alignment, Backgrounds background, int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.charClass = charClass;
        this.description = description;
        this.imageUrl = imageUrl;
        this.alignment = alignment;
        this.background = background;
        this.strength = strength;
        this.dexterity = dexterity;
        this.constitution = constitution;
        this.intelligence = intelligence;
        this.wisdom = wisdom;
        this.charisma = charisma;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Genders getGender() {
        return gender;
    }

    public void setGender(Genders gender) {
        this.gender = gender;
    }

    public Classes getCharClass() {
        return charClass;
    }

    public void setCharClass(Classes charClass) {
        this.charClass = charClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public Backgrounds getBackground() {
        return background;
    }

    public void setBackground(Backgrounds background) {
        this.background = background;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getConstitution() {
        return constitution;
    }

    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getWisdom() {
        return wisdom;
    }

    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    public int getCharisma() {
        return charisma;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
