package com.example.gymrat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainingPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    private Integer difficultyLevel;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany(mappedBy = "trainingPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseInPlan> exercisesInPlan = new ArrayList<>();

    @OneToMany(mappedBy = "trainingPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "trainingPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingPlanLike> likes = new ArrayList<>();

    @Formula("(SELECT COALESCE(SUM(CASE WHEN l.is_like = true THEN 1 ELSE -1 END), 0) FROM training_plan_like l WHERE l.training_plan_id = id)")
    private Integer likeCount;


    @ElementCollection(targetClass = CategoryName.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "training_plan_categories", joinColumns = @JoinColumn(name = "training_plan_id"))
    @Column(name = "category")
    private Set<CategoryName> categories = new HashSet<>();

    @OneToMany(mappedBy = "trainingPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteTrainingPlan> favoriteTrainingPlans = new ArrayList<>();


}
